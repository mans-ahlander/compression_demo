package demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

public class RollingGraphCompressionRatio extends RollingGraph {

	public RollingGraphCompressionRatio(int size) {
		super(size);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		//DRAW AXES
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(0, 470, 512, 470);
		g2d.drawLine(40, 10, 40, 470);
		g2d.drawLine(35, 10, 45, 10);
		g2d.drawLine(35, 240, 45, 240);
		g2d.drawString("100%", 2, 15);
		g2d.drawString("50%", 2, 245);
		g2d.drawString("75%", 2, 135);
		g2d.drawString("25%", 2, 360);
		
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(40, 10, 512, 10);
        g2d.drawLine(40, 240, 512, 240);
        
        g2d.drawLine(40, 125, 512, 125);
        g2d.drawLine(40, 355, 512, 355);
        
        
		//DRAW GRAPH
		g2d.setStroke(new BasicStroke(2));
		for(int i = 0; i < size-1; i++) {
			if(data[i] != 0)
				g2d.drawLine(((width/size)*i), height-40-data[i], ((width/size)*(i+1)), height-40-data[i+1]);
		}
	}
}
