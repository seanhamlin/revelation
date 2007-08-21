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

package gui;

import help.HelpDialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import control.BmpFilter;
import control.ConcealControl;

/**
 * The Select Cover File class. The class is responsible for
 * displaying the select cover file screen in Revelation
 * @author Sean Hamlin
 * @version 1.0
 */
public class SelectCoverFile 
{
	ConcealControl concealControl;
	
	static JFrame frame;
	static JLabel showBackground;
	static JButton help;
	static JFileChooser fc;
	static JTextPane filePathString;
	static JButton open;
	static JLabel actualSize;
	static JLabel actualDate;
	static JButton preview;
	
	static JButton rightArrow;
	static JButton leftArrow;
	static JButton cancel;
	
	static JLabel selectCoverFile;
	static JLabel fileProperties;
	static JLabel size;
	static JLabel date;
	
	static JPanel properties;
	static JPanel bottomPanel;
	
	File fileName = null;

	private static final String PATH = "data/images/";
	
	/**
	 * Builds the GUI
	 * @param pane the content pane to add the components to
	 */
    public void addComponentsToPane(Container pane) 
	{
		final ImageIcon bannerIcon = loadIcon("banner2.jpg");
		showBackground = new JLabel(bannerIcon);
		
		final ImageIcon helpIcon = loadIcon("help.gif");
		help = new JButton("Help", helpIcon);
		
		final ImageIcon openIcon = loadIcon("open.gif");
		open = new JButton("Open", openIcon);
		
		final ImageIcon previewIcon = loadIcon("glass.jpg");
		final ImageIcon preview2Icon = loadIcon("glass2.jpg");
		preview = new JButton(previewIcon);
		preview.setBorder(BorderFactory.createEmptyBorder());
		preview.setRolloverEnabled(true);
		preview.setRolloverIcon(preview2Icon);
		
		final ImageIcon blackIcon = loadIcon("black.jpg");
		final ImageIcon rightArrowIcon = loadIcon("rightArrow.jpg");
		final ImageIcon rightArrow2Icon = loadIcon("rightArrow2.jpg");
		rightArrow = new JButton(rightArrowIcon);
		rightArrow.setBorder(BorderFactory.createEmptyBorder());
		rightArrow.setDisabledIcon(blackIcon);
		rightArrow.setRolloverEnabled(true);
		rightArrow.setRolloverIcon(rightArrow2Icon);
		rightArrow.setEnabled(false);
		
		final ImageIcon leftArrowIcon = loadIcon("leftArrow.jpg");
		final ImageIcon leftArrow2Icon = loadIcon("leftArrow2.jpg");
		leftArrow = new JButton(leftArrowIcon);
		leftArrow.setBorder(BorderFactory.createEmptyBorder());
		leftArrow.setRolloverEnabled(true);
		leftArrow.setRolloverIcon(leftArrow2Icon);
		
		final ImageIcon cancelIcon = loadIcon("cancel.jpg");
		final ImageIcon cancel2Icon = loadIcon("cancel2.jpg");
		cancel = new JButton(cancelIcon);
		cancel.setBorder(BorderFactory.createEmptyBorder());
		cancel.setRolloverEnabled(true);
		cancel.setRolloverIcon(cancel2Icon);
		
		final ImageIcon selectCoverFileIcon = loadIcon("selectCoverImage.jpg");
		selectCoverFile = new JLabel(selectCoverFileIcon);
		
		fileProperties = new JLabel("File Properties");
		filePathString = new JTextPane();	
		
		actualSize = new JLabel();
		actualSize.setText(" "); //initially empty
		
		actualDate = new JLabel();
		actualDate.setText(" "); //initially empty
		
		filePathString.setText("C:\\");
			
		fileName = concealControl.getCoverFileName();
		
		//set up the screen if fileName exists
		if (fileName != null) {
			filePathString.setText(fileName.getName());	
			rightArrow.setEnabled(true);
			
			// < 1 kilobyte
			if (fileName.length() < 1024){
				actualSize.setText(fileName.length() + " Bytes");
			}
			// < 10 megs
			else if (fileName.length() < 10485760){
				actualSize.setText(fileName.length() / 1024 + " KB");
			}
			// > 10 megs
			else {
				actualSize.setText(fileName.length() / 1048576 + " MB");
			}

			actualDate.setText("" + new java.util.Date(fileName.lastModified()));
		}
		
		
        pane.setLayout(new GridBagLayout());
		pane.setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();
		
		Color myGreen = new Color(16,119,27);
		
		Border myLineBorder = BorderFactory.createLineBorder(myGreen, 1);
		Border innerBorder = BorderFactory.createLineBorder(Color.BLACK, 5);
		CompoundBorder wholeBorder = new CompoundBorder(myLineBorder, innerBorder);

        //the Revelation Banner (row 0) ++++++++++
        c.weightx = 1.0; //can grow horizontally
		c.weighty = 0.0; //does not grow vertically
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
		c.gridwidth = 4; //span all 4 cols
		c.insets = new Insets(5,5,5,5);
        pane.add(showBackground, c);

		//The Select Data File (row 1) +++++++++++++
		selectCoverFile.setToolTipText("Step 2");
		selectCoverFile.setPreferredSize(new Dimension(350, 35));
        c.gridx = 0;
        c.gridy = 1;
		//c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipady = 10;      //make this component tall
		c.ipadx = 0;
        pane.add(selectCoverFile, c);

		//The JFileChooser (row 3) +++++++++++++
		filePathString.setEditable(false);
		filePathString.setBackground(Color.BLACK);
		filePathString.setForeground(Color.ORANGE);
		filePathString.setBorder(wholeBorder);
        c.gridx = 0;
        c.gridy = 2;
		c.ipadx = 0;
		c.ipady = 0; 
		c.gridheight = 1;
		c.gridwidth = 3; 
		pane.add(filePathString, c);
		
		//Open Button
		open.setPreferredSize(new Dimension(70, 10));
        c.gridx = 3;
        c.gridy = 2;
		c.ipadx = 0;
		c.ipady = 0; 
		c.gridheight = 1;
		c.gridwidth = 1; 
		pane.add(open, c);
		
		//The file properties row (row 4) +++++++++++++ 
		fileProperties.setBackground(Color.BLACK);
		fileProperties.setForeground(Color.ORANGE);
		fileProperties.setToolTipText("Properties for the selected file");
        c.gridx = 0;
        c.gridy = 3;
		c.ipadx = 0;
		c.ipady = 0; 
		c.gridheight = 1;
		c.gridwidth = 1; 
        pane.add(fileProperties, c);
		
			// !! properties JPanel !!
			properties = new JPanel();
			properties.setLayout(new GridBagLayout());
			GridBagConstraints newC = new GridBagConstraints();
			properties.setBackground(Color.BLACK);
			
			newC.weightx = 0.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.HORIZONTAL;
			newC.gridx = 0;
			newC.gridy = 0;
			newC.gridwidth = 1;
			//newC.insets = new Insets(5,5,5,5);
			
			size = new JLabel("Size : ");
			size.setBackground(Color.BLACK);
			size.setForeground(Color.ORANGE);
			size.setToolTipText("The size of the selected file");
			size.setPreferredSize(new Dimension(50,50));

			properties.add(size, newC);
			
			newC.weightx = 1.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.BOTH;
			newC.gridx = 1;
			newC.gridy = 0;
			
			actualSize.setBackground(Color.BLACK);
			actualSize.setForeground(Color.ORANGE);
			actualSize.setPreferredSize(new Dimension(50,50));

			properties.add(actualSize, newC);
			
			newC.weightx = 0.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.HORIZONTAL;
			newC.gridx = 0;
			newC.gridy = 1;
			
			date = new JLabel("Date : ");
			date.setBackground(Color.BLACK);
			date.setForeground(Color.ORANGE);
			date.setToolTipText("The date of the selected file");
			date.setPreferredSize(new Dimension(50,50));

			properties.add(date, newC);
			
			newC.weightx = 1.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.BOTH;
			newC.gridx = 1;
			newC.gridy = 1;
			
			actualDate.setBackground(Color.BLACK);
			actualDate.setForeground(Color.ORANGE);
			actualDate.setPreferredSize(new Dimension(185,50));

			properties.add(actualDate, newC);
			
			properties.setBorder(wholeBorder);
		
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.insets = new Insets(5,5,0,5);
		pane.add(properties,c);
		
		
		//showGlass
		preview.setBackground(Color.BLACK);
        c.weightx = 1.0; //can grow horizontally
		c.weighty = 1.0; //can grow vertically
		//c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
		c.gridheight = 2; //span 2 rows
		c.gridwidth = 1; 
		//c.insets = new Insets(5,5,5,5);
        pane.add(preview, c);
		
		help.setToolTipText("Help with this Step");
		help.setMnemonic('H');
		help.setPreferredSize(new Dimension(85, 10));
        c.gridx = 3;
        c.gridy = 4;
		c.gridheight = 2; //span 2 rows
		c.gridwidth = 1; 
		c.ipadx = 0;
        c.ipady = 0;      
        pane.add(help, c);
		
			//!! Bottom Panel 
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new GridBagLayout());
			GridBagConstraints newNewC = new GridBagConstraints();
			bottomPanel.setBackground(Color.BLACK);
		
			//leftArrow.setToolTipText("Previous Step");
			leftArrow.setPreferredSize(new Dimension(150, 47));
			newNewC.gridx = 0;
			newNewC.gridy = 0;
			newNewC.gridheight = 1;
			newNewC.gridwidth = 1; 
			newNewC.ipadx = 0;
			newNewC.ipady = 0;   
			newNewC.weightx = 0.0;
			newNewC.weighty = 0.0; 
			newNewC.fill = GridBagConstraints.NONE;
			newNewC.insets = new Insets(0,10,0,10);
			bottomPanel.add(leftArrow, newNewC);
					
			cancel.setToolTipText("Cancel");
			cancel.setPreferredSize(new Dimension(50, 47));
			newNewC.gridx = 1;
			newNewC.gridy = 0;
			bottomPanel.add(cancel, newNewC);
			
			//rightArrow.setToolTipText("Next Step");
			rightArrow.setPreferredSize(new Dimension(150, 47));
			newNewC.gridx = 2;
			newNewC.gridy = 0;
			bottomPanel.add(rightArrow, newNewC);
			
			bottomPanel.setBorder(wholeBorder);
			
        c.gridx = 0;
        c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 4; 
        c.weightx = 0.0;
		c.weighty = 0.0; 
		c.insets = new Insets(5,5,5,5);
		pane.add(bottomPanel, c);
    }
	
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() 
	{
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        frame = new JFrame("Revelation - Conceal (Step 2)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set the icon
		URL url = ClassLoader.getSystemResource(PATH + "icon.gif");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
			
        //Display the window.
        frame.pack();
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width) / 2,
                (d.height - frame.getSize().height) / 2);
        frame.setVisible(true);
    }
	
	/**
	 * Default constructor
	 * @param concealControl a reference to concealControl
	 */
	public SelectCoverFile(ConcealControl concealControl) {
		this.concealControl = concealControl;
	}
	
	/**
	 * Creates and shows the GUI
	 */
	public void CreateGUI()
	{
		createAndShowGUI();	
		help.addActionListener(new helpButtonAction());
		open.addActionListener(new openFileButtonAction());
		preview.addActionListener(new previewButtonAction());
		rightArrow.addActionListener(new rightButtonAction());
		leftArrow.addActionListener(new leftButtonAction());
		cancel.addActionListener(new cancelButtonAction());
	}
	
	/**
	 * Load the icon.
	 * @param aFileName the icon file name
	 * @return an icon
	 */
	private static ImageIcon loadIcon(String aFileName) {
		URL url = ClassLoader.getSystemResource(PATH + aFileName);
		if (url!=null) return new ImageIcon(url);
		else System.out.println("Cannot locate image " + PATH + aFileName);
		return null;
	}
	
	/**
	 * Takes you to Step 3 of Conceal
	 * @author Sean Hamlin
	 */
	private class rightButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			new SelectPasswordConceal(concealControl).CreateGUI();
		}
	}
	
	/**
	 * Takes you to Step 1 of Conceal
	 * @author Sean Hamlin
	 */
	private class leftButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			new SelectDataFile(concealControl).CreateGUI();
		}
	}
	
	/**
	 * Takes you to the main menu
	 * @author Sean Hamlin
	 */
	private class cancelButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			Main.main(null);
		}
	}
	
	/**
	 * Lets the user select a file to hide
	 * @author Sean Hamlin
	 */
	private class openFileButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			//create the file chooser
			fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setFileFilter(new BmpFilter());
			
			int result = fc.showDialog(frame, "Conceal");
			
			if (result == JFileChooser.CANCEL_OPTION) {
				return;
			}
			
			fileName = fc.getSelectedFile();
			
			if (fileName == null || fileName.getName().equals(""))
			{
				JOptionPane.showMessageDialog(frame  , "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);;
			}
			else
			{
				//set up the references
				concealControl.setCoverFileName(fileName);
				
				//fileName.getAbsolutePath()
				//fileName.getName()
				//fileName.length()
				//fileName.lastModified()
				//fileName.isHidden()
				filePathString.setText(fileName.getName());	
				
				rightArrow.setEnabled(true);
				
				// < 1 kilobyte
				if (fileName.length() < 1024){
					actualSize.setText(fileName.length() + " Bytes");
				}
				// < 10 megs
				else if (fileName.length() < 10485760){
					actualSize.setText(fileName.length() / 1024 + " KB");
				}
				// > 10 megs
				else {
					actualSize.setText(fileName.length() / 1048576 + " MB");
				}

				actualDate.setText("" + new java.util.Date(fileName.lastModified()));
				
				frame.pack(); //make sure the date fits nicely.
			}
		}
	}
	
	/**
	 * Lets the user preview the selected Data File
	 * @author Sean Hamlin
	 */
	private class previewButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			//# Windows
			//# Linux
			//# Solaris
			//# Other Unix platforms
			//# Mac
			//# OS/2
			
			if (fileName == null) {
				JOptionPane.showMessageDialog(frame  , "Please select a Cover File first !", "Preview", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				String osName = System.getProperty("os.name");
				//System.out.println ( System.getProperty("os.name") );
	
				//windows specific
				if (osName.substring(0,6).toUpperCase().equals("WINDOW")) {
					try {
						Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + fileName.getAbsolutePath());
					} catch (Exception e) {
						System.out.println("Unable to open : " + fileName.getAbsolutePath());
					}
				}
				//mac specific
				else if (osName.substring(0,3).toUpperCase().equals("MAC")){
					try {
						Runtime.getRuntime().exec("open " + fileName.getAbsolutePath());
					} catch (Exception e) {
						System.out.println("Unable to open : " + fileName.getAbsolutePath());
					}
				}
				else {
					JOptionPane.showMessageDialog(frame  , "Windows and Mac Specific Preview only, " + osName + " is not supported !", "Information", JOptionPane.ERROR_MESSAGE);
				}	
			}
		}
	}
	
	/**
	 * Pops-up the built in help
	 * @author Sean Hamlin
	 */
	private class helpButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			new HelpDialog(frame, "concealStepTwo.html", "Help with Conceal (Step 2)");
		}
	}
}

