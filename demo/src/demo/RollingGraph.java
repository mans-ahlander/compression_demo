package demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

public class RollingGraph extends JPanel {
	static final int height = 512;
	static final int width = 512;
	
	int[] data = null;
	int size = 0;
	
	public RollingGraph(int size) {
		this.size = size;
		this.data = new int[size];
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
	}
	
	public void addValue(int val) {
		for(int i = 0; i < size-1; i++) {
			data[i] = data[i+1];
		}
		data[size-1] = val;
	}
	
	public void clearData() {
		for(int i = 0; i < size; i++) {
			data[i] = 0;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		for(int i = 0; i < size-1; i++) {
			g.setColor(Color.BLACK);
			g2d.drawLine(((width/size)*i), height-data[i], ((width/size)*(i+1)), height-data[i+1]);
			
			//g.setColor(Color.RED);
			//g2d.drawOval(((width/size)*i) , height-data[i], 6, 6);
		}
	}
}
