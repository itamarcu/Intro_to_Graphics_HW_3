public class Color
{
    /**
     * R, G, B are clamped to range [0,1] (inclusive) where 0 is black and 1 is white.
     * <p>
     * For example, the color orange is equal to Color(1.0, 0.7, 0.0)
     */
    public final double r, g, b;
    
    public Color(double r, double g, double b)
    {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
    }
    
    public Color()
    {
        this(0, 0, 0);
    }
    
    /**
     * Clamp to [0,1]
     */
    private static double clamp(double x)
    {
        if (x < 0)
            return 0;
        if (x > 1)
            return 1;
        return x;
    }
    
    public Color plus(Color color)
    {
        return new Color(r + color.r, g + color.g, b + color.b);
    }
    
    public Color mul(Color c2)
    {
        return new Color(r * c2.r, g * c2.g, b * c2.b);
    }
    
    public Color scaledBy(double factor)
    {
        return new Color(r * factor, g * factor, b * factor);
    }
    
    public byte getRed()
    {
        return (byte) (255 * r);
    }
    
    public byte getGreen()
    {
        return (byte) (255 * g);
    }
    
    public byte getBlue()
    {
        return (byte) (255 * b);
    }
    
    public String toString()
    {
        return "Color(" + r + ", " + g + ", " + b + ")";
    }
    
    public double grayscale()
    {
        return (r + g + b) / 3;
    }
}
