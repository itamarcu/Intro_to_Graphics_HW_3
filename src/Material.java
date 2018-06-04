
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
    
    public Color getColor(){
    	return GetBaseColor();
    	
    	//Specular color phongSpecularity
    	
    	//Reflection color
    	//Transparency
    	// output color = (background color) * transparency + (diffuse + specular) * (1 - transparency)	+ (reflection color) 
    }
    
    public Color getDiffuse(){
    	return diffuseColor;
    }
    
    public Color getSpecularColor(){
    	return specularColor;
    }
    
    public Color getShade(){
    	return new Color(); //todo
    }
    private Color GetBaseColor(){
    	// light * diffuseColor
    	return diffuseColor;
    }
}
