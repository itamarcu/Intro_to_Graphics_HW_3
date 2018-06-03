public class Vec3
{
    public final double x, y, z;
    
    public Vec3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public String toString()
    {
        return "Vec3(" + x + ", " + y + ", " + z + ")";
    }
    
    public double squareMagnitude()
    {
        return x * x + y * y + z * z;
    }
    
    public double magnitude()
    {
        return Math.sqrt(squareMagnitude());
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
    
    public Vec3 normalized()
    {
        if (x == 0 && y == 0 && z == 0)
            return new Vec3(0, 0, 0);
        double magnitude = magnitude();
        return new Vec3(x / magnitude, y / magnitude, z / magnitude);
    }
    
    public double dot(Vec3 v2)
    {
        return x * v2.x + y * v2.y + z * v2.z;
    }
    
    public Vec3 cross(Vec3 v2)
    {
        double x1 = x, x2 = v2.x, y1 = y, y2 = v2.y, z1 = z, z2 = v2.z;
        return new Vec3(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2, x1 * y2 - y1 * x2);
    }
}
