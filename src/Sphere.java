public class Sphere extends Shape
{
    final Vec3 center;
    final double radius;
    final double radius_squared;
    
    public Sphere(Vec3 center, double radius, int materialIndex)
    {
        super(materialIndex);
        this.center = center;
        this.radius = radius;
        radius_squared = radius * radius;
    }
    
    public String toString()
    {
        return "Sphere(" + center + ", " + radius + ", " + materialIndex + ")";
    }
    
    @Override
    public Intersection findRayIntersection(Vec3 origin, Vec3 direction)
    {
        Vec3 point_to_center = center.minus(origin);
        if (point_to_center.squareMagnitude() < radius_squared)
            return null; // point is inside sphere
        double projection = point_to_center.dot(direction);
        if (projection < 0)
            return null; // intersection is "behind" ray
        double extra = Math.sqrt(radius_squared - projection * projection);
        //points of intersection are: point + direction*(projection +- extra)
        //closest point with minus, farthest point with plus
        Vec3 intersection_position = origin.plus(direction.scaledBy(projection - extra));
        Vec3 normal = intersection_position.minus(center).normalized();
        return new Intersection(intersection_position, normal, materialIndex);
    }
}
