package demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Controller extends JPanel {
	private static final long serialVersionUID = 1L;

	Compressor c = null;
	RollingGraph currentGraph = null;
	RollingGraphBitrate gb = null;
	RollingGraphCompressionRatio gc = null;
	
	public Controller(Compressor c, int size) {
		this.gb = new RollingGraphBitrate(size);
		this.gc = new RollingGraphCompressionRatio(size);
		this.currentGraph = gc;
		
		GraphButton gbtn1 = new GraphButton(gc, gb);
		gbtn1.setText("BitRate(Mbit/s)");
		gbtn1.setSize(64, 32);;
		this.add(gbtn1);
		
		GraphButton gbtn2 = new GraphButton(gc, gc);
		gbtn2.setText("Compression Ratio(%)");
		gbtn2.setSize(64, 32);;
		this.add(gbtn2);
		
		this.c = c;
		setPreferredSize(new Dimension(512, 40));
		setBackground(new Color(0.9f, 0.9f, 0.9f));
		
		JLabel lbl1 = new JLabel();
		lbl1.setText("SQNR:");
		this.add(lbl1);
		
		JButton btn5 = new NoiseButton(1);
		btn5.setText("0dB");
		btn5.setSize(64, 32);;
		this.add(btn5);
		
		JButton btn1 = new NoiseButton(2);
		btn1.setText("3dB");
		btn1.setSize(64, 32);;
		this.add(btn1);
		
		JButton btn2 = new NoiseButton(10);
		btn2.setText("10dB");
		btn2.setSize(64, 32);;
		this.add(btn2);
		
		JButton btn3 = new NoiseButton(100);
		btn3.setText("20dB");
		btn3.setSize(64, 32);;
		this.add(btn3);
		
		JButton btn4 = new NoiseButton(1000);
		btn4.setText("30dB");
		btn4.setSize(64, 32);;
		this.add(btn4);

	}
	
	class GraphButton extends JButton {
		private static final long serialVersionUID = 1L;
		RollingGraph currentGraph = null;
		RollingGraph thisGraph = null;
		
		public GraphButton(RollingGraph cur, RollingGraph t) {
			this.currentGraph = cur;
			this.thisGraph = t;
			
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currentGraph = thisGraph;
				}
			});
		}
	}
	
	class NoiseButton extends JButton {
		private static final long serialVersionUID = 1L;
		int noiseLevel = 0;
		DemoMain demo = null;
		
		public NoiseButton(int level) {
			this.noiseLevel = level;
			
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					c.setSNR(level);
				}
			});
		}
	}
}
