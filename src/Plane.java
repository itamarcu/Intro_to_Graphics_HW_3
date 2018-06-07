public class Plane extends Shape
{
    final Vec3 normal;
    final double offset;
    
    public Plane(Vec3 normal, double offset, int materialIndex)
    {
        super(materialIndex);
        this.normal = normal.normalized();
        this.offset = offset;
    }
    
    public String toString()
    {
        return "Plane(" + normal + ", " + offset + ", " + materialIndex + ")";
    }
    
    @Override
    public Intersection findRayIntersection(Vec3 origin, Vec3 direction, boolean shadowCheck)
    {
        double dirDotNorm = direction.dot(normal);
        if (Math.abs(dirDotNorm) < 0.01)
            return null; // plane is parallel to ray
        double t = (offset - origin.dot(normal)) / dirDotNorm;
        if (t <= 0)
            return null; // plane is behind origin
        if (t == Double.NaN)
            return null; // plane is behind origin
        Vec3 intersection_position = origin.plus(direction.scaledBy(t));
        if (intersection_position.minus(origin).squareMagnitude() < 0.01)
            return null; // likely intersect with same plane twice
        Vec3 hitNormal = normal.scaledBy(-dirDotNorm);
        return new Intersection(intersection_position, hitNormal, direction, materialIndex);
    }
}
