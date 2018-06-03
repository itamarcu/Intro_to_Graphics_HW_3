import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;

/**
 * Main class for ray tracing exercise.
 */
public class RayTracer
{
    private int imageWidth;
    private int imageHeight;
    private Scene scene;
    
    /**
     * Runs the ray tracer. Takes scene file, output image file and optional image size as input.
     */
    public static void main(String[] args)
    {
        
        try
        {
            
            RayTracer tracer = new RayTracer();
            
            // Default values:
            tracer.imageWidth = 500;
            tracer.imageHeight = 500;
            
            if (args.length < 2)
                throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and " +
                        "an output image file for rendering.");
            
            String sceneFileName = args[0];
            String outputFileName = args[1];
            
            if (args.length > 3)
            {
                tracer.imageWidth = Integer.parseInt(args[2]);
                tracer.imageHeight = Integer.parseInt(args[3]);
            }
            
            
            tracer.parseScene(sceneFileName);
            
            tracer.renderScene(outputFileName);
        }
        catch (RayTracerException e)
        {
            e.printStackTrace();
        }
        
        
    }
    
    /* Easy to use parsing, uses static helper variable. Don't call this too many times.
     */
    static String[] _params;
    static int _paramsIndex;
    
    private void startSmartParsing(String[] params)
    {
        _params = params;
        _paramsIndex = 0;
    }
    
    // See above comments
    private int nextInt()
    {
        return Integer.parseInt(_params[_paramsIndex++]);
    }
    
    // See above comments
    private double nextDouble()
    {
        return Double.parseDouble(_params[_paramsIndex++]);
    }
    
    // See above comments
    private Vec3 nextVec3()
    {
        return new Vec3(nextDouble(), nextDouble(), nextDouble());
    }
    
    // See above comments
    private Color nextColor()
    {
        return new Color(nextDouble(), nextDouble(), nextDouble());
    }
    
    /* End of the parsing-related helper methods
    */
    
    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
    private void parseScene(String sceneFileName) throws RayTracerException
    {
        scene = new Scene();
        
        try
        {
            FileReader fr = new FileReader(sceneFileName);
            BufferedReader r = new BufferedReader(fr);
            System.out.println("Started parsing scene file " + sceneFileName);
            
            String line;
            int lineNum = 0;
            while ((line = r.readLine()) != null)
            {
                line = line.trim();
                ++lineNum;
                
                if (line.isEmpty() || (line.charAt(0) == '#'))
                {  // This line in the scene file is a comment
                    continue;
                }
                String code = line.substring(0, 3).toLowerCase();
                // Split according to white space characters:
                String[] params = line.substring(3).trim().toLowerCase().split("\\s+");
                startSmartParsing(params); // sets static variables for the next___() methods
                
                switch (code)
                {
                    case "cam":
                        // px   	py   	pz 	lx  	ly  	lz 	ux  	uy  	uz 	sc_dist	sc_width
                        scene.camera = new Camera(nextVec3(), nextVec3(), nextVec3(), nextDouble(), nextDouble());
                        System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                        break;
                    case "set":
                        // bgr  	bgg  	bgb	sh_rays	rec_max SS
                        scene.backgroundColor = nextColor();
                        scene.shadowRayCount = nextInt();
                        scene.maximumRecursionCount = nextInt();
                        scene.superSamplingLevel = nextInt();
                        System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                        break;
                    case "mtl":
                        // dr    	dg    	db	sr   	sg   	sb 	rr   	rg  	rb	phong 	trans
                        Material m = new Material(nextColor(), nextColor(), nextColor(), nextDouble(), nextDouble());
                        scene.materials.add(m);
                        System.out.println(String.format("Parsed material (line %d)", lineNum));
                        break;
                    case "sph":
                        // Add code here to parse sphere parameters
                        // Example (you can implement this in many different ways!):
                        // Sphere sphere = new Sphere();
                        // sphere.setCenter(params[0], params[1], params[2]);
                        // sphere.setRadius(params[3]);
                        // sphere.setMaterial(params[4]);
    
                        // cx   	cy   	cz  	radius 	mat_idx
                        Sphere s = new Sphere(nextVec3(), nextDouble(), nextInt());
                        scene.spheres.add(s);
                        System.out.println(String.format("Parsed sphere (line %d)", lineNum));
                        break;
                    case "pln":
                        // nx	ny	nz	offset	mat_idx
                        Plane p = new Plane(nextVec3(), nextDouble(), nextInt());
                        scene.planes.add(p);
                        System.out.println(String.format("Parsed plane (line %d)", lineNum));
                        break;
                    case "trg":
                        // p0x p0y p0z   	p1x p1y p1z   	p2x p2y p2z  	 	mat_idx
                        Triangle t = new Triangle(nextVec3(), nextVec3(), nextVec3(), nextInt());
                        scene.triangles.add(t);
                        System.out.println(String.format("Parsed triangle (line %d)", lineNum));
                        break;
                    case "lgt":
                        // px	py	pz	r	g	b	spec	shadow	width
                        Light l = new Light(nextVec3(), nextColor(), nextDouble(), nextDouble(), nextDouble());
                        scene.lights.add(l);
                        System.out.println(String.format("Parsed light (line %d)", lineNum));
                        break;
                    default:
                        System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code,
                                lineNum));
                        break;
                }
            }
            
            // It is recommended that you check here that the scene is valid,
            // for example camera settings and all necessary materials were defined.
            
            System.out.println("Finished parsing scene file " + sceneFileName);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Failed to find file in working directory: " + System.getProperty("user.dir"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    private void renderScene(String outputFileName)
    {
        long startTime = System.currentTimeMillis();
        
        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[imageWidth * imageHeight * 3];
        
        
        // Put your ray tracing code here!
        //
        // Write pixel color values in RGB format to rgbData:
        // Pixel [r, g] red component is in rgbData[(g * imageWidth + r) * 3]
        //            green component is in rgbData[(g * imageWidth + r) * 3 + 1]
        //             blue component is in rgbData[(g * imageWidth + r) * 3 + 2]
        //
        // Each of the red, green and blue components should be a byte, i.e. 0-255
        
        
        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;
        
        // The time is measured for your own conveniece, rendering speed will not affect your score
        // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");
        
        // This is already implemented, and should work without adding any code.
        saveImage(imageWidth, rgbData, outputFileName);
        
        System.out.println("Saved file " + outputFileName);
        
    }
    
    
    //////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////
    
    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    private static void saveImage(int width, byte[] rgbData, String fileName)
    {
        try
        {
            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));
        }
        catch (IOException e)
        {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }
    }
    
    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    private static BufferedImage bytes2RGB(int width, byte[] buffer)
    {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);
        
        return result;
    }
    
    public static class RayTracerException extends Exception
    {
        RayTracerException(String msg)
        {
            super(msg);
        }
    }
}