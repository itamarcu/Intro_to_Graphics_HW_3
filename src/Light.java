
public class Light
{
    final Vec3 position;
    final Color color;
    final double specularIntensity;
    final double shadowIntensity;
    final double width; // width/radius, used for soft shadows
    
    public Light(Vec3 position, Color color, double specularIntensity, double shadowIntensity, double width)
    {
        this.position = position;
        this.color = color;
        this.specularIntensity = specularIntensity;
        this.shadowIntensity = shadowIntensity;
        this.width = width;
    }
}
