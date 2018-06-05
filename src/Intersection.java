public class Intersection
{
    public final Vec3 position;
    public final Vec3 normal;
    public final int materialIndex;
    
    public Intersection(Vec3 position, Vec3 normal, int materialIndex)
    {
        this.position = position;
        this.normal = normal.normalized();
        this.materialIndex = materialIndex;
    }
    
    @Override
    public String toString()
    {
        return "Intersection(" + position + ", " + normal + ", " + materialIndex + ")";
    }
}
