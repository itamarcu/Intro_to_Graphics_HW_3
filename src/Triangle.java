public class Triangle extends Shape
{
    final Vec3[] vertices; // size = 3
    
    public Triangle(Vec3 vertex1, Vec3 vertex2, Vec3 vertex3, int materialIndex)
    {
    	super(materialIndex);
        this.vertices = new Vec3[]{vertex1, vertex2, vertex3};
    }
    
    public Vec3 FindIntersection(Vec3 P0, Vec3 v)
    {
    	// first check is in plane of triangle
    	// check point is inside the triangle
    	return null;//TODO
    }
}
