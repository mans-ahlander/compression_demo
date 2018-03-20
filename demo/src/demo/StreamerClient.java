package demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
	
	public void writeCmd(char cmd, int val) throws IOException {
		dos.writeByte(cmd);
		dos.writeInt(Integer.reverseBytes(val));
       	dos.flush();
	}
	
	public StreamerClient(String ip, int port, int height, int width, int x0, int y0) {
		this.width = width;
		this.height = height;
		this.nbytes = (height*width*3)/2;
		
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
	           	writeCmd('w', width);
	           	writeCmd('h', height);
	           	writeCmd('x', x0);
	           	writeCmd('y', y0);
	           	writeCmd('c', 0);
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
		socket.close();
	}
	
	public int[] getImage() throws IOException {
		byte[] bytes = getBytes();
		
		int[] im = new int[height*width];

		int i = 0;
		int n = 0;
		int b = 0;
		while(i < nbytes) {
			int val = Byte.toUnsignedInt(bytes[i]);
			
			if(b == 0) { //first byte
				im[n] = val;
				b++;
			}
			else if(b == 1) { //second byte
				int val1 = val & 0x0f;
				int val2 = val & 0xf0;
				
				im[n] += val1 << 8;
				im[n] = im[n] >> 4; //SCALE TO 8 BITS
			
				n++;
				
				im[n] = val2 >> 8;
				b++;
			}
			else { //third byte
				im[n] += val << 4;
				im[n] = im[n] >> 4; //SCALE TO 8 BITS
				
				n++;
				b = 0;
			}
			i++;
		}

		return im;
	}
	
	private byte[] getBytes() throws IOException {
		byte[] buffer = new byte[nbytes];
		writeCmd('$', 255);
		
		int read = 0;
		int result = 0;
		while ((read < nbytes) && (result != -1)) {
			result = input.read(buffer, read, nbytes-read);
			if (result!=-1)
				read = read+result;
		}
	
		return buffer;
	}
}
