public abstract class Shape
{
    final int materialIndex;
    
    public Shape(int materialIndex)
    {
        this.materialIndex = materialIndex;
    }
    
    public abstract Intersection findRayIntersection(Vec3 origin, Vec3 direction, boolean shadowCheck);
}