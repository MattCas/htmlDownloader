package htmlDownloader;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadController {
	public static String address = null;
	public static String dest = null;
	public static Document doc;
	public static String extensions = "jpg" ; // remember to separate them with

	public static void main(String[] args) {
		retrieveLocation();
		initializeUrl();
		//parse page
		//retrieve links
		//add links to job list
		//save to specified location

	}
	public static void initializeUrl(){
		//get URL from user
		address = "http://www.cs.bham.ac.uk/~dehghanh/vision_files/lab/lab4/"; // Temporary	
		//store the page as an HTML document 
		try {
			doc = Jsoup.connect(address).get();
			//Select all files to download
			Elements f2d = doc.select("a[href$=." + extensions + "]");
			for (Element file: f2d){
				String fName = file.attr("href");
				String link = (address + fName);
				System.out.println(link);
				//add each file to the queue
				
				 //Open a URL Stream
				URL url = new URL(link);
				InputStream in = url.openStream();
				OutputStream out = new BufferedOutputStream(new FileOutputStream( dest + fName));
				for (int b; (b = in.read()) != -1;) {
				out.write(b);
				}
				out.close();
				in.close();
				
			}
			System.out.println(f2d.size() + " file(s) to download");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void retrieveLocation(){
		//Get destination folder from user
		dest = "/Users/macbookpro/Desktop/Test/"; //For now

	}

}
