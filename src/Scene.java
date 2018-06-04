import java.util.ArrayList;
import java.util.List;

class Scene
{
    //General settings / singletons
    
    public Camera camera;
    public Color backgroundColor;
    public int shadowRayCount;
    public int maximumRecursionCount;
    public int superSamplingLevel;
    
    // Things
    
    public List<Material> materials;
    public List<Light> lights;
    public List<Sphere> spheres;
    public List<Plane> planes;
    public List<Triangle> triangles;
    
    public Scene()
    {
        materials = new ArrayList<>();
        lights = new ArrayList<>();
        spheres = new ArrayList<>();
        planes = new ArrayList<>();
        triangles = new ArrayList<>();
    }
    
    public Shape FindIntersection(Vec3 P0, Vec3 pv)
    {
    	Vec3 intersection;
    	Shape closestShape = null;
    	double dist, minDist=Integer.MAX_VALUE;
    	// check all spheres
    	for(Sphere sphere :spheres)
    	{
    		intersection = sphere.FindIntersection(P0, pv);
    		if(intersection!=null){
	    		dist = P0.minus(intersection).magnitude();
	    		if(dist<minDist)
	    			closestShape = sphere;
    		}
    	}
    	// check all triangles
    	for(Triangle triangle :triangles)
    	{
    		intersection = triangle.FindIntersection(P0, pv);
    		if(intersection!=null){
	    		dist = P0.minus(intersection).magnitude();
	    		if(dist<minDist)
	    			closestShape = triangle;
    		}
    	}
    	// check all planes
    	for(Plane plane :planes)
    	{
    		intersection = plane.FindIntersection(P0, pv);
    		if(intersection!=null){
	    		dist = P0.minus(intersection).magnitude();
	    		if(dist<minDist)
	    			closestShape = plane;
    		}
    	}
    	return closestShape;
    }
    
    public Material getMaterial(int materialIndex){
    	return materials.get(materialIndex);
    }
    
    public Color getColor(Shape hit){
    	Material mat = getMaterial(hit.materialIndex);
		Color color= backgroundColor.scaledBy(mat.transparency).plus((mat.getDiffuse().plus(mat.specularColor)).scaledBy(1 - mat.transparency));
				//ambient* mat.getDiffuse();
		for(Light light :lights)
		{
			//todo
//			color=color.plus(mat.getShade());
//			(ray, hit, L->getDir(), L->getColor());
		}
		return color;
	}
    
}
