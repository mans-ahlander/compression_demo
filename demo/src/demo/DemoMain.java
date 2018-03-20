package demo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;

public class DemoMain {
	public static final int width = 512;
	public static final int height = 512;
	public static final int x0 = 712;
	public static final int y0 = 288;
	
	public static void main(String [] args)
	{
		StreamerClient sc = null;
		try {
			sc = new StreamerClient("192.168.0.90", 1234, height, width, x0, y0);
			int[] imageData = sc.getImage();
			
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JFrame.setDefaultLookAndFeelDecorated(true);
			f.setResizable(false);
			UI ui = new UI(height, width);
			ui.setImageData(imageData);
			f.add(ui, BorderLayout.CENTER);
			f.pack();
			f.setVisible(true);
			
			Compressor c = new Compressor();
			
			int k = 0;
			while(k == 0) {
				imageData = sc.getImage();
				
				c.compress(imageData, 1000, height, width);

				System.out.println("Compression ratio: "+(1- ((double) c.getSize())/(((double) imageData.length)*12))*100);
				int[] imD = c.decoder(height, width, 1000);
				
//				for(int i=0;i <imD.length;i++) {
//					imD[i] = imD[i] >> 4;
//				}
				ui.setImageData(imD);
				f.revalidate();
				f.repaint();
			}
			
			sc.CloseStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
