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
    private Camera camera;
    private static String grayscale_string = "█▓▒@ʘo•· ";
    private static boolean REVERSE_ASCII_BLACK_AND_WHITE = false;
    /**
     * Runs the ray tracer. Takes scene file, output image file and optional image size as input.
     */
    public static void main(String[] args)
    {
        if (REVERSE_ASCII_BLACK_AND_WHITE)
            grayscale_string = (new StringBuilder(grayscale_string)).reverse().toString();
        
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
    
            if (args.length >= 4)
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
    
    private class Parser
    {
        private String[] params;
        private int paramsIndex;
        
        public Parser(String[] params)
        {
            this.params = params;
            this.paramsIndex = 0;
        }
        
        private int aInt()
        {
            return Integer.parseInt(params[paramsIndex++]);
        }
        
        private double aDouble()
        {
            return Double.parseDouble(params[paramsIndex++]);
        }
        
        private Vec3 aVec3()
        {
            return new Vec3(aDouble(), aDouble(), aDouble());
        }
        
        private Color aColor()
        {
            return new Color(aDouble(), aDouble(), aDouble());
        }
        
        private boolean notDone()
        {
            return paramsIndex < params.length;
        }
    }
    
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
            System.out.println("Started parsing scene file:   " + sceneFileName);
            
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
                Parser parse = new Parser(params);
                
                switch (code)
                {
                    case "cam":
                        // px   	py   	pz 	lx  	ly  	lz 	ux  	uy  	uz 	sc_dist	sc_width
                        camera = new Camera(parse.aVec3(), parse.aVec3(), parse.aVec3(),
                                parse.aDouble(), parse.aDouble());
                        System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                        break;
                    case "set":
                        // bgr  	bgg  	bgb	sh_rays	rec_max SS
                        scene.backgroundColor = parse.aColor();
                        scene.shadowRayCount = parse.aInt();
                        scene.maximumRecursionCount = parse.aInt();
                        if (parse.notDone())
                            scene.superSamplingLevel = parse.aInt();
                        else
                            scene.superSamplingLevel = 2;
                        System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                        break;
                    case "mtl":
                        // dr    	dg    	db	sr   	sg   	sb 	rr   	rg  	rb	phong 	trans
                        Material m = new Material(parse.aColor(), parse.aColor(), parse.aColor(),
                                parse.aDouble(), parse.aDouble());
                        scene.materials.add(m);
                        System.out.println(String.format("Parsed material (line %d)", lineNum));
                        break;
                    case "sph":
                        // cx   	cy   	cz  	radius 	mat_idx
                        Sphere s = new Sphere(parse.aVec3(), parse.aDouble(), parse.aInt());
                        scene.shapes.add(s);
                        System.out.println(String.format("Parsed sphere (line %d)", lineNum));
                        break;
                    case "pln":
                        // nx	ny	nz	offset	mat_idx
                        Plane p = new Plane(parse.aVec3(), parse.aDouble(), parse.aInt());
                        scene.shapes.add(p);
                        System.out.println(String.format("Parsed plane (line %d)", lineNum));
                        break;
                    case "trg":
                        // p0x p0y p0z   	p1x p1y p1z   	p2x p2y p2z  	 	mat_idx
                        Triangle t = new Triangle(parse.aVec3(), parse.aVec3(), parse.aVec3(), parse.aInt());
                        scene.shapes.add(t);
                        System.out.println(String.format("Parsed triangle (line %d)", lineNum));
                        break;
                    case "lgt":
                        // px	py	pz	r	g	b	spec	shadow	width
                        Light l = new Light(parse.aVec3(), parse.aColor(), parse.aDouble(),
                                parse.aDouble(), parse.aDouble());
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
            if (camera == null || camera.screenWidth <= 0 || camera.screenDistance < 0)
                throw new RayTracerException("Bad camera input");
            int maxMaterialIndex = scene.materials.size() + 1;
            for (Shape shape : scene.shapes)
                if (shape.materialIndex <= 0 || shape.materialIndex >= maxMaterialIndex)
                    throw new RayTracerException("Invalid material for shape: " + shape);
            
            System.out.println("Finished parsing scene file:    " + sceneFileName);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Failed to find file in working directory:   " + System.getProperty("user.dir"));
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
        byte[] rgbData = raycastScene(camera, scene, imageWidth, imageHeight);
        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;
        
        // The time is measured for your own convenience, rendering speed will not affect your score
        // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + 0.001 * renderTime + " seconds.");
        
        // This is already implemented, and should work without adding any code.
        saveImage(imageWidth, rgbData, outputFileName);
        
        System.out.println("Saved file:    " + outputFileName);
    }
    
    private byte[] raycastScene(Camera camera, Scene scene, int pixelWidth, int pixelHeight)
    {
        boolean enableSuperSampling = false;
        
        double screenHeight = camera.screenWidth / pixelWidth * pixelHeight;
        double superSamplingFactor = 1.0 / scene.superSamplingLevel;
        byte[] rgbData = new byte[imageWidth * imageHeight * 3];
        Vec3 onePixelTowardsRight = camera.right.scaledBy(camera.screenWidth / pixelWidth);
        Vec3 onePixelTowardsUp = camera.up.scaledBy(screenHeight / pixelHeight);
        Vec3 currentScreenPoint = camera.position
                .plus(camera.forward.scaledBy(camera.screenDistance)) // forward by camera distance
                .plus(camera.right.scaledBy(-camera.screenWidth / 2)) // left by half screen
                .plus(camera.up.scaledBy(screenHeight / 2)); // up by half screen
    
        int ascii_print_width = 78;
        int ascii_x_skip = pixelWidth / ascii_print_width;
        int ascii_y_skip = pixelHeight / ascii_print_width * 2;
        
        long startTime = System.currentTimeMillis();
        //        long lastSecPrinted = 0;
        for (int y = 0; y < pixelHeight; y++)
        {
            for (int x = 0; x < pixelWidth; x++)
            {
                Color avgColor = new Color(0, 0, 0);
                if (enableSuperSampling)
                {
                    for (int xx = 0; xx < scene.superSamplingLevel; xx++)
                        for (int yy = 0; yy < scene.superSamplingLevel; yy++)
                        {
                            double randomUp = Math.random(), randomRight = Math.random();
                            Vec3 subPixelScreenPoint = currentScreenPoint
                                    .plus(onePixelTowardsRight.scaledBy((xx + randomRight) * superSamplingFactor))
                                    .plus(onePixelTowardsUp.scaledBy((yy + randomUp) * superSamplingFactor));
                            Vec3 rayDirection = (subPixelScreenPoint.minus(camera.position)).normalized();
                            Intersection intersection = scene.raycast(camera.position, rayDirection);
                            Color color = scene.getColor(intersection, 0);
                            avgColor = avgColor.plus(color.scaledBy(superSamplingFactor * superSamplingFactor));
                        }
                }
                else
                {
                    Vec3 rayDirection = (currentScreenPoint.minus(camera.position)).normalized();
                    Intersection intersection = scene.raycast(camera.position, rayDirection);
                    avgColor = scene.getColor(intersection, 0);
                }
                int pixelIndex = (y * imageHeight + x) * 3;
                rgbData[pixelIndex] = avgColor.getRed();
                rgbData[pixelIndex + 1] = avgColor.getGreen();
                rgbData[pixelIndex + 2] = avgColor.getBlue();
                currentScreenPoint = currentScreenPoint.plus(onePixelTowardsRight);
    
                if (x % ascii_x_skip == 0 && y % ascii_y_skip == 0)
                {
                    double grayscale = (avgColor.x + avgColor.y + avgColor.z) / 3;
                    System.out.print(grayscale_string.charAt((int) (grayscale * grayscale_string.length() * 0.99)));
                }
            }
            if (y % ascii_y_skip == 0)
                System.out.println();
            currentScreenPoint = currentScreenPoint.minus(onePixelTowardsRight.scaledBy(pixelWidth)); // â†� â†� â†�
            // â†� â†�
            currentScreenPoint = currentScreenPoint.minus(onePixelTowardsUp); // top to bottom
    
            //            long currentSec = (System.currentTimeMillis() - startTime) / 1000;
            //            if (currentSec != lastSecPrinted)
            //            {
            //                System.out.printf("%d%% complete...\n", 100 * y / pixelHeight);
            //                lastSecPrinted = currentSec;
            //            }
        }
        System.out.printf("Finished running in: %s\n", GetFormattedInterval(System.currentTimeMillis() - startTime));
        return rgbData;
    }
    
    private static String GetFormattedInterval(final long ms)
    {
        long millis = ms % 1000;
        long x = ms / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
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
