package demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class StreamerClient {
	static final int height = 1920;
	static final int width = 1088;
	static final int nbytes = height*width*(3/2);
	
	Socket socket = null;
	InputStream input = null;
	OutputStream output = null;
	
	public StreamerClient(String ip, int port) {
		ip = "192.168.0.90";
		port = 1234;
		
		//Try Connecting
	    try {
	           socket = new Socket(ip, port);
	           input = socket.getInputStream();
	           output = socket.getOutputStream();
	    } catch (UnknownHostException e) {
			System.out.println(e);
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
	    
	    //Initialize
	    
	    
	}
	
	public void CloseStream() throws IOException {
		socket.close();
	}
	
	public int[][] getImage() throws IOException {
		byte[] bytes = getBytes();
		
		//Convert to int matrix
		int[][] im = new int[height][width];
		
		
		return im;
	}
	
	private byte[] getBytes() throws IOException {
		byte[] buffer = new byte[nbytes];
		int read = 0;
		int result = 0;
		while ((read < nbytes) && (result != -1)) {
			result = input.read(buffer, read, 100-read);
			if (result!=-1)
				read = read+result;
		}
		return buffer;
	}
}
