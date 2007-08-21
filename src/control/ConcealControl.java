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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import security.PRNG;
import security.pseudoRandom;

/**
 * The Conceal Control class. The class is responsible for
 * the whole encoding process in Revelation
 * @author Sean Hamlin
 * @version 1.1
 * @since 2.0
 */
public class ConcealControl {
	private File dataFileName;
	private File coverFileName;
	private FileInputStream input;
	private FileOutputStream output;
	
	private JProgressBar progress;
	private JFrame myFrame;
	private BmpWriter newBMPFile;
	private LoadBitmapBytes loadBitmapBytes;
	
	private Image stegoImage;
	
	private PRNG pseudoRandomNumber;
	
	private int nwidth;
	private int nheight;
	
	private byte[] dataBytes = null;
	private int[] ndata = null;
	private byte[] fileName = null;
	private byte[] stegoMethod = null;
	
	private static final int ONEBITPERBYTE = 1;
	private static final int TWOBITSPERBYTE = 2;
	private static final int THREEBITSPERBYTE = 3;
	private static final int FOURBITSPERBYTE = 4;
	private static final int ADAPTIVE = 5;
	
	private String password = "password"; //default password
	
	/**
	 * Default constructor
	 */
	public ConcealControl()	{	
	}
	
	/**
	 * The main method which controls the entire encoding process
	 * @return whether the entire encoding process was successful or not
	 */
	public boolean control() {
		
		//++++++++++ 1 read in data ++++++++++ 
		try {
			progress.setStringPainted(true);
			progress.setString("Reading in Data File ...");
			dataBytes = getBytesFromFile(dataFileName);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (dataBytes == null)	//make sure reading the data was successful
			return false;
		
		//++++++++++ 2 read in image ++++++++++
		progress.setString("Reading in the Cover Image ...");
		ndata = loadbitmap(coverFileName.getAbsolutePath());
		
		if (ndata == null) 				//make sure reading the cover image was successful
			return false;

		//++++++++++ 3 encode data in image ++++++++++
		progress.setString("Encoding the Data File in the Cover Image ...");
		int intStegoMethod = determineStegoMethod();
		
		if ( intStegoMethod < 0) 
			return false;

		boolean okay = encodeStegoImage(intStegoMethod);
		
		if (!okay) 				//make sure reading the cover image was successful
			return false;
		
		//++++++++++ 4 create the image ++++++++++
		stegoImage = createImage();
		
		if (stegoImage == null)
			return false;
		
		//++++++++++ 5 save the image ++++++++++
		progress.setString("Saving Bitmap now ...");
		okay = saveTheImage();
		
		if (!okay) 				//make sure reading the cover image was successful
			return false;
		
		progress.setString("Finished !!");
		
		return true;
	}
	
	/**
	 * Determines the least number of LSB's to change
	 * in each byte of each pixel to encode the data fully.
	 * @return the number of LSB's to change
	 */
	private int determineStegoMethod() {
		fileName = encodeFileName(); 		//firstly encode the filename into ASCII bytes
		int fileLength = fileName.length;	//length of File Name
		int dataLength = dataBytes.length;	//length of data File
		int bitsPerPixel = 0;
		int bitsPerByte = 0;
		//now work out the least number of bits per pixel required to hide the data
		for (bitsPerByte = 1 ; bitsPerByte <= 4 ; bitsPerByte++) {
			bitsPerPixel = bitsPerByte * 3;
			int numberOfPixelsOfFile = (fileLength * 8) / bitsPerPixel; 		//3 bytes per pixel
			int numberOfPixelsOfData = (dataLength * 8) / bitsPerPixel; 	//3 bytes per pixel
			//System.out.print("when B.P.Byte = " + bitsPerByte + "  numberOfPixelsOfFile = " + numberOfPixelsOfFile);
			//System.out.println("  numberOfPixelsOfData = " + numberOfPixelsOfData + " ** ndata = " + ndata.length);
			if ((7 + numberOfPixelsOfFile + numberOfPixelsOfData) < ndata.length) {
				//System.out.println("B.P.Byte = " + bitsPerByte);
				return bitsPerByte;
			}
		}
		
		JOptionPane.showMessageDialog(myFrame  , "Cover image is too small to fit all that data !!" +
				"\nGo back and select again", "Information", JOptionPane.ERROR_MESSAGE);
		progress.setString("Error - Cover image is too small to fit all that data !!");
		
		return -1;
	}

	/**
	 * Creates the physical BMP File 
	 * @return whether the writing was successful or not
	 */
	private boolean saveTheImage() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File outputFile = fileChooser.getSelectedFile();
			//check to make sure user wants to overwrite the file
			if (outputFile.exists()) {
				int response = JOptionPane.showConfirmDialog (myFrame, "Overwrite existing file?", "Confirm Overwrite",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION) 
					return false;
			}
			//check for a .bmp extension, if there is not one, then add it
			String fileName = outputFile.getName();
			String fullPath = outputFile.getParent();
			if (!fileName.toLowerCase().endsWith(".bmp")) {
				StringBuffer newFileName = new StringBuffer(fileName);
				newFileName.append(".bmp");
				outputFile = new File(fullPath + System.getProperty("file.separator") + newFileName.toString());
			}
			//System.out.println("outputFile = " + outputFile);
			
			newBMPFile = new BmpWriter(progress, myFrame); 
			newBMPFile.saveBitmap(outputFile.getAbsolutePath(), stegoImage, nwidth, nheight);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Creates an image from the 4 byte array (alpha, Red, Green, Blue)
	 * @return the newly created image
	 */
	private Image createImage() {
		Image image = null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.createImage( new MemoryImageSource (nwidth, nheight,ndata, 0, nwidth));
		return image;
	}

	/**
	 * Reads all the bytes from the selected data file
	 * @param file the Data File to read in
	 * @return the read in Byte Array
	 * @throws IOException if all the data is not read
	 */
    public byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
		//ensure file is less than 4 GB's
        if (length > Integer.MAX_VALUE) {
			return null;
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
		int increment = bytes.length / 100; 	//100 increments
		progress.setMaximum(bytes.length);
		
		if (increment < 1) {
			increment = 1;
		}

        int numRead = 0;											  //bytes.length-offset
        while (offset < bytes.length && (numRead=is.read(bytes, offset, increment)) >= 0) {
            offset += numRead;
			//progress for reading the data file
			progress.setValue(offset);
			if (offset + increment > bytes.length) {
				increment = bytes.length - offset;
			}
			//update the progress bar
			myFrame.update(myFrame.getGraphics());
        }
		
		//DEBUG  - print out the binary to the console
		
		//for (int i = 0 ; i < bytes.length ; i++) {
			//System.out.println("ndata[" + i + "] : " + ndata[i]);

		//	StringBuffer buf = new StringBuffer( Integer.toBinaryString(bytes[i]) );
			// pad to 8 bits
		//	while (buf.length()<8)
		//	   buf.insert(0, '0');
		//	System.out.println("bytes[" + i + "] : " + buf);
		//}
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        is.close();
        return bytes;
    }

	/**
	 * Reads the data bytes from a bitmap
	 * @param fullPath
	 * @return the data bytes of the cover bitmap
	 */
	private int[] loadbitmap (String fullPath)
	{
		int[] temp = null;
		loadBitmapBytes = new LoadBitmapBytes(progress, myFrame);
		temp = loadBitmapBytes.loadbitmap(fullPath);
		if (temp != null) {
			nheight = loadBitmapBytes.getNheight();
			nwidth = loadBitmapBytes.getNwidth();
		}
		
		//debug
		/*System.out.println(" before encoding ");
		for (int i = 0 ; i < temp.length ; i++) {
			StringBuffer buf = new StringBuffer( Integer.toBinaryString(temp[i]) );
			// 4 bytes
			while (buf.length()<32)
			   buf.insert(0, '0');
			System.out.println("temp[" + i + "] : " + buf);
		}*/
		
		return temp;
	}
	
	/**
	 * Does the physical encoding process
	 * @param method the Steganographic method to be used
	 */
	private boolean encodeStegoImage(int method) {
		
		int fileLength = fileName.length;	//length of File Name
		int dataLength = dataBytes.length;	//length of data File
		
		//set up the unique PRNG
		pseudoRandomNumber = new pseudoRandom();
		pseudoRandomNumber.setPassword(password);
		pseudoRandomNumber.setLength(ndata.length);
		pseudoRandomNumber.init();
		
		stegoMethod = encodeMethod(method);	//then encode the method into ASCII
		int bitsPerPixel = method * 3; 		//3 bytes per pixel
		
		int numberOfPixelsOfFile = (fileLength * 8) / bitsPerPixel; 		//3 bytes per pixel
		int numberOfPixelsOfData = (dataLength * 8) / bitsPerPixel; 	//3 bytes per pixel
		
		progress.setValue(0);
		progress.setMaximum(7 + numberOfPixelsOfFile + numberOfPixelsOfData);		//set to the number of pixels
		int increment = (7 + numberOfPixelsOfFile + numberOfPixelsOfData) / 100;	//set increment of the JProgressBar
		
		if (increment < 1 ) {	//make sure the increment is within range
			increment = 1;
		}
		
		//DEBUG
		//System.out.println("method = " + method);
		//System.out.println("bitsPerPixel = " + bitsPerPixel);
		//System.out.println("numberOfPixelsOfFile = " + numberOfPixelsOfFile);
		//System.out.println("numberOfPixelsOfData = " + numberOfPixelsOfData);
		//System.out.println("ndata.length = " + ndata.length);
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now encode the method into the image.						** Pixel 0
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			
		//methodInBinary is 6 bits long
		StringBuffer methodInBinary = new StringBuffer( Integer.toBinaryString(stegoMethod[0]) );
		while (methodInBinary.length()<6)
			methodInBinary.insert(0, '0');
		
		encodePixel(methodInBinary.substring(0, 6), pseudoRandomNumber.getNextPsuedo(), TWOBITSPERBYTE);
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now encode the length of the filename into the image.		** Pixel 1
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				
		//fileLengthInBinary is 6 bits long
		StringBuffer fileLengthInBinary = new StringBuffer( Integer.toBinaryString(fileLength) );
		while (fileLengthInBinary.length()<6)
			fileLengthInBinary.insert(0, '0');
		
		encodePixel(fileLengthInBinary.substring(0, 6), pseudoRandomNumber.getNextPsuedo(), TWOBITSPERBYTE);
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now encode the length of the Data File into the image.		** Pixel 2 - 6
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//convert the data length into a 30 bit string
		StringBuffer dataLengthString = new StringBuffer( Integer.toBinaryString(dataLength) );
		while (dataLengthString.length()<30)
			dataLengthString.insert(0, '0');
		//5 pixels
		for (int i = 2 ; i < 7 ; i++) {
			int start = (i - 2) * 6;
			int end = (i - 2) * 6 + 6;
			//System.out.println("start : " + start);
			//System.out.println("end : " + end);
			encodePixel(dataLengthString.substring(start, end), pseudoRandomNumber.getNextPsuedo(), TWOBITSPERBYTE);
		}
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now encode the filename into the image.						** Pixel 7 - 7+numberOfPixelsOfFile
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		StringBuffer fileNameBuffer = new StringBuffer("");
		int index = 0;
		
		//go through all pixels
		for (int i = 7 ; i <= 7 + numberOfPixelsOfFile ; i ++) {
			//extend running string if needs be
			while (fileNameBuffer.length() < bitsPerPixel) {
				//System.out.println("index = " + index);
				StringBuffer temp = new StringBuffer( Integer.toBinaryString(fileName[index]) );
				while (temp.length()<8)
					temp.insert(0, '0');
				fileNameBuffer.append(temp);// += Integer.toBinaryString(fileName[index]);
				index++;
				//ensure we can read the next byte
				if (index == fileLength && fileNameBuffer.length() < bitsPerPixel ) {
					while (fileNameBuffer.length() < bitsPerPixel)
						fileNameBuffer.append("0");
				}
			}
			//System.out.println("fileNameBuffer : " + fileNameBuffer);
			encodePixel(fileNameBuffer.substring(0, bitsPerPixel), pseudoRandomNumber.getNextPsuedo(), method);
			fileNameBuffer.delete(0, bitsPerPixel);
			
			if (i % increment == 0) {
				progress.setValue(i);
				myFrame.update(myFrame.getGraphics());
			}
			
			//reached the end, and no more data
			if (index == fileLength && fileNameBuffer.length() == 0) {
				//System.out.println("exit = true, fileNameBuffer empty");
				break;
			} 
			//reached the end, but still data in the buffer - must continue
			else if (index == fileLength && fileNameBuffer.length() < bitsPerPixel ) {
				while (fileNameBuffer.length() < bitsPerPixel)
					fileNameBuffer.append("0");
				//System.out.println("exit = true, fileNameBuffer contains : " + fileNameBuffer.length());
			}
		}
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//		now encode the data bytes into the image.					** Pixel 7+numberOfPixelsOfFile - 7+numberOfPixelsOfFile+numberOfPixelsOfData
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		index = 0;
		StringBuffer dataFileBuffer = new StringBuffer("");
		
		//go through all remaining pixels
		for (int i = 7 + numberOfPixelsOfFile + 1; i <= 7 + numberOfPixelsOfFile + 1 + numberOfPixelsOfData ; i ++) {
			//extend running string if needs be
			while (dataFileBuffer.length() < bitsPerPixel) {
				//System.out.println("index = " + index);
				StringBuffer temp = new StringBuffer( Integer.toBinaryString(dataBytes[index]) ); //8 or 32 bits here
				while (temp.length()<8)
					temp.insert(0, '0');
				//CHECK FOR NEGATIVE
				if (temp.length() == 32) { //truncate to 8 bits
					temp.delete(0, 24);
				}
				dataFileBuffer.append(temp);
				index++;
				//ensure we can read the next byte
				if (index == dataLength && dataFileBuffer.length() < bitsPerPixel ) {
					while (dataFileBuffer.length() < bitsPerPixel)
						dataFileBuffer.append("0");
				}
			}
			//System.out.println("dataFileBuffer = " + dataFileBuffer);
			encodePixel(dataFileBuffer.substring(0, bitsPerPixel), pseudoRandomNumber.getNextPsuedo(), method);
			dataFileBuffer.delete(0, bitsPerPixel);
			
			if (i % increment == 0) {
				progress.setValue(i);
				myFrame.update(myFrame.getGraphics());
			}
			
			//reached the end, and no more data
			if (index == dataLength && dataFileBuffer.length() == 0) {
				//System.out.println("exit = true, dataFileBuffer empty");
				break;
			} 
			//reached the end, but still data in the buffer - must continue
			else if (index == dataLength && dataFileBuffer.length() < bitsPerPixel ) {
				while (dataFileBuffer.length() < bitsPerPixel)
					dataFileBuffer.append("0");
				//System.out.println("exit = true, dataFileBuffer contains : " + dataFileBuffer.length());
			}
		}
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//							++ END ++
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//debug
		/*System.out.println(" after encoding ");
		for (int i = 0 ; i < ndata.length ; i++) {
			StringBuffer buf = new StringBuffer( Integer.toBinaryString(ndata[i]) );
			// 4 bytes
			while (buf.length()<32)
			   buf.insert(0, '0');
			System.out.println("ndata[" + i + "] : " + buf);
		}*/
		return true;
	}
	
	/**
	 * A nice method that can encode a 
	 * pixel with the data using a specified method
	 * 
	 * Also implements Minimum Error Replacement (MER)
	 * 
	 * @param string the bits to be hidden in the string
	 * @param i the index of the pixel in the array ndata
	 * @param method the LSB replacement method
	 */
	private void encodePixel(String string, int i, int method) {	
		//get data bytes
		StringBuffer temp = new StringBuffer( Integer.toBinaryString(ndata[i]) );
		StringBuffer origByte0 = new StringBuffer(temp.substring(0,8));
		StringBuffer origByte1 = new StringBuffer(temp.substring(8,16));
		StringBuffer origByte2 = new StringBuffer(temp.substring(16,24));
		StringBuffer origByte3 = new StringBuffer(temp.substring(24,32));
		
		//now modify LSB's
		StringBuffer newByte1 = new StringBuffer(origByte1.substring(0, (8 - method) ));
		newByte1.append(string.substring(0,method));
		StringBuffer newByte2 = new StringBuffer(origByte2.substring(0, (8 - method) ));
		newByte2.append(string.substring(method,method * 2));
		StringBuffer newByte3 = new StringBuffer(origByte3.substring(0, (8 - method) ));
		newByte3.append(string.substring(method * 2,method * 3));
		
		//perform MER
		String bestByte1 = findMinimumErrorReplacement(origByte1,newByte1,method);
		String bestByte2 = findMinimumErrorReplacement(origByte2,newByte2,method);
		String bestByte3 = findMinimumErrorReplacement(origByte3,newByte3,method);
		
		//form the new pixel
		String newPixel = origByte0.toString() + bestByte1 + bestByte2 + bestByte3;
		
		//modify the picture
		int newDecimal = convertNegativeBinaryToDecimal(newPixel);
		ndata[i] = newDecimal;
	}

	/**
	 * Performs the Minimum Error Replacement (MER)
	 * @param origByte the original byte of the picture
	 * @param newByte the same byte after it's LSB's have been modified
	 * @param method the steganographic method used
	 * @return the best byte after MER has occurred
	 */
	private String findMinimumErrorReplacement(StringBuffer origByte, StringBuffer newByte, int method) {
		String temp;
	
		int questionLSB = 8 - (method + 1);
		//System.out.println("LSB to change : " + questionLSB);
		
		//firstly look at the next LSB and change it
		if (newByte.charAt(questionLSB) == '0') {
			//System.out.println("0 -> 1");
			//System.out.println("newByte.substring(0, questionLSB) = " + newByte.substring(0, questionLSB));
			temp = newByte.substring(0, questionLSB) + "1" + newByte.substring(questionLSB + 1, 8);
		} else {
			//System.out.println("1 -> 0");
			temp = newByte.substring(0, questionLSB) + "0" + newByte.substring(questionLSB + 1, 8);
		}
		//System.out.println("questionLSB : " + temp);
		
		int originalDecimal = Integer.parseInt(origByte.toString(),2);
		int newByteDecimal = Integer.parseInt(newByte.toString(),2);
		int questionLSBDecimal = Integer.parseInt(temp,2);
		
		//System.out.println("originalDecimal : " + originalDecimal);
		//System.out.println("newByteDecimal : " + newByteDecimal);
		//System.out.println("questionLSBDecimal : " + questionLSBDecimal);
		
		//if original newByte was the best
		if ( Math.abs(originalDecimal - newByteDecimal) < Math.abs(originalDecimal - questionLSBDecimal) ) {
			//System.out.println("orig");
			return newByte.toString();
		} else {
			//System.out.println("modified");
			return temp;
		}
	}

	/**
	 * Converts a Signed Binary number to a Signed Integer
	 * Two's Complement
	 * -2,147,483,648 -> 2,147,483,647
	 * 
	 * @param newPixel the string that is signed binary
	 * @return the decimal number (can be negative)
	 */
	private int convertNegativeBinaryToDecimal(String pixel) {
		int[] binary = new int[32];
		for (int i = 0 ; i < pixel.length() ; i ++) {
			binary[i] = Integer.parseInt(pixel.substring(i, i+1));
		}
		
		//invert
		for (int i = 0 ; i < 32 ; i++) {
			if (binary[i] == 1) {
				binary[i] = 0;
			} else {
				binary[i] = 1;
			}
		}
		
		//add one
		for (int i = 31 ; i >= 0 ; i--) {
			if (binary[i] == 0) {
				binary[i] = 1; 
				break;
			} else {
				binary[i] = 0;
			}
		}
		
		int decimal = 0;
		for(int i = 31 ; i >= 0 ; i--)
			   decimal += binary[i]*Math.pow(2,31-i);

		return -decimal;
	}

	/**
	 * Converts the method (int) into a byte
	 * @param method the steganographic method used
	 * @return the byte representation of the method
	 */
	private byte[] encodeMethod(int method) {
		byte[] binaryMethod = new byte[1];
		binaryMethod[0] = (byte) method;
		return binaryMethod;
	}

	/**
	 * Converts the Data File Name (array of ints) into a byte array
	 * Also does a check to see if the filename is too long, and will truncate accordingly
	 * @return the new byte array
	 */
	private byte[] encodeFileName() {
		String asciiName = dataFileName.getName();
		
		//check for a filename greater than 64 chars (will have to truncate if so
		if (asciiName.length() > 63) {
			StringBuffer temp = new StringBuffer(asciiName);
			temp.delete((asciiName.length() - 4) - (asciiName.length() - 63), (asciiName.length() - 4) );
			asciiName = temp.toString();
			System.out.println("asciiName = " + asciiName);
			JOptionPane.showMessageDialog(myFrame  , "Data File Name is going to be truncated to\n" +
					asciiName + "\nas it was too long.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
		byte[] binaryName = new byte[asciiName.length()];
				
		for (int i = 0 ; i < asciiName.length() ; i++) {
			binaryName[i] =  (byte) asciiName.charAt(i);
		}
		return binaryName;
	}

	/**
	 * @return Returns the coverFileName.
	 */
	public File getCoverFileName() {
		return coverFileName;
	}

	/**
	 * @param coverFileName The coverFileName to set.
	 */
	public void setCoverFileName(File coverFileName) {
		this.coverFileName = coverFileName;
	}

	/**
	 * @return Returns the dataFileName.
	 */
	public File getDataFileName() {
		return dataFileName;
	}

	/**
	 * @param dataFileName The dataFileName to set.
	 */
	public void setDataFileName(File dataFileName) {
		this.dataFileName = dataFileName;
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
	 * 
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
