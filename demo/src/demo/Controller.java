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
	
	public Controller(Compressor c, RollingGraph rg) {
		this.currentGraph = rg;
		this.c = c;
		setPreferredSize(new Dimension(512, 80));
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
		
		GraphButton gbtn1 = new GraphButton(false);
		gbtn1.setText("BitRate(Mbit/s)");
		gbtn1.setSize(64, 32);
		this.add(gbtn1);
		
		GraphButton gbtn2 = new GraphButton(true);
		gbtn2.setText("Compression Ratio");
		gbtn2.setSize(64, 32);
		this.add(gbtn2);
	}
	
	class GraphButton extends JButton {
		private static final long serialVersionUID = 1L;
		boolean mode;
		
		public GraphButton(boolean m) {
			this.mode = m;
			
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currentGraph.changeMode(mode);
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
