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
}
