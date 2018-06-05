public class Triangle extends Shape
{
    final Vec3[] vertices; // size = 3
    
    public Triangle(Vec3 vertex1, Vec3 vertex2, Vec3 vertex3, int materialIndex)
    {
        super(materialIndex);
        this.vertices = new Vec3[]{vertex1, vertex2, vertex3};
    }
    
    public String toString()
    {
        return "Triangle(" + vertices[0] + ", " + vertices[1] + ", " + vertices[2] + ", " + materialIndex + ")";
    }
    
    @Override
    public Intersection findRayIntersection(Vec3 origin, Vec3 direction)
    {
        // first check is in plane of triangle
        // check point is inside the triangle
        return null;//TODO
    }
}
