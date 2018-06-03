public class Camera
{
    public final Vec3 position;
    public final Vec3 forward;
    public final Vec3 up;
    public final double screenDistance;
    public final double screenWidth;
    
    public Camera(Vec3 position, Vec3 lookAt, Vec3 up, double screenDistance, double screenWidth)
    {
        this.position = position;
        this.forward = lookAt.minus(position);
        this.up = up;
        this.screenDistance = screenDistance;
        this.screenWidth = screenWidth;
    }
}
