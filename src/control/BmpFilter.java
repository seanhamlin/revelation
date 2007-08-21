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

/**
 * The BMP Filter class. The class is resposible for
 * adding a BMP Filter to the JFileChoosers in Revelation
 * @author Sean Hamlin
 * @version 1.0
 */
public class BmpFilter extends javax.swing.filechooser.FileFilter
{
  /**
    This is the one of the methods that is declared in
    the abstract class
   */
  public boolean accept(File f)
  {
    //if it is a directory -- we want to show it so return true.
    if (f.isDirectory())
      return true;
  
    //get the extension of the file

    String extension = getExtension(f);
    //check to see if the extension is equal to "bmp"
    if (extension.equals("bmp")) // || (extension.equals("htm")))
       return true;

    //default -- fall through. False is return on all
    //occasions except:
    //a) the file is a directory
    //b) the file's extension is what we are looking for.
    return false;
  }
    
  /**
    Again, this is declared in the abstract class

    The description of this filter
   */
  public String getDescription()
  {
      return "Bitmap files (*.bmp)";
  }

  /**
    Method to get the extension of the file, in lowercase
   */
  private String getExtension(File f)
  {
    String s = f.getName();
    int i = s.lastIndexOf('.');
    if (i > 0 &&  i < s.length() - 1)
      return s.substring(i+1).toLowerCase();
    return "";
  }
}