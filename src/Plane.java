public class Plane
{
    final Vec3 normal;
    final double offset;
    final int materialIndex;
    
    public Plane(Vec3 normal, double offset, int materialIndex)
    {
        this.normal = normal;
        this.offset = offset;
        this.materialIndex = materialIndex;
    }
}
