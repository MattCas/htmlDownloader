package htmlDownloader;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The Download Controller
 */
public class DownloadController {

	/** The web address from which filed will be downloaded. */
	public static String address = null;

	/** The destination folder where the files will be saved. */
	public static String dest = null;

	/** The parsed contents of the webpage in HTML. */
	public static Document doc;

	/** The file extensions specified by the user to be downloaded. */
	public static String extensions; 

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		retrieveLocation();
		retrieveExtensions();
		initializeUrl();
		download();
		//parse page
		//retrieve links
		//add links to job list
		//save to specified location

	}

	/**
	 * Anonymous class to create downloader threads
	 */

	private static Runnable download(final String dLink, final String fileName, final String destination){

		Runnable downloaderThread = new Runnable(){
			public void run(){
				try {
					//Open a URL Stream
					URL url = new URL(dLink);
					InputStream in = url.openStream();
					OutputStream out = new BufferedOutputStream(new FileOutputStream( dest + fileName));
					for (int b; (b = in.read()) != -1;) {
						out.write(b);
					}
					out.close();
					in.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		return downloaderThread;
	}

	/**
	 * Takes in the url and assigns it to the variable <code>address</code>
	 */
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
	}

	/**
	 * Parses webpage, filters out and downloads required files.
	 */
	public static void download(){
		//Create thread pool with 3 threads
		ExecutorService pool = Executors.newFixedThreadPool(3);
		//store the page as an HTML document 
		try {
			doc = Jsoup.connect(address).get();
			//Select all files to download
			Elements f2d = doc.select("a[href~=(?i)\\.(" + extensions + ")]");
			for (Element file: f2d){
				String fName = file.attr("href");
				String link = (address + fName);
				System.out.println(link);
				//create downloader threads for each file
				Runnable downObj = download(link, fName, dest);
				//add each file to the queue
				pool.execute(downObj);

			}
			System.out.println(f2d.size() + " file(s) to download");
		} catch (IOException e) {
			e.printStackTrace();
		}
		pool.shutdown();
	}

	/**
	 * Takes in the destination path and assigns it to the variable <code>dest</code>.
	 */
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

	/**
	 * Takes in the file extensions string and assigns it to the variable <code>extensions</code>.
	 */
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
