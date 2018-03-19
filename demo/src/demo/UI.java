package demo;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class UI extends JPanel {

	int height = 1920;
	int width = 1088;
	
	int[] imageData = null;

	public UI(int height, int width) {
		this.height = height;
		this.width  = width;
		setPreferredSize(new Dimension(width, height));
  	}

	public void setImageData(int [] im) {
		this.imageData = im;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		final BufferedImage image;
		
		image = (BufferedImage) createImage(width, height);
		WritableRaster raster = image.getRaster();
		raster.setSamples(0, 0, width, height, 0, imageData);
		raster.setSamples(0, 0, width, height, 1, imageData);
		raster.setSamples(0, 0, width, height, 2, imageData);
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
  }
}
