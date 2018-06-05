public class Intersection
{
    public final Vec3 position;
    public final Vec3 outPosition;
    public final Vec3 normal;
    public final int materialIndex;
    public final Vec3 direction;
    
    public Intersection(Vec3 position, Vec3 outPosition, Vec3 normal, Vec3 direction, int materialIndex)
    {
        this.position = position;
        this.outPosition = outPosition;
        this.normal = normal.normalized();
        this.direction = direction;
        this.materialIndex = materialIndex;
    }
    
    @Override
    public String toString()
    {
        return "Intersection(" + position + ", " + normal + ", " + direction + ", " + materialIndex + ")";
    }
}
