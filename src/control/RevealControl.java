 /* 
  * Copyright (c) 2005 Sean Hamlin
  * All Rights Reserved.
  * 
  * http://revelation.atspace.biz
  * 
  * This source is provided free of charge
  * as long as you reference me, and my
  * website. Thanks
  */
 
package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import security.PRNG;
import security.pseudoRandom;

/**
 * The Reveal Control class. The class is resposible for
 * the whole decoding process in Revelation
 * @author Sean Hamlin
 * @version 1.0
 */
public class RevealControl {
	
	JProgressBar progress;
	JFrame myFrame;
	
	LoadBitmapBytes loadBitmapBytes;
	
	byte[] dataBytes = null;
	int[] ndata = null;
	
	private PRNG pseudoRandomNumber;
	
	File stegoImageFileName;
	File dataFile;
	String dataFileName;

	//private static final int ONEBITPERBYTE = 1;
	private static final int TWOBITSPERBYTE = 2;
	//private static final int THREEBITSPERBYTE = 3;
	//private static final int FOURBITSPERBYTE = 4;
	//private static final int ADAPTIVE = 5;
	
	private String password = "password"; //default password
	
	/**
	 * Default constructor
	 */
	public RevealControl() {		
	}
	
	/**
	 * The main method which controls the entire decoding process
	 * @return whether the entire decoding process was successful or not
	 */
	public boolean control() {
		
		//++++++++++ 1 read in Stego Image ++++++++++
		progress.setStringPainted(true);
		progress.setString("Reading in the Stego Image ...");
		ndata = loadbitmap(stegoImageFileName.getAbsolutePath());
		
		if (ndata == null) 				//make sure reading the cover image was successful
			return false;
		
		//++++++++++ 2 Decode Data from Stego Image ++++++++++
		progress.setString("Decoding the Data File out of the Stego Image ...");
		boolean okay = decodeStegoImage();
		
		if (!okay) {
			progress.setString("Error - This is not a Stego Image !!");
			return false;
		}
		
		//++++++++++ 3 save the Data File ++++++++++
		progress.setString("Saving Data File now ...");
		okay = saveTheDataFile();
		
		if (!okay) 
			return false;
		
		progress.setString("Finished !!");
		
		return true;
	}
	
	/**
	 * Writes the dataBytes to the directory which the
	 * user selects. Uses the default name that was encoded
	 * @return whether successful or not
	 */
	private boolean saveTheDataFile() {
		FileOutputStream fout;
		StringBuffer fullPath = new StringBuffer("");
		
		progress.setValue(0);
		progress.setMaximum(dataBytes.length - 1);		//set to the number of pixels
		int increment = (dataBytes.length) / 100;	//set increment of the JProgressBar
		
		if (increment < 1 ) {	//make sure the increment is within range
			increment = 1;
		}
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//int result = fileChooser.showSaveDialog(null);
		int result = fileChooser.showDialog(myFrame, "Set Destination Directory");
		
		if (result == JFileChooser.CANCEL_OPTION) {
			return false;
		}

		File outputDir = fileChooser.getSelectedFile();
		fullPath.append(outputDir.getAbsolutePath());
		fullPath.append(System.getProperty("file.separator"));
		fullPath.append(dataFileName);
		dataFile = new File(fullPath.toString());
		
		//check to see if the Data File already exists
		if (dataFile.exists()) {
			int response = JOptionPane.showConfirmDialog (myFrame, "Overwrite existing file?", "Confirm Overwrite",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.CANCEL_OPTION) {
				progress.setString("File not to be overwritten - Aborted");
				return false;
			}
		}
		
		try {
			fout = new FileOutputStream(dataFile);
		
		} catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(myFrame  , "Error Opening Output Directory" +
					"\nPlease make sure it exists and then select again", "Error", JOptionPane.ERROR_MESSAGE);
			progress.setString("Error Opening Output Directory");
			return false;
		}
		try {

			// Write the vals array to the file.
			for(int i = 0; i < dataBytes.length; i++) {
				fout.write(dataBytes[i]);
				if (i % increment == 0) {
					progress.setValue(i);
					myFrame.update(myFrame.getGraphics());
				}
			}

		} catch(IOException e) {
			JOptionPane.showMessageDialog(myFrame  , "Error Writing File" +
					"\nPlease make sure you have write access to the destination drive\n" +
					"and then try again", "Error", JOptionPane.ERROR_MESSAGE);
			progress.setString("Error Writing File");
			return false;
		}

		try {
			fout.close(); 
		} catch(IOException e) {
			System.out.println("Error Closing File");
			JOptionPane.showMessageDialog(myFrame  , "Error Closing File" +
					"\nPlease make sure the file has not been tampered with", "Error", JOptionPane.ERROR_MESSAGE);
			progress.setString("Error Closing File");
			return false;
		}

		return true;
	}

	/**
	 * Reads the data bytes from a bitmap
	 * @param fullPath the path to the bitmap
	 * @return the data bytes
	 */
	public int[] loadbitmap (String fullPath)
	{
		int[] temp = null;
		loadBitmapBytes = new LoadBitmapBytes(progress, myFrame);
		temp = loadBitmapBytes.loadbitmap(fullPath);
		return temp;
	}
	
	/**
	 * The main method which decodes the read in bytes from the
	 * image. Calculates the method, the filename, and the data bytes
	 */
	public boolean decodeStegoImage() {
		int method;
		int fileLength;
		int dataLength;
		byte[] fileName;
		
		//set up the unique PRNG
		pseudoRandomNumber = new pseudoRandom();
		pseudoRandomNumber.setPassword(password);
		pseudoRandomNumber.setLength(ndata.length);
		pseudoRandomNumber.init();
		
		System.out.println("ndata.length = " + ndata.length);
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now decode the method out of the image.						** Pixel 0
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			
		//methodInBinary is 6 bits long
		String methodInBinary = decodePixel(pseudoRandomNumber.getNextPsuedo(), TWOBITSPERBYTE);
		method = Integer.parseInt(methodInBinary,2);
		System.out.println("method : " + method);
		
		//make sure the image is a Stego Image
		if (method > 4) {
			JOptionPane.showMessageDialog(myFrame  , "This is not a Stego Image !!" +
					"\nGo back and select again", "Information", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now decode the length of the filename out of the image.		** Pixel 1
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				
		//fileLengthInBinary is 6 bits long
		String fileLengthInBinary = decodePixel(pseudoRandomNumber.getNextPsuedo(), TWOBITSPERBYTE);
		fileLength = Integer.parseInt(fileLengthInBinary,2);
		System.out.println("fileLength : " + fileLength);
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now decode the length of the Data File out of the image.	** Pixel 2 - 6
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		StringBuffer dataLengthString = new StringBuffer("");
		//5 pixels
		for (int i = 2 ; i < 7 ; i++) {
			int test = pseudoRandomNumber.getNextPsuedo();
			System.out.println("i= " + i + ", pixel= " + test);
			dataLengthString.append(decodePixel( test, TWOBITSPERBYTE ));
			System.out.println("dataLengthString = " + dataLengthString);
		}
		dataLength = Integer.parseInt(dataLengthString.toString(),2);
		System.out.println("dataLength : " + dataLength);
		
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		int bitsPerPixel = method * 3; 		//3 bytes per pixel
		
		int numberOfPixelsOfFile = (fileLength * 8) / bitsPerPixel; 	//3 bytes per pixel
		int numberOfPixelsOfData = (dataLength * 8) / bitsPerPixel; 	//3 bytes per pixel
		
		System.out.println("numberOfPixelsOfFile: " + numberOfPixelsOfFile);
		System.out.println("numberOfPixelsOfData: " + numberOfPixelsOfData);
		
		fileName = new byte[fileLength];
		dataBytes = new byte[dataLength];
		
		progress.setValue(0);
		progress.setMaximum(7 + numberOfPixelsOfFile + numberOfPixelsOfData);		//set to the number of pixels
		int increment = (7 + numberOfPixelsOfFile + numberOfPixelsOfData) / 100;	//set increment of the JProgressBar
		
		if (increment < 1 ) {	//make sure the increment is within range
			increment = 1;
		}
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now decode the filename out of the image.					** Pixel 7 - 7+numberOfPixelsOfFile
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		StringBuffer fileNameBuffer = new StringBuffer("");
		StringBuffer charFileNameBuffer = new StringBuffer("");
		int index = 0;

		//go through all pixels
		for (int i = 7 ; i <= 7 + numberOfPixelsOfFile ; i ++) {
			
			//read pixel
			fileNameBuffer.append(decodePixel( pseudoRandomNumber.getNextPsuedo(), method ));
			
			//if you have more than 8 bits, then save one byte
			while (fileNameBuffer.length() >= 8) {
				if (index == fileName.length) 
					break;
				//System.out.println("fileNameBuffer : " + fileNameBuffer);
				int temp = Integer.parseInt(fileNameBuffer.substring(0, 8),2);
				fileName[index] = (byte) temp;
				
				charFileNameBuffer.append((char)fileName[index]);
				
				//System.out.println("fileName[" + index + "] : " + fileName[index]);
				fileNameBuffer.delete(0, 8);
				index++;
			}
			if (i % increment == 0) {
				progress.setValue(i);
				myFrame.update(myFrame.getGraphics());
			}
		}
		dataFileName = charFileNameBuffer.toString();
		System.out.println("charFileNameBuffer : " + charFileNameBuffer);

		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now decode the data bytes out of the image.					** Pixel 7+numberOfPixelsOfFile+1 - 7+numberOfPixelsOfFile+1+numberOfPixelsOfData
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		StringBuffer dataFileBuffer = new StringBuffer("");
		index = 0;
		
		//go through all pixels
		for (int i = 7 + numberOfPixelsOfFile + 1; i <= 7 + numberOfPixelsOfFile + 1 + numberOfPixelsOfData ; i ++) {
			
			//read pixel
			dataFileBuffer.append(decodePixel( pseudoRandomNumber.getNextPsuedo(), method ));
			
			//if you have more than 8 bits, then save one byte
			while (dataFileBuffer.length() >= 8) {
				if (index == dataBytes.length) 
					break;
				//System.out.println("dataFileBuffer : " + dataFileBuffer);
				int temp = Integer.parseInt(dataFileBuffer.substring(0, 8),2);
				dataBytes[index] = (byte) (temp & 0xFF);
				//System.out.println("dataBytes[" + index + "] : " + dataFileBuffer.substring(0, 8));
				dataFileBuffer.delete(0, 8);
				index++;
			}
			if (i % increment == 0) {
				progress.setValue(i);
				myFrame.update(myFrame.getGraphics());
			}
		}
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//							++ END ++
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		
		//debug ndata.length
		/*for (int i = 0 ; i < ndata.length ; i++) {
			StringBuffer buf = new StringBuffer( Integer.toBinaryString(ndata[i]) );
			// 4 bytes
			while (buf.length()<32)
			   buf.insert(0, '0');
			System.out.println("ndata[" + i + "] : " + buf);
		}*/
		return true;
	}
	
	/**
	 * Decodes a particular pixel, given the steganographic method used
	 * @param i the pixel to decode
	 * @param method the number of LSB's to decode
	 * @return the decoded string in binary
	 */
	private String decodePixel(int i, int method) {
		int firstOffset = 16 - method;
		int secondOffset = 24 - method;
		int thirdOffset = 32 - method;
		StringBuffer temp = new StringBuffer( Integer.toBinaryString(ndata[i]) );
		String decoded = temp.substring(firstOffset, 16) + temp.substring(secondOffset, 24) +
							temp.substring(thirdOffset, 32);
		//System.out.println("decoded ["+i+"] : " + decoded);
		return decoded;
	}

	/**
	 * @return Returns the stegoImageFileName.
	 */
	public File getStegoImageFileName() {
		return stegoImageFileName;
	}

	/**
	 * @param stegoImageFileName The stegoImageFileName to set.
	 */
	public void setStegoImageFileName(File stegoImageFileName) {
		this.stegoImageFileName = stegoImageFileName;
	}

	/**
	 * @return Returns the myFrame.
	 */
	public JFrame getMyFrame() {
		return myFrame;
	}

	/**
	 * @param myFrame The myFrame to set.
	 */
	public void setMyFrame(JFrame myFrame) {
		this.myFrame = myFrame;
	}

	/**
	 * @return Returns the progress.
	 */
	public JProgressBar getProgress() {
		return progress;
	}

	/**
	 * @param progress The progress to set.
	 */
	public void setProgress(JProgressBar progress) {
		this.progress = progress;
	}
}
