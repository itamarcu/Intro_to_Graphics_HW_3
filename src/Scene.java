import java.util.ArrayList;
import java.util.List;

class Scene
{
    //General settings / singletons
    
    public Camera camera;
    public Color backgroundColor;
    public int shadowRayCount;
    public int maximumRecursionCount;
    public int superSamplingLevel;
    
    // Things
    
    public List<Material> materials;
    public List<Light> lights;
    public List<Sphere> spheres;
    public List<Plane> planes;
    public List<Triangle> triangles;
    
    public Scene()
    {
        materials = new ArrayList<>();
        lights = new ArrayList<>();
        spheres = new ArrayList<>();
        planes = new ArrayList<>();
        triangles = new ArrayList<>();
    }
}
