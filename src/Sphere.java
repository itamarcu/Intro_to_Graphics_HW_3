public class Sphere extends Shape
{
    final Vec3 center;
    final double radius;
    
    public Sphere(Vec3 center, double radius, int materialIndex)
    {
    	super(materialIndex);
        this.center = center;
        this.radius = radius;
    }
    
    public Vec3 FindIntersection(Vec3 P0, Vec3 pv)
    {
    	double angle = Math.atan2(P0.y, P0.x) - Math.atan2(pv.y, pv.x);
    	double L =  center.minus(P0).magnitude();
    	double	tca = L* Math.cos(angle);
    	double d2 =  Math.sqrt(Math.pow(L, 2)-Math.pow(tca, 2));//center.minus(P0));
    	if (d2 > Math.pow(radius, 2)) 
    		return null;//new Vec3(0,0,0);
    	double thc = Math.sqrt(Math.pow(radius, 2) - d2);
    	double t = tca - thc;// and tca + thc
    	Vec3 V = pv.minus(P0).normalized();
    	return P0.plus(V.scaledBy(t));   
    }
}
