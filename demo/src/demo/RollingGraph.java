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

public class RollingGraph extends JPanel {
	static final int height = 512;
	static final int width = 512;
	
	float[] data = null;
	int size = 0;
	boolean mode = false;
	
	public RollingGraph(int size) {
		this.size = size;
		this.data = new float[size];
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
	}
	
	public void addValue(float val) {
		for(int i = 0; i < size-1; i++) {
			data[i] = data[i+1];
		}
		data[size-1] = (val + data[size-2] + data[size-3])/3;
	}
	
	public void clearData() {
		for(int i = 0; i < size; i++) {
			data[i] = 0;
		}
	}
	
	public void changeMode(boolean m) {
		this.mode = m;
		this.clearData();
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(mode == true) {
			
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
					g2d.drawLine(((width/size)*i), height-40-((int)(data[i]*512)), ((width/size)*(i+1)), height-40-((int)(data[i+1]*512)));
			}
		}
		else {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.BLACK);
			//DRAW AXES
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(0, 470, 512, 470);
			g2d.drawLine(40, 10, 40, 470);
			g2d.drawLine(35, 10, 45, 10);
			g2d.drawLine(35, 240, 45, 240);
			g2d.drawString("1000", 2, 15);
			g2d.drawString("500", 2, 245);
			g2d.drawString("750", 2, 135);
			g2d.drawString("275", 2, 360);
			
			Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	        g2d.setStroke(dashed);
	        g2d.drawLine(40, 10, 512, 10);
	        g2d.drawLine(40, 240, 512, 240);
	        
	        g2d.drawLine(40, 125, 512, 125);
	        g2d.drawLine(40, 355, 512, 355);
	        
	        int imageSize = 1920*1088*12; //height*width*12; // bits/frame
	        int orgBitRate = (int) (imageSize*30);
			
	        float defaultVal = orgBitRate;
	        g2d.setColor(Color.RED);
	        g2d.setStroke(new BasicStroke(2));
	        float h = 512*((defaultVal/1000000000f));
	        g2d.drawLine(0, 512-(int)h, width, 512-(int)h);
	        
	        g2d.setColor(Color.BLACK);
			//DRAW GRAPH
			for(int i = 0; i < size-1; i++) {
				if(data[i] != 0) {
					float bitRate = ((1-data[i])*imageSize*30);
					float ratio = bitRate/1000000000f;
					float bitRate2 = ((1-data[i+1])*imageSize*30);
					float ratio2 = bitRate2/1000000000f;
					g2d.drawLine(((width/size)*i), 512-((int)(512*ratio)), ((width/size)*(i+1)), 512-((int)(512*ratio2)));
				}
			}
		}
	}
}
