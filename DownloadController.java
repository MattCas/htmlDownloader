package htmlDownloader;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadController {
	public static String address = null;
	public static String dest = null;
	public static Document doc;
	public static String extensions; //= "pdf|jpe?g" ; // remember to separate them with |

	public static void main(String[] args) {
		retrieveLocation();
		retrieveExtensions();
		initializeUrl();
		//parse page
		//retrieve links
		//add links to job list
		//save to specified location

	}
	public static void initializeUrl(){
		//Retrieve URL from User using JTextField
		JTextField u = new JTextField(50);
		u.setText("http://www.cs.bham.ac.uk/~dehghanh/vision_files/lab/lab4/");
		int uAction = JOptionPane.showConfirmDialog(null, u, "Enter the URL", JOptionPane.OK_CANCEL_OPTION);
		if (uAction > 0){
			//Error if user cancelled
			JOptionPane.showMessageDialog(null, "User cancelled the operation 'Enter URL'", "Quitting", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		else{
			address = new String (u.getText());
		}

		//store the page as an HTML document 
		try {
			doc = Jsoup.connect(address).get();
			//Select all files to download
			Elements f2d = doc.select("a[href~=(?i)\\.(" + extensions + ")]");
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
		JTextField u = new JTextField(50);
		u.setText("/Users/macbookpro/Desktop/Test/");
		int uAction = JOptionPane.showConfirmDialog(null, u, "Enter the path where the files will be saved", JOptionPane.OK_CANCEL_OPTION);
		if (uAction > 0){
			//Error if user cancelled
			JOptionPane.showMessageDialog(null, "User cancelled the operation 'Enter path'", "Quitting", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		else{
			dest = new String (u.getText());
		}
	}

	public static void retrieveExtensions(){
		//Get file extensions for filtering from user
		JTextField u = new JTextField(50);
		u.setText("pdf,jpe?g");
		int uAction = JOptionPane.showConfirmDialog(null, u, "Enter file extensions separated only by a comma (,)", JOptionPane.OK_CANCEL_OPTION);
		if (uAction > 0){
			//Error if user cancelled
			JOptionPane.showMessageDialog(null, "User cancelled the operation 'Enter extensions'", "Quitting", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		else{
			extensions = new String (u.getText().replace(",","|" ));
		}
	}

}
