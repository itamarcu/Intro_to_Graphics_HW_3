public class Sphere
{
    final Vec3 center;
    final double radius;
    final int materialIndex;
    
    public Sphere(Vec3 center, double radius, int materialIndex)
    {
        this.center = center;
        this.radius = radius;
        this.materialIndex = materialIndex;
    }
}
