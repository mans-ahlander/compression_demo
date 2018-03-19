package demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class StreamerClient {
	int height = 1920;
	int width = 1088;
	int nbytes = 1920*1088*(3/2);
	
	Socket socket = null;
	InputStream input = null;
	OutputStream output = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	
	public StreamerClient() {
		
	}
	
	public StreamerClient(String ip, int port, int height, int width) {
		this.width = width;
		this.height = height;
		this.nbytes = height*width*(3/2);
		
		//Try Connecting
	    try {
	    		System.out.println("Trying to connect...");
	           	socket = new Socket(ip, port);
	           	input = socket.getInputStream();
	           	output = socket.getOutputStream();
	           	dos = new DataOutputStream(output);
	           	dis = new DataInputStream(input);
	           
	           	System.out.println("Connected!");
	           	System.out.println("Initilizing Stream");
	           	
	           	//Initialize
	           	dos.writeByte('w');
	           	dos.writeInt(width);
	           
	           	dos.writeByte('h');
	           	dos.writeInt(height);
	           
	           	dos.writeByte('x');
	           	dos.writeInt(0);
	           
	           	dos.writeByte('y');
	           	dos.writeInt(0);
	           
	           	dos.writeByte('c');
	           	dos.writeInt(0);
	           	
	           	System.out.println("Stream Initialized");
	           
	    } catch (UnknownHostException e) {
	    	System.out.println("Unknown Host");
			System.out.println(e);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Stream Error");
			System.out.println(e);
			System.exit(1);
		}
	    
	}
	
	public void CloseStream() throws IOException {
		//socket.close();
	}
	
	public int[] getImage() throws IOException {
		byte[] bytes = getBytes();
		
		int[] im = new int[height*width];

		int i = 0;
		int n = 0;
		byte b = 0;
		while(i < nbytes) {
			int val = bytes[i];
			if(b == 0) { //first byte
				im[n] = val << 4;
				b++;
			}
			else if(b == 1) { //second byte
				int val1 = val & 0b11110000;
				int val2 = val & 0b00001111;
				
				im[n] += val1 >> 4;
				n++;
				
				im[n] += val2 << 4;
				b++;
			}
			else {
				im[n] += val;
				n++;
				b = 0;
			}
			i++;
		}
		return im;
	}
	
	private byte[] getBytes() throws IOException {
		byte[] buffer = new byte[nbytes];
		dos.writeByte('S');
		dos.writeInt(255);
		
		int read = 0;
		int result = 0;
		while ((read < nbytes) && (result != -1)) {
			result = input.read(buffer, read, 100-read);
			if (result!=-1)
				read = read+result;
		}
		
		System.out.println("Expected " + nbytes + " bytes. Got " + read);
		
		return buffer;
	}
}
