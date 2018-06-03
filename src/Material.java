public class Material
{
    final Color diffuseColor;
    final Color specularColor;
    final Color reflectionColor;
    final double phongSpecularity;
    final double transparency;
    
    public Material(Color diffuseColor, Color specularColor, Color reflectionColor, double
            phongSpecularity, double transparency)
    {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.reflectionColor = reflectionColor;
        this.phongSpecularity = phongSpecularity;
        this.transparency = transparency;
    }
}
