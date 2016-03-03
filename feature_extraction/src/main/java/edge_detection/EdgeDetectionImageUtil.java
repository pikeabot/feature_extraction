package edge_detection;
import javax.imageio.ImageIO;  

import java.awt.image.BufferedImage;  
import java.io.*;  
import java.awt.*;  

public class EdgeDetectionImageUtil {
  
    private static int rows = 2;   //we assume the no. of rows and cols are known and each chunk has equal width and height  
    private static int cols = 2;  
    
	    public static void chunk(BufferedImage image) throws IOException {  
	  
	        int chunks = rows * cols;  
	  
	        int chunkWidth = image.getWidth() / cols; // determines the chunk width and height  
	        int chunkHeight = image.getHeight() / rows;  
	        int count = 0;  
	        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks  
	        for (int x = 0; x < rows; x++) {  
	            for (int y = 0; y < cols; y++) {  
	                //Initialize the image array with image chunks  
	                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());  
	  
	                // draws the image chunk  
	                Graphics2D gr = imgs[count++].createGraphics();  
	                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);  
	                gr.dispose();  
	            }  
	        }  
	        System.out.println("Splitting done");  
	  
	        //writing mini images into image files  
	        for (int i = 0; i < imgs.length; i++) {  
	            ImageIO.write(imgs[i], "jpg", new File("tmp\\img" + i + ".jpg"));  
	        }  
	        System.out.println("Mini images created");    
	}
	    
	
	public static void knit(BufferedImage[] buffImages) throws IOException {  
	    
        int chunkWidth, chunkHeight;  
        int type; 
        
        type = buffImages[0].getType();  
        chunkWidth = buffImages[0].getWidth();  
        chunkHeight = buffImages[0].getHeight();  
  
        //Initializing the final image  
        BufferedImage finalImg = new BufferedImage(chunkWidth*cols, chunkHeight*rows, type);  
  
        int num = 0;  
        for (int i = 0; i < rows; i++) {  
            for (int j = 0; j < cols; j++) {  
                finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);  
                num++;  
            }  
        }  
        System.out.println("Image concatenated.....");  
        ImageIO.write(finalImg, "jpeg", new File(System.getProperty("user.dir") +"\\tmp\\finalImg.jpg"));
	}
	
	public static BufferedImage byteToBufferedImage(byte[] imgBytes) throws IOException{
		InputStream in = new ByteArrayInputStream(imgBytes);
		BufferedImage bImageFromConvert = ImageIO.read(in);
		return bImageFromConvert;
	}
	
	public static byte[] bufferedImageToByte(BufferedImage originalImage) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( originalImage, "jpg", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}
}
