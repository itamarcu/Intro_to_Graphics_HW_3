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
            return new Intersection(origin, point_to_center.normalized().scaledBy(-1), direction, materialIndex); //
        // point is inside sphere
        double projection_length = point_to_center.dot(direction);
        if (projection_length < 0)
            return null; // intersection is "behind" ray
        double projection_normal_length_sqr = point_to_center.squareMagnitude() - projection_length * projection_length;
        if (projection_normal_length_sqr > radius_squared)
            return null; // no intersection at all
        double extra = Math.sqrt(radius_squared - projection_normal_length_sqr);
        //points of intersection are: point + direction*(projection_length +- extra)
        //closest point with minus, farthest point with plus
        Vec3 intersection_position = origin.plus(direction.scaledBy(projection_length - extra));
        Vec3 normal = intersection_position.minus(center).normalized();
        return new Intersection(intersection_position, normal, direction, materialIndex);
    }
}
