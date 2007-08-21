 /* 
  * Copyright (c) 2005 Sean Hamlin
  * All Rights Reserved.
  * 
  * http://revelation.atspace.biz
  * 
  * This source is provided free of charge
  * as long as you reference me, and my
  * website. Thanks
  * 
  * This source originally came from :
  * Jeff West, with John D. Mitchell
  * Java Tip 43: How to read 8- and 24-bit Microsoft Windows bitmaps in Java applications
  * http://www.javaworld.com/javaworld/javatips/jw-javatip43.html
  * Retrieved 1 June 2005
  */
 
package control;

import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 * The Load Bitmap Bytes class. The class is resposible for
 * extracting the colour bytes (RGB) from a bitmap in Revelation
 * @author Sean Hamlin
 * @version 1.0
 */
public class LoadBitmapBytes {
	
	private JProgressBar progress;
	private JFrame myFrame;
	private int nwidth;
	private int nheight;
	private int[] ndata;
	
	/**
	 * Constructor for LoadBitmapBytes
	 * @param progress a reference to the progress bar
	 * @param myFrame a reference to the main JFrame
	 */
	public LoadBitmapBytes(JProgressBar progress, JFrame myFrame) {
	  this.progress = progress;
	  this.myFrame = myFrame;
	}
	
	/**
	 * Method that reads the bitmap in
	 * @param fullPath the path to the bitmap
	 * @return the dat bytes from the image (in Alpha, Red, Green, Blue)
	 */
	public int[] loadbitmap (String fullPath)
	{
		try
		    {
		    FileInputStream fs=new FileInputStream(fullPath);
		    int bflen=14;  // 14 byte BITMAPFILEHEADER
			byte[] bf = new byte[bflen];
		    fs.read(bf,0,bflen);
		    int bilen=40; // 40-byte BITMAPINFOHEADER
			byte[] bi = new byte[bilen];
		    fs.read(bi,0,bilen);
	
		    // Interperet data.
		    int nsize = (((int)bf[5]&0xff)<<24)	| (((int)bf[4]&0xff)<<16)
		    		  | (((int)bf[3]&0xff)<<8) | (int)bf[2]&0xff;
		    //System.out.println("File type is :"+(char)bf[0]+(char)bf[1]);
		    //System.out.println("Size of file is :"+nsize);
	
		    int nbisize = (((int)bi[3]&0xff)<<24) | (((int)bi[2]&0xff)<<16)
		    			| (((int)bi[1]&0xff)<<8) | (int)bi[0]&0xff;
		    //System.out.println("Size of bitmapinfoheader is :"+nbisize);
	
		    nwidth = (((int)bi[7]&0xff)<<24) | (((int)bi[6]&0xff)<<16)
		    		   | (((int)bi[5]&0xff)<<8) | (int)bi[4]&0xff;
		    //System.out.println("Width is :"+nwidth);
	
		    nheight = (((int)bi[11]&0xff)<<24) | (((int)bi[10]&0xff)<<16)
		    		    | (((int)bi[9]&0xff)<<8) | (int)bi[8]&0xff;
		    //System.out.println("Height is :"+nheight);
	
		    int nplanes = (((int)bi[13]&0xff)<<8) | (int)bi[12]&0xff;
		    //System.out.println("Planes is :"+nplanes);
	
		    int nbitcount = (((int)bi[15]&0xff)<<8) | (int)bi[14]&0xff;
		    //System.out.println("BitCount is :"+nbitcount);
	
		    // Look for non-zero values to indicate compression
		    int ncompression = (((int)bi[19])<<24) | (((int)bi[18])<<16)
		    				 | (((int)bi[17])<<8) | (int)bi[16];
		    //System.out.println("Compression is :"+ncompression);
	
		    int nsizeimage = (((int)bi[23]&0xff)<<24) | (((int)bi[22]&0xff)<<16)
		    			   | (((int)bi[21]&0xff)<<8) | (int)bi[20]&0xff;
		    //System.out.println("SizeImage is :"+nsizeimage);
	
		    int nxpm = (((int)bi[27]&0xff)<<24)	| (((int)bi[26]&0xff)<<16)
		    		 | (((int)bi[25]&0xff)<<8) | (int)bi[24]&0xff;
		    //System.out.println("X-Pixels per meter is :"+nxpm);
	
		    int nypm = (((int)bi[31]&0xff)<<24)	| (((int)bi[30]&0xff)<<16)
		    		 | (((int)bi[29]&0xff)<<8) | (int)bi[28]&0xff;
		    //System.out.println("Y-Pixels per meter is :"+nypm);
	
		    int nclrused = (((int)bi[35]&0xff)<<24)	| (((int)bi[34]&0xff)<<16)
		    			 | (((int)bi[33]&0xff)<<8) | (int)bi[32]&0xff;
		    //System.out.println("Colors used are :"+nclrused);
	
		    int nclrimp = (((int)bi[39]&0xff)<<24) | (((int)bi[38]&0xff)<<16)
		    			| (((int)bi[37]&0xff)<<8) | (int)bi[36]&0xff;
		    //System.out.println("Colors important are :"+nclrimp);
			
			if (ncompression != 0) {
				JOptionPane.showMessageDialog(myFrame  , "Image must not have compression" +
						", aborting...", "Error", JOptionPane.ERROR_MESSAGE);
				progress.setString("Error - Image must not have compression");
				return null;
			}
		    if (nbitcount==24)
			{
				//No Palatte data for 24-bit format but scan lines are
				//padded out to even 4-byte boundaries.
				int npad = (nsizeimage / nheight) - nwidth * 3;
				if (npad == 4)   // <==== Bug correction
					npad = 0;     // <==== Bug correction
				ndata = new int [nheight * nwidth]; 
				byte brgb[] = new byte [( nwidth + npad) * 3 * nheight];
				
				progress.setValue(0);
				progress.setMaximum(nsizeimage);
				int increment = brgb.length / 100;
				int offset = 0;
				
				if (increment < 1) {
					increment = 1;
				}
				
		        int numRead = 0;											  //bytes.length-offset
		        while (offset < brgb.length && (numRead=fs.read(brgb, offset, increment)) >= 0) {
		            offset += numRead;
					//progress for reading the data file
					progress.setValue(offset);
					//System.out.println("offset : " + offset);
					if (offset + increment > brgb.length) {
						increment = brgb.length - offset;
					}
					//update the progress bar
					myFrame.update(myFrame.getGraphics());
		        }
				
					  //byte //offset //increment
				//fs.read (brgb, 0, (nwidth + npad) * 3 * nheight);
				int nindex = 0;
				
				
				for (int j = 0; j < nheight; j++)
			    {
					for (int i = 0; i < nwidth; i++)
					{
						ndata [nwidth * (nheight - j - 1) + i] =
					    (255&0xff)<<24 | (((int)brgb[nindex+2]&0xff)<<16)
					    | (((int)brgb[nindex+1]&0xff)<<8) | (int)brgb[nindex]&0xff;
						//System.out.println("Encoded Color at ("+i+","+j+")is: "+" (R,G,B)= ("+((int)(brgb[2]) & 0xff)+","+((int)brgb[1]&0xff)+","+((int)brgb[0]&0xff)+")");
						nindex += 3;
					}
					nindex += npad;
				}

				//print out the binary to the console 
				/*
				for (int i = 0 ; i < ndata.length ; i++) {
					StringBuffer buf = new StringBuffer( Integer.toBinaryString(ndata[i]) );
					// 4 bytes
					while (buf.length()<32)
					   buf.insert(0, '0');
					System.out.println("ndata[" + i + "] : " + buf);
				}*/
			}
			//if not a 24 bit bit bitmap
			else
			{
				fs.close();
				JOptionPane.showMessageDialog(myFrame  , "Not a 24-bit Windows Bitmap, aborting...", "Information", JOptionPane.ERROR_MESSAGE);
				progress.setString("Error - Not a 24-bit Bitmap Image");
				return null;
			}
	
		    fs.close();
		    return ndata;
		}
		catch (Exception e)
	    {
			JOptionPane.showMessageDialog(myFrame  , "An Error Occurred while Loading the Bitmap!" +
					"\nPlease choose the Image again", "Error", JOptionPane.ERROR_MESSAGE);
			progress.setString("An Error Occurred while Loading the Bitmap!");
			return null;
	    }
	}

	/**
	 * @return Returns the nheight.
	 */
	public int getNheight() {
		return nheight;
	}

	/**
	 * @param nheight The nheight to set.
	 */
	public void setNheight(int nheight) {
		this.nheight = nheight;
	}

	/**
	 * @return Returns the nwidth.
	 */
	public int getNwidth() {
		return nwidth;
	}

	/**
	 * @param nwidth The nwidth to set.
	 */
	public void setNwidth(int nwidth) {
		this.nwidth = nwidth;
	}
}
