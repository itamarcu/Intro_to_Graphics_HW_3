import java.util.ArrayList;
import java.util.List;

class Scene
{
    //General settings / singletons
    public Color backgroundColor;
    public int shadowRayCount;
    public int maximumRecursionCount;
    public int superSamplingLevel;
    
    // Things
    
    public List<Material> materials;
    public List<Light> lights;
    public List<Shape> shapes;
    
    public Scene()
    {
        materials = new ArrayList<>();
        lights = new ArrayList<>();
        shapes = new ArrayList<>();
    }
    
    /**
     * Calculates for a given point and direction the closest shape it hits
     */
    public Intersection raycast(Vec3 point, Vec3 direction)
    {
        Intersection closestIntersection = null;
        double min_dist_sqr = Integer.MAX_VALUE;
        for (Shape shape : shapes)
        {
            Intersection intersection = shape.findRayIntersection(point, direction);
            if (intersection != null)
            {
                double dist_sqr = point.minus(intersection.position).squareMagnitude();
                if (dist_sqr < min_dist_sqr)
                {
                    min_dist_sqr = dist_sqr;
                    closestIntersection = intersection;
                }
            }
        }
        return closestIntersection;
    }
    
    /**
     * @param materialIndex 1-based index
     * @return material with this index (from 0-based materials list)
     */
    public Material getMaterial(int materialIndex)
    {
        return materials.get(materialIndex - 1);
    }
    
    //    public Color getColor(Intersection hit) {
    //        if (hit == null)
    //            return backgroundColor;
    //        Material mat = getMaterial(hit.materialIndex);
    //        Color color = backgroundColor.scaledBy(mat.transparency).plus((mat.diffuseColor.plus(mat.specularColor)
    // ).scaledBy(1 - mat.transparency));
    //        //ambient* mat.getDiffuse();
    //        for (Light light : lights) {
    //            //todo
    ////			color=color.plus(mat.getShade());
    ////			(ray, hit, L->getDir(), L->getColor());
    //        }
    //        return color;
    //    }
    
    /**
     * Returns the color of the intersection, continues recursively.
     * If maximumRecursionCount is reached, or there is no object intersected, returns background color.
     * Otherwise, calculates - diffused + specular + transparency + reflection
     */
    public Color getColor(Intersection hit, int recursionCount)
    {
        if (hit == null || recursionCount == maximumRecursionCount)
            return backgroundColor;
    
        Material mat = getMaterial(hit.materialIndex);
        Color color = new Color(0, 0, 0);
        
        //ambient lighting
        color = color.plus(backgroundColor).scaledBy(mat.transparency);
        
        Vec3 point = hit.position;
        for (Light light : lights)
        {
            Vec3 reverseLightDirection = light.position.minus(point).normalized();
            Vec3 start = point.plus(reverseLightDirection.scaledBy(0.001));
            double illumination = 1.0;
            double invCountOfShadowRays = 1.0 / shadowRayCount;
            double shadowRayShadowFraction = 1.0 / shadowRayCount / shadowRayCount * light.shadowIntensity;
            Vec3 lightWidthRight = reverseLightDirection.anyPerpendicular().scaledBy(light.width);
            Vec3 lightWidthDown = lightWidthRight.cross(reverseLightDirection.normalized()).scaledBy(light.width);
            double rayLength;
            for (int xx = 0; xx < shadowRayCount; xx++)
                for (int yy = 0; yy < shadowRayCount; yy++)
                {
                    double randomUp = Math.random(), randomRight = Math.random();
                    Vec3 pointNearLight = light.position
                            .plus(lightWidthRight.scaledBy(
                                    (-shadowRayCount / 2 + xx + randomRight - 0.5) * invCountOfShadowRays))
                            .plus(lightWidthDown.scaledBy(
                                    (-shadowRayCount / 2 + yy + randomUp - 0.5) * invCountOfShadowRays));
            
                    Vec3 reverseShadowDirection = pointNearLight.minus(start).normalized();
                    rayLength = start.minus(pointNearLight).magnitude();
                    double fractionOfLightLeftInRay = 1.0;
                    for (Shape s : shapes)
                    {
                        Intersection shadowHit = s.findRayIntersection(start, reverseShadowDirection);
                        if (shadowHit != null && shadowHit.position.minus(start).magnitude() < rayLength)
                        {
                            fractionOfLightLeftInRay -= (1 - getMaterial(s.materialIndex).transparency);
                            // TODO  * light.shadowIntensity ??? (seems like it works without)
                            if (fractionOfLightLeftInRay < 0)
                            {
                                fractionOfLightLeftInRay = 0;
                                break; //TODO transparency here?
                            }
                        }
                    }
                    illumination -= shadowRayShadowFraction * (1 - fractionOfLightLeftInRay);
                }
            
            Color lightAmount = light.color.scaledBy(illumination);
            
            if (illumination > 0)
            {
                //diffuse lighting
                Color diffuseColor = lightAmount.mul(mat.diffuseColor.scaledBy(
                        hit.normal.dot(reverseLightDirection)));
                //specular lighting
                Vec3 reflectionDirection = reverseLightDirection.reflectedBy(hit.normal);
                Color specularColor = lightAmount.mul(mat.specularColor.scaledBy(light.specularIntensity
                        * Math.pow(reflectionDirection.dot(hit.direction), mat.phongSpecularity)));
    
                color = color.plus(
                        diffuseColor.plus(specularColor).scaledBy(1 - mat.transparency));
            }
        }
        
        // Reflection color
        Vec3 mirrorDir = hit.getReflactionVector();
        Intersection rayMirror = raycast(hit.position, mirrorDir);
        Color reflectionColor = getColor(rayMirror, recursionCount + 1).mul(mat.reflectionColor);
        color = color.plus(reflectionColor);
        
        // Transparency color
        if (mat.transparency > 0)
        {
            Intersection nextSurface = raycast(hit.outPosition, hit.direction);
            color = color.plus(getColor(nextSurface, recursionCount + 1).scaledBy(mat.transparency));
        }
        
        return color;
    }
    
}
