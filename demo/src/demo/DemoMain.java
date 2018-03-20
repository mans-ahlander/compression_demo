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
			
			JFrame f2 = new JFrame();
			f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JFrame.setDefaultLookAndFeelDecorated(true);
			f2.setResizable(false);
			f2.setLocation(512, 0);
			RollingGraph rg = new RollingGraph(100);
			f2.add(rg, BorderLayout.CENTER);
			f2.pack();
			f2.setVisible(true);
			
			int k = 0;
			while(k < 100000) {
				imageData = sc.getImage();
				
				ui.setImageData(imageData);
				f.revalidate();
				f.repaint();
				
				System.out.println("Adding");
				rg.addValue(k*10);

				f2.revalidate();
				f2.repaint();
				
				k++;
			}
			
			sc.CloseStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
