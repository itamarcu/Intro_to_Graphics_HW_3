public class Triangle
{
    final Vec3[] vertices; // size = 3
    final int materialIndex;
    
    public Triangle(Vec3 vertex1, Vec3 vertex2, Vec3 vertex3, int materialIndex)
    {
        this.vertices = new Vec3[]{vertex1, vertex2, vertex3};
        this.materialIndex = materialIndex;
    }
}
