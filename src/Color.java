public class Color
{
    /**
     * R, G, B shuold all be in range [0,1] (inclusive) where 0 is black and 1 is white.
     *
     * For example, the color orange is equal to Color(1.0, 0.7, 0.0)
     */
    public final double r, g, b;
    
    public Color(double r, double y, double z)
    {
        this.r = r;
        this.g = y;
        this.b = z;
    }
    
    public Color()
    {
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }
    
    public String toString()
    {
        return "Color(" + r + ", " + g + ", " + b + ")";
    }
    
    /*
    public double squareMagnitude()
    {
        return r * r + g * g + b * b;
    }
    
    public double magnitude()
    {
        return Math.sqrt(squareMagnitude());
    }
    
    public Color plus(Color v2)
    {
        return new Color(r + v2.r, g + v2.g, b + v2.b);
    }
    
    public Color minus(Color v2)
    {
        return new Color(r - v2.r, g - v2.g, b - v2.b);
    }
    
    public Color scaledBy(double factor)
    {
        return new Color(r * factor, g * factor, b * factor);
    }
    
    public double dot(Color v2)
    {
        return r * v2.r + g * v2.g + b * v2.b;
    }
    
    public Color cross(Color v2)
    {
        double x1 = r, x2 = v2.r, y1 = g, y2 = v2.g, z1 = b, z2 = v2.b;
        return new Color(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2, x1 * y2 - y1 * x2);
    }*/
}
