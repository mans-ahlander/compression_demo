package demo;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;

public class DemoMain {
	public static final int width = 1920;
	public static final int height = 1088;
	
	public static void main(String [] args)
	{
		StreamerClient sc = null;
		try {
			//sc = new StreamerClient();
			sc = new StreamerClient("192.168.0.90", 1234, height, width);
			int[] imageData = sc.getImage();
			
			/*
			int[] testim = new int[height*width];
			for(int j = 0; j<height*width; j++) {
					testim[j] = 0;
			}
			
			int[] testim2 = new int[height*width];
			for(int j = 0; j<height*width; j++) {
					testim2[j] = 150;
			}*/
			
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JFrame.setDefaultLookAndFeelDecorated(true);
			f.setResizable(false);
			UI ui = new UI(height, width);
			ui.setImageData(imageData);
			f.add(ui, BorderLayout.CENTER);
			f.pack();
			f.setVisible(true);
			
			int k = 0;
			while(k == 0) {
				imageData = sc.getImage();
				ui.setImageData(imageData);
				f.revalidate();
				f.repaint();
			}
			
			//int[] im = sc.getImage();
			
			sc.CloseStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
