package demo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.lang.Math;

public class Compressor {
	List<Boolean> output=new ArrayList<Boolean>(Arrays.asList(new Boolean[0]));
	int k;
	int m;
	int[] intervals = {3, 7, 15, 32, 64, 131, 265, 531};
	int padding = 0;	
	
	public Compressor(){
		/*
		 * Constructor, initializes k and m
		 */
//		this.k = k;
//		m = Math.pow(2, k);;
	}
	
	private void setK(int mu) {
		/*
		 * Update k based on the mean, mu, calculated from past compressed symbols
		 */
		k = 1;
		int g = 0;
		int intervalLength = intervals.length;
		while(g<intervalLength && mu>intervals[g]) {
			g++;
			k++;
		}
	}
	
	private void getUnaryCode(int number) {
		/*
		 * Append unary code to output.
		 */
		for (int i = 0; i<number; i++) {
			output.add(true);
		}
		output.add(false);
		
	}
	
	public List<Boolean> compress(int[] im, int SNR, int height, int width) {

//	public int compress(int[] im, int SNR, int height, int width) {
		
		int M = 2048;				//half of max symbol value (max remapped value)
		k = 4;
		m = (int) Math.pow(2, k);
		
		output=new ArrayList<Boolean>(Arrays.asList(new Boolean[0]));
		
		int bits = 0;
		int mu;						// mean prediction error?
		int a;						//pixel above
		int b;						//pixel to left
		int n;						//iterative variable
		int x_hat;					// prediction of x
		int x;						// current pixel value
		int e;						// prediction error
		int newE;					// quantized e
		int diff;					// diff between newE and e
		int maxNoise;				// max allowed noise
		int r;						//rest
		int rtmp;
		int rBinLength;
		int q;						//integer division
		
		boolean sign;
		
		// start of compressing
		for(int j=0; j<height;j++) {
			mu = 0;
			for(int i=0; i<width;i++) {
				x = im[j*width+i];	//get pixel value
				
				/*
				 * PREDICTION MEAN2L
				 */
				
				//get value from left
				if (i <= 1) {
					b = 0;
				} else {
					b = im[j*width+i-2];
				}
				
				//get value from above
				if (j <= 1) {
					a = 0;
				} else {
					a = im[(j-2)*width+i];
					if (i<width-2) {
						a = a + im[(j-2)*width+i+2];
						a = a >> 1;
					}
				}
				
				//prediction
				if ((a != 0) && (b != 0)) {	
					x_hat = a+b;
					x_hat = x_hat >> 1;
				} else if (a!=0) {		// b = 0 predict with a
					x_hat = a;
				} else {				// a = 0 predict with b
					x_hat = b;
				}
				
				/*
				 * BIT REMOVAL CALCULATIONS
				 */
				bits = 0;
				maxNoise = (int) Math.floor(Math.sqrt(x_hat)/SNR);
				maxNoise++;
				while(maxNoise != 0) {
					maxNoise = maxNoise >> 1;
					bits++;
				}
				bits--;
				
				/*
				 * ERROR CALCULATION
				 */
				
				e = Math.abs(x-x_hat);
				newE = e >> bits;
				newE = newE << bits;
				diff = e - newE;
				
				if (x - x_hat > 0) {
					sign = false;
					im[j*width+i] = im[j*width+i] - diff;
				} else {
					sign = true;
					im[j*width+i] = im[j*width+i] + diff;
				}
				
				e = newE;
				
				//Remapp error
				if ((e>=M) && (!sign)) {			//pos values
					e = e - M;
					im[j*width+i] = im[j*width+i] - M;
				} else if ((e>=M) && (sign)) {		//neg values
					e = e - M;
					im[j*width+i] = im[j*width+i] + M;
				}
				
				mu = mu + e;
				e = e >> bits;
				
				/*
				 * ENCODING PART
				 */
				
				output.add(sign);

				q = (int) Math.floor(e/m);
				r = e%m;
				
				// add unary code to output.
				getUnaryCode(q);
				
				
				// get remainer code to output
				
				rBinLength = 0;
				rtmp = r;
				while(rtmp!=0) {
					rtmp = rtmp >> 1;
					rBinLength++;
				}
				
				
				for(n = 0; n < k-rBinLength; n++) {
					output.add(false);
				}
				
				n = 0;
				List<Boolean> tmpOutput=new ArrayList<Boolean>(Arrays.asList(new Boolean[rBinLength]));
				while(r!=0) {
					n++;
					tmpOutput.set(rBinLength-n, (r%2 != 0)); //flip binary code
					r = r >> 1;
				}
				//insert to output. //change size to rBinLength?!
				for (n = 0; n<tmpOutput.size(); n++) {
					output.add(tmpOutput.get(n));
				}
				
			}
			mu = (int) Math.floor(mu/width);
			setK(mu);
			m = (int) Math.pow(2, k);

		}
		
		return output;
	}
	
	public int[] decoder(int height, int width, int SNR){
		int[] im = new int[height*width];
		k=4;
		m = (int) Math.pow(2, k);
		
		int mu; 		//average error
		int a;			//value above
		int b;			//value left
		int x_hat;		//predicted value
		int bits;		//bits to remove
		int maxNoise;	//max allowed noise
		boolean sign;		//sign bit
		int e;			//error
		int q;			//unary counter
		int r;			//rest term
		int pop;		//pop variable
		int n;			//for loop iterator
		int c = 0;		// position in list counter
		
		boolean flag;
		for(int j=0; j<height; j++) {
			mu = 0;
			for(int i = 0; i<width; i++) {
				
				/*
				 * PREDICTION MEAN2L
				 */
				
				//get value from left
				if (i <= 1) {
					b = 0;
				} else {
					b = im[j*width+i-2];
				}
				
				//get value from above
				if (j <= 1) {
					a = 0;
				} else {
					a = im[(j-2)*width+i];
					if (i<width-2) {
						a = a + im[(j-2)*width+i+2];
						a = a >> 1;
					}
				}
				
				//prediction
				if ((a != 0) && (b != 0)) {	
					x_hat = a+b;
					x_hat = x_hat >> 1;
				} else if (a!=0) {		// b = 0 predict with a
					x_hat = a;
				} else {				// a = 0 predict with b
					x_hat = b;
				}
				/*
				 * BIT REMOVAL CALCULATIONS
				 */
				bits = 0;
				maxNoise = (int) Math.floor(Math.sqrt(x_hat)/SNR);
				maxNoise++;
				while(maxNoise != 0) {
					maxNoise = maxNoise >> 1;
					bits++;
				}
				bits--;
				
				/*
				 * DECODING PART
				 */
				
				sign = output.get(c);
				c++;
				
				q = -1;
				r = 0;
				
				//decode unary part
				flag = true;
				while(flag) {
					q++;
					flag = output.get(c);
					c++;
				}
				
				//decode remainder
				for(n = 0;n < k;n++) {
					if(output.get(c)) {
						pop = 1;
					} else {
						pop = 0;
					}
					c++;
					r=r+(int) (pop*Math.pow(2, (k-n-1)));
				}
				
				//get error
				e = q*m+r;
				e = e << bits;
				
				//increase mean error of row
				mu = mu + e;
				
				if(sign){
					im[j*width+i] = x_hat - e;
				} else {
					im[j*width+i] = x_hat + e;
				}
				
				
			}
			mu = (int) Math.floor(mu/width);
			setK(mu);
			m = (int) Math.pow(2, k);
		}
		
		
		return im;
	}
	
	public int getSize() {
		return output.size();
	}


}
