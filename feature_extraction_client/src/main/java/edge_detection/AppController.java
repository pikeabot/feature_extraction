package edge_detection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import edge_detection.EdgeDetectionImageUtil.*;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;  

@Controller
public class AppController {
	
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Ready....");
        return "index";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException{
	     Socket soc;
	     soc=new Socket("localhost",4000);
        if (!file.isEmpty()) {
            try {
            	
                byte[] imageInBytes = file.getBytes();
            	InputStream in = new ByteArrayInputStream(imageInBytes);
            	  
            	BufferedImage image = ImageIO.read(in);
            	int chunks = 4;
        		long startTime = System.currentTimeMillis();

        		BufferedImage[] buffImgs = new BufferedImage[chunks];
        		System.out.println("getting image data");
        		//Split the original image into subimages and save to tmp dir
        		EdgeDetectionImageUtil.chunk(image);
        		for (int i=0; i<chunks; i++) {

	        	     BufferedImage img = null;
	        	     
	        	     System.out.println("Client is running. ");
					 System.out.println("Reading image from disk. ");
					 img = ImageIO.read(new File(System.getProperty("user.dir") + "\\tmp\\img" + i + ".jpg"));
					 ByteArrayOutputStream baos = new ByteArrayOutputStream();
					 
					 ImageIO.write(img, "jpg", baos);
					 baos.flush();
					 
					 byte[] bytes = baos.toByteArray();
					 baos.close();
					 
					 System.out.println("Sending image to server. ");
					 
					 OutputStream out = soc.getOutputStream(); 
					 DataOutputStream dos = new DataOutputStream(out);
					 
					 dos.writeInt(bytes.length);
					 dos.write(bytes, 0, bytes.length);
					 
					 System.out.println("Image sent to server. ");
				
				     dos.close();
				     out.close();  
        		}
        		//Combine the images back into one image
        		EdgeDetectionImageUtil.knit(buffImgs);
        		long stopTime = System.currentTimeMillis();
        		long elapsedTime = stopTime - startTime;    
        		System.out.println(elapsedTime);
        		return "Edge detection complete in " + Long.toString(elapsedTime);
            } catch (Exception e) {
            	soc.close();
                return "Failed to upload => " + e.getMessage();
            }
        } else {
        	soc.close();
            return "Upload failed because the file was empty.";
        }
        
    }
 	
}
