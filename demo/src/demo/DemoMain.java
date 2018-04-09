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
			JFrame f2 = new JFrame();
			f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JFrame.setDefaultLookAndFeelDecorated(true);
			f2.setResizable(false);
			f2.setLocation(512, 0);
		
			RollingGraph rg = new RollingGraph(100);
			Controller controller = new Controller(c, rg);
			f2.add(controller, BorderLayout.NORTH);
			f2.add(rg, BorderLayout.CENTER);
			f2.pack();
			f2.setVisible(true);
			
			long startTime = System.currentTimeMillis();
			
			
			int k = 0;
			while(k < 100000) {
				
				imageData = sc.getImage();
				
				c.compress(imageData, height, width);

				double cr = 1 - ((double) c.getSize()) / (((double) imageData.length)*12);
				
				if(k%10 == 0 && k > 0) {
					long endTime = System.currentTimeMillis();
					double diff = (endTime - startTime)/10;
					startTime = System.currentTimeMillis();
					
					double fps = 1/diff * 1000;
					
					System.out.println("FPS: " + fps);
				}
				
				imageData = sc.getImage();
				
				c.compress(imageData, height, width);

				//System.out.println("Compression ratio: " + cr);
				//int[] imD = c.decoder(height, width, 1000);
				
				for(int i=0;i <imageData.length;i++) {
					imageData[i] = imageData[i] >> 4;
				}
				
				ui.setImageData(imageData);
				f.revalidate();
				f.repaint();
				
				rg.addValue((float)cr);

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
