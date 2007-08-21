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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import control.RevealControl;

/**
 * The Decode Stego Image class. The class is responsible for
 * displaying the decode stego image screen in Revelation
 * @author Sean Hamlin
 * @version 1.1
 * @since 1.0
 */
public class DecodeStegoImage {
	private RevealControl revealControl;
	
	private static JFrame frame;
	private static JLabel showBackground;
	private static JButton help;
	private static JLabel actualCoverFile;
	
	private static JButton rightArrow;
	private static ImageIcon rightArrow3Icon;
	private static JButton leftArrow;
	private static JButton cancel;
	
	private static JLabel decodeStegoImage;
	private static JLabel inputFile;
	private static JLabel StegoImage;
	
	private static JPanel properties;
	private static JPanel bottomPanel;
	
	private File fileName = null;
	
	private boolean hasCompleted = false;

	private static final String PATH = "data/images/";
	
	private JProgressBar progress;
	
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
			
		final ImageIcon blackIcon = loadIcon("black.jpg");
		final ImageIcon rightArrowIcon = loadIcon("rightArrow.jpg");
		final ImageIcon rightArrow2Icon = loadIcon("rightArrow2.jpg");
		rightArrow3Icon = loadIcon("rightArrow3.jpg");
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
		
		final ImageIcon decodeStegoImageIcon = loadIcon("decodeStegoImage.jpg");
		decodeStegoImage = new JLabel(decodeStegoImageIcon);

		inputFile = new JLabel("Input File :");
		
		actualCoverFile = new JLabel();
		actualCoverFile.setText(" "); //initially empty

		fileName = revealControl.getStegoImageFileName();
		
		//set up the screen if fileName exists
		if (fileName != null) {
			rightArrow.setEnabled(true);
			actualCoverFile.setText("" + fileName.getName()); 
		}
		
		Color myGreen = new Color(16,119,27);
		
		Border myLineBorder = BorderFactory.createLineBorder(myGreen, 1);
		Border innerBorder = BorderFactory.createLineBorder(Color.BLACK, 5);
		CompoundBorder wholeBorder = new CompoundBorder(myLineBorder, innerBorder);
		
        pane.setLayout(new GridBagLayout());
		pane.setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();

        //the Revelation Banner (row 0) ++++++++++
        c.weightx = 1.0; //can grow horizontally
		c.weighty = 0.0; //does not grow vertically
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
		c.gridwidth = 3; //span all 4 cols
		c.insets = new Insets(5,5,5,5);
        pane.add(showBackground, c);

		//The Select Data File (row 1) +++++++++++++
		decodeStegoImage.setToolTipText("Step 3");
		decodeStegoImage.setPreferredSize(new Dimension(350, 35));
        c.gridx = 0;
        c.gridy = 1;
		//c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipady = 10;      //make this component tall
		c.ipadx = 0;
        pane.add(decodeStegoImage, c);

		//The JProgress Bar Row (row 2) +++++++++++++
		int minimum = 0;
	    int maximum = 100;
	    progress = new JProgressBar(minimum, maximum);
		progress.setPreferredSize(new Dimension(350, 35));
		progress.setBorder(wholeBorder);
		//progress.setBackground(Color.BLACK);
		//progress.setForeground(myGreen);
        c.gridx = 0;
        c.gridy = 2;
		c.ipadx = 0;
		c.ipady = 0; 
		c.gridheight = 1;
		c.gridwidth = 3; 
		pane.add(progress, c);
				
		//The Input Files row (row 3) +++++++++++++ 
		inputFile.setBackground(Color.BLACK);
		inputFile.setForeground(Color.ORANGE);
		inputFile.setToolTipText("Properties for all the selected file");
        c.gridx = 0;
        c.gridy = 3;
		c.ipadx = 0;
		c.ipady = 0; 
		c.gridheight = 1;
		c.gridwidth = 1; 
        pane.add(inputFile, c);
		
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
			
			StegoImage = new JLabel("Cover Image : ");
			StegoImage.setBackground(Color.BLACK);
			StegoImage.setForeground(Color.ORANGE);
			StegoImage.setToolTipText("The image in which to hide the data file");
			StegoImage.setPreferredSize(new Dimension(85,50));
			
			properties.add(StegoImage, newC);
			
			newC.weightx = 0.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.HORIZONTAL;
			newC.gridx = 1;
			newC.gridy = 0;
			
			actualCoverFile.setBackground(Color.BLACK);
			actualCoverFile.setForeground(Color.ORANGE);
			actualCoverFile.setPreferredSize(new Dimension(185,50));

			properties.add(actualCoverFile, newC);
			properties.setBorder(wholeBorder);
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(5,5,0,5);
		pane.add(properties,c);
				
		help.setToolTipText("Help with this Step");
		help.setMnemonic('H');
		help.setPreferredSize(new Dimension(85, 10));
        c.gridx = 2;
        c.gridy = 4;
		c.gridheight = 1; //span 2 rows
		c.gridwidth = 1; 
		c.ipadx = 0;
        c.ipady = 0;      
        pane.add(help, c);
			
			//!! Bottom Panel 
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new GridBagLayout());
			GridBagConstraints newNewC = new GridBagConstraints();
			bottomPanel.setBackground(Color.BLACK);
		
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
			
			rightArrow.setPreferredSize(new Dimension(150, 47));
			newNewC.gridx = 2;
			newNewC.gridy = 0;
			bottomPanel.add(rightArrow, newNewC);
			
			bottomPanel.setBorder(wholeBorder);
			
        c.gridx = 0;
        c.gridy = 5; //6
		c.gridheight = 1;
		c.gridwidth = 3; 
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
        frame = new JFrame("Revelation - Reveal (Step 2)");
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
	 * @param revealControl a reference to revealControl
	 */
	public DecodeStegoImage(RevealControl revealControl) {
		this.revealControl = revealControl;
	}
	
	/**
	 * Creates and shows the GUI
	 */
	public void CreateGUI()
	{
		createAndShowGUI();	
		help.addActionListener(new helpButtonAction());
		rightArrow.addActionListener(new rightButtonAction());
		leftArrow.addActionListener(new leftButtonAction());
		cancel.addActionListener(new cancelButtonAction());
		revealControl.setProgress(progress);
		revealControl.setMyFrame(frame);
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
	 * Starts the physical decoding process
	 * @author Sean Hamlin
	 */
	private class rightButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			//hand the power over to reveal control
			if (!hasCompleted) 
				hasCompleted = revealControl.control();
			//if already done then go back to start
			else {
				frame.dispose();
				Main.main(null);
			}
			if (hasCompleted)
				rightArrow.setRolloverIcon(rightArrow3Icon);
		}
	}
	
	/**
	 * Takes you to Step 1 of Reveal
	 * @author Sean Hamlin
	 */
	private class leftButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			new SelectStegoImage(revealControl).CreateGUI();
		}
	}
	
	/**
	 * Takes you back to the main menu
	 * @author Sean Hamlin
	 */
	private class cancelButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			Main.main(null);
		}
	}
	
	/**
	 * Pops-up the built in help
	 * @author Sean Hamlin
	 */
	private class helpButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			new HelpDialog(frame, "revealStepTwo.html", "Help with Reveal (Step 2)");
		}
	}
}

