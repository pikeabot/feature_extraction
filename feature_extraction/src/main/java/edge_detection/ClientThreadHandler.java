package edge_detection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.web.client.RestTemplate;


public class ClientThreadHandler implements Runnable{

	BufferedImage imgChunk;
	BufferedImage processedImg;
	
	public ClientThreadHandler(BufferedImage imgChunk)  {
		this.imgChunk = imgChunk;
	}
	@Override
    public void run() {
        try {
        	// Run edge detection
	        this.processedImg = EdgeDetection.runEdgeDetection(imgChunk);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
	
	public BufferedImage getImage() {
		return processedImg;
	}
}	

