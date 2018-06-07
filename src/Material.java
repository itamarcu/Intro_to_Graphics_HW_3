
public class Material
{
    public final Color diffuseColor;
    public final Color specularColor;
    public final Color reflectionColor;
    public final double phongSpecularity;
    public final double transparency; // 0 = not transparent (opaque), 1 = fully transparent (invisible)
    
    public Material(Color diffuseColor, Color specularColor, Color reflectionColor,
                    double phongSpecularity, double transparency)
    {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.reflectionColor = reflectionColor;
        this.phongSpecularity = phongSpecularity;
        this.transparency = transparency;
    }

    @Override
    public String toString() {
        return "Material{" +
                "diffuseColor=" + diffuseColor +
                ", specularColor=" + specularColor +
                ", reflectionColor=" + reflectionColor +
                ", phongSpecularity=" + phongSpecularity +
                ", transparency=" + transparency +
                '}';
    }

    public Color getColor()
    {
        return GetBaseColor(); //todo?
        
        //Specular color phongSpecularity
        
        //Reflection color
        //Transparency
        // output color = (background color) * transparency + (diffuse + specular) * (1 - transparency)	+ (reflection
        // color)
    }
    
    public Color getShade()
    {
        return new Color(); //todo
    }
    
    private Color GetBaseColor()
    {
        // light * diffuseColor
        return diffuseColor; //todo
    }
}
