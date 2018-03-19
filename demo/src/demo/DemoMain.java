package demo;

import java.io.IOException;

public class DemoMain {
	public static void main(String [] args)
	{
		try {
			StreamerClient sc = new StreamerClient("192.168.0.90", 1234);
			int[][] im = sc.getImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
