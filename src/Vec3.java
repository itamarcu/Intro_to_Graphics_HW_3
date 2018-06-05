public class Vec3
{
    public final double x, y, z;
    //extras
    private boolean calculatedExtras;
    private boolean isNormalized;
    private double squareMagnitude;
    private double magnitude;
    
    public Vec3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        calculatedExtras = false;
    }
    
    private void calculateExtras()
    {
        if (calculatedExtras)
            return;
        this.squareMagnitude = x * x + y * y + z * z;
        this.magnitude = Math.sqrt(squareMagnitude);
        this.isNormalized = squareMagnitude == 1 || squareMagnitude == 0;
        calculatedExtras = true;
    }
    
    public String toString()
    {
        return "Vec3(" + x + ", " + y + ", " + z + ")";
    }
    
    public Vec3 plus(Vec3 v2)
    {
        return new Vec3(x + v2.x, y + v2.y, z + v2.z);
    }
    
    public Vec3 minus(Vec3 v2)
    {
        return new Vec3(x - v2.x, y - v2.y, z - v2.z);
    }
    
    public Vec3 scaledBy(double factor)
    {
        return new Vec3(x * factor, y * factor, z * factor);
    }
    
    public double squareMagnitude()
    {
        calculateExtras();
        return squareMagnitude;
    }
    
    public double magnitude()
    {
        calculateExtras();
        return magnitude;
    }
    
    public Vec3 normalized()
    {
        calculateExtras();
        if (isNormalized)
            return this;
        double magnitude = magnitude();
        return new Vec3(x / magnitude, y / magnitude, z / magnitude);
    }
    
    public double dot(Vec3 v2)
    {
        return x * v2.x + y * v2.y + z * v2.z;
    }
    
    public Vec3 mul(Vec3 v2)
    {
        return new Vec3(x * v2.x, y * v2.y, z * v2.z);
    }
    
    public Vec3 cross(Vec3 v2)
    {
        double x1 = x, x2 = v2.x, y1 = y, y2 = v2.y, z1 = z, z2 = v2.z;
        return new Vec3(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2, x1 * y2 - y1 * x2);
    }
    
    public Vec3 projectedOn(Vec3 v2)
    {
        return v2.normalized().scaledBy(this.dot(v2) / v2.magnitude());
    }
    
    public Vec3 projectionPerpendicularTo(Vec3 v2)
    {
        return this.minus(projectedOn(v2));
    }
    
    /*
     * ↖     ↗
     *  \ | /
     *   \|/
     * ---+---
     */
    public Vec3 reflectedBy(Vec3 normal)
    {
        normal = normal.normalized();
        return this.minus(normal.scaledBy(2 * this.dot(normal)));
        //        Vec3 perp = this.projectionPerpendicularTo(normal);
        //        return this.minus(perp).minus(perp);
    }
    
    public Vec3 anyPerpendicular()
    {
        // adding some extra positive to get a different vector
        return this.cross(new Vec3(this.x + 17, this.y + 17, this.z + 17)).normalized();
    }
}
