public class Plane extends Shape
{
    final Vec3 normal;
    final double offset;
    
    public Plane(Vec3 normal, double offset, int materialIndex)
    {
    	super(materialIndex);
        this.normal = normal;
        this.offset = offset;        
        
    }
    
    public Vec3 FindIntersection(Vec3 P0, Vec3 pv)
    {
		Vec3 V = pv.minus(P0).normalized();
    	double t = -(P0.mult(normal) + offset) / V.mult(normal);    	
    	return P0.plus(V.scaledBy(t));  
    }
}
