package edge_detection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;  

import edge_detection.ClientThreadHandler;

@Controller
public class AppController {
	
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Ready....");
        return "index";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException{
    	int chunks = 4;
	
        if (!file.isEmpty()) {
            try {
            	
                byte[] imageInBytes = file.getBytes();
            	InputStream in = new ByteArrayInputStream(imageInBytes);
            	  
            	BufferedImage image = ImageIO.read(in);
            	
        		long startTime = System.currentTimeMillis();

        		BufferedImage[] buffImgs = new BufferedImage[chunks];
        		System.out.println("getting image data");
        		//Split the original image into subimages and save to tmp dir
        		EdgeDetectionImageUtil.chunk(image);
            	Thread[] t = new Thread[chunks];
            	ClientThreadHandler[] cth = new ClientThreadHandler[chunks];
            	
        		for (int i=0; i<chunks; i++) {
        			
        			BufferedImage img = ImageIO.read(new File(System.getProperty("user.dir") + "\\tmp\\img" + i + ".jpg"));
	        	     cth[i] = new ClientThreadHandler(img);
	        	     t[i] = new Thread(cth[i]);
	        	     t[i].start();
        		}
        		for (int j=0; j<chunks; j++) {
        			while (true) {
        				if (cth[j].getImage() == null) {
        					t[j].sleep(1000);
        				}
        				else {
        					buffImgs[j] = cth[j].getImage();
        					break;
        				}
        			}
	        	     
        		}
        		//Combine the images back into one image
        		EdgeDetectionImageUtil.knit(buffImgs);
        		long stopTime = System.currentTimeMillis();
        		long elapsedTime = stopTime - startTime;    
        		System.out.println(elapsedTime);
        		return "Edge detection complete in " + Long.toString(elapsedTime);
            } catch (Exception e) {
                return "Failed to upload => " + e.getMessage();
            }
        } else {
            return "Upload failed because the file was empty.";
        }
        
    }
 	
}
