public class Camera
{
    public final Vec3 position;
    public final Vec3 forward;
    public final Vec3 up;
    public final double screenDistance;
    public final double screenWidth;
    //extras
    public final Vec3 right;
    
    public Camera(Vec3 position, Vec3 lookAt, Vec3 up, double screenDistance, double screenWidth)
    {
        this.position = position;
        this.forward = lookAt.minus(position).normalized();
        this.up = up.projectionPerpendicularTo(forward).normalized(); // need to fix to be perpendicular
        this.screenDistance = screenDistance;
        this.screenWidth = screenWidth;
        right = up.cross(forward).normalized();
    }
}
