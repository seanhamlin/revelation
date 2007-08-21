 /* 
  * Copyright (c) 2007 Sean Hamlin
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import security.PasswordChecker;
import security.PasswordCheckerImpl;

import control.ConcealControl;

/**
 * The Select Data File class. The class is responsible for
 * displaying the select data file screen in Revelation
 * @author Sean Hamlin
 * @version 1.0
 * @since 2.0
 */
public class SelectPasswordConceal {
	private ConcealControl concealControl;
	private PasswordChecker pwChecker;
	
	private static JFrame frame;
	private static JLabel showBackground;
	private static JButton help;
	private static JPasswordField passwordString1;
	private static JPasswordField passwordString2;
	private static JLabel type;
	private static JLabel reType;
	private static JLabel strength;
	private static JTextField strengthColor;
	
	private static JButton rightArrow;
	private static JButton leftArrow;
	private static JButton cancel;
	
	private static JLabel selectPassword;
	
	private static JPanel passwordPanel;
	private static JPanel bottomPanel;
	
	private static JRadioButton defaultPassword;
	private static JRadioButton yourPassword;
	private static JPanel selectPasswordPanel;

	private static final String PATH = "data/images/";
	
	private void addComponentsToPane(Container pane) 
	{
		final ImageIcon bannerIcon = loadIcon("banner2.jpg");
		showBackground = new JLabel(bannerIcon);
		
		final ImageIcon helpIcon = loadIcon("help.gif");
		help = new JButton("Help", helpIcon);
				
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
		
		final ImageIcon selectPasswordIcon = loadIcon("selectPassword.jpg");
		selectPassword = new JLabel(selectPasswordIcon);
						
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
		c.gridwidth = 3; //span all 3 cols
		c.insets = new Insets(5,5,5,5);
        pane.add(showBackground, c);

		//The Select Password (row 1) +++++++++++++
		selectPassword.setToolTipText("Step 3");							// <== change here
		selectPassword.setPreferredSize(new Dimension(350, 35));
        c.gridx = 0;
        c.gridy = 1;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipady = 10;      //make this component tall
		c.ipadx = 0;
        pane.add(selectPassword, c);

			//Select Password Option Panel 
			selectPasswordPanel = new JPanel();
			selectPasswordPanel.setLayout(new GridBagLayout());
			GridBagConstraints newNewC = new GridBagConstraints();
			selectPasswordPanel.setBackground(Color.BLACK);
	
			//No password check box
			defaultPassword = new JRadioButton("Default Password", false); //off by default
			defaultPassword.setBackground(Color.BLACK);
			defaultPassword.setForeground(Color.ORANGE);
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
			selectPasswordPanel.add(defaultPassword, newNewC);
					
			yourPassword = new JRadioButton("Your Password", true); //on by default
			yourPassword.setBackground(Color.BLACK);
			yourPassword.setForeground(Color.ORANGE);
			newNewC.gridx = 1;
			newNewC.gridy = 0;
			selectPasswordPanel.add(yourPassword, newNewC);
			
		    ButtonGroup group = new ButtonGroup(); //only one radio button at a time
		    group.add(defaultPassword);
		    group.add(yourPassword);
			
			selectPasswordPanel.setBorder(innerBorder);
			
	    c.gridx = 0;
	    c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 3; 
	    c.weightx = 0.0;
		c.weighty = 0.0; 
		c.insets = new Insets(5,5,5,5);
		pane.add(selectPasswordPanel, c);
         
        //The password panel (row 2) +++++++++++++++++
			// !! passwordPanel JPanel !!
        	passwordPanel = new JPanel();
        	passwordPanel.setLayout(new GridBagLayout());
			GridBagConstraints newC = new GridBagConstraints();
			passwordPanel.setBackground(Color.BLACK);
			
			newC.weightx = 0.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.HORIZONTAL;
			newC.gridx = 0;
			newC.gridy = 0;
			newC.gridwidth = 1;
			//newC.insets = new Insets(5,5,5,5);
			
			type = new JLabel();
			type.setText("Enter Password : ");
			type.setBackground(Color.BLACK);
			type.setForeground(Color.ORANGE);
			type.setToolTipText("Must be greater than 4 characters");
			type.setPreferredSize(new Dimension(100,25));
			
			passwordPanel.add(type, newC);
			
			newC.weightx = 1.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.BOTH;
			newC.gridx = 1;
			newC.gridy = 0;
			
			passwordString1 = new JPasswordField();	
			//passwordString1.setEchoChar('*');
			passwordString1.setBackground(Color.WHITE);
			passwordString1.setForeground(Color.BLACK);
			passwordString1.setBorder(innerBorder);
	
			passwordPanel.add(passwordString1, newC);
			
			newC.weightx = 0.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.HORIZONTAL;
			newC.gridx = 0;
			newC.gridy = 1;
			
			reType = new JLabel();
			reType.setText("Re-type : "); //initially empty
			reType.setBackground(Color.BLACK);
			reType.setForeground(Color.ORANGE);
			reType.setToolTipText("Must be the same as the original password");
			reType.setPreferredSize(new Dimension(100,25));
	
			passwordPanel.add(reType, newC);
			
			newC.weightx = 1.0; //horizontal grow ?
			newC.weighty = 1.0; //vertical grow ?
			newC.fill = GridBagConstraints.BOTH;
			newC.gridx = 1;
			newC.gridy = 1;
			
			passwordString2 = new JPasswordField();	
			passwordString2.setEditable(true);
			passwordString2.setBackground(Color.WHITE);
			passwordString2.setForeground(Color.BLACK);
			passwordString2.setBorder(innerBorder);
	
			passwordPanel.add(passwordString2, newC);
			passwordPanel.setBorder(wholeBorder);
	
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(5,5,0,5);
		pane.add(passwordPanel,c);
        
		//The Strength + help row (row 3) +++++++++++++
		//Strength Label
		strength = new JLabel();
		strength.setText("Strength : ");
		strength.setBackground(Color.BLACK);
		strength.setForeground(Color.ORANGE);
		strength.setPreferredSize(new Dimension(100,25));
		
        c.weightx = 0.0; //can grow horizontally
		c.weighty = 1.0; //does not grow vertically
        c.gridx = 0;
        c.gridy = 4;
		c.ipadx = 0;
		c.ipady = 0; 
		c.gridheight = 1;
		c.gridwidth = 1; 
		pane.add(strength, c);
		
		//Strength Indicator
		strengthColor = new JTextField();
		strengthColor.setText(" ");
		strengthColor.setEditable(false);
		strengthColor.setBackground(Color.BLACK);
		strengthColor.setForeground(Color.BLACK);
		strengthColor.setPreferredSize(new Dimension(200,25));
		
        c.weightx = 1.0; //can grow horizontally
		c.weighty = 1.0; //does not grow vertically
        c.gridx = 1;
        c.gridy = 4;
		c.ipadx = 0;
		c.ipady = 0; 
		c.gridheight = 1;
		c.gridwidth = 1; 
		pane.add(strengthColor, c);
		
		//help Button
		help.setToolTipText("Help with this Step");
		help.setMnemonic('H');
		help.setPreferredSize(new Dimension(100, 25));
		
        c.weightx = 0.0; //can grow horizontally
		c.weighty = 1.0; //does not grow vertically
        c.gridx = 2;
        c.gridy = 4;
		c.ipadx = 0;
		c.ipady = 0; 
		c.gridheight = 1;
		c.gridwidth = 1; 
		pane.add(help, c);	
					
			//!! Bottom Panel 
		
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new GridBagLayout());
			GridBagConstraints newNewNewC = new GridBagConstraints();
			bottomPanel.setBackground(Color.BLACK);
		
			//leftArrow.setToolTipText("Previous Step");
			leftArrow.setPreferredSize(new Dimension(150, 47));
			newNewNewC.gridx = 0;
			newNewNewC.gridy = 0;
			newNewNewC.gridheight = 1;
			newNewNewC.gridwidth = 1; 
			newNewNewC.ipadx = 0;
			newNewNewC.ipady = 0;   
			newNewNewC.weightx = 0.0;
			newNewNewC.weighty = 0.0; 
			newNewNewC.fill = GridBagConstraints.NONE;
			newNewNewC.insets = new Insets(0,10,0,10);
			bottomPanel.add(leftArrow, newNewNewC);
					
			cancel.setToolTipText("Cancel");
			cancel.setPreferredSize(new Dimension(50, 47));
			newNewNewC.gridx = 1;
			newNewNewC.gridy = 0;
			bottomPanel.add(cancel, newNewNewC);
			
			//rightArrow.setToolTipText("Next Step");
			rightArrow.setPreferredSize(new Dimension(150, 47));
			newNewNewC.gridx = 2;
			newNewNewC.gridy = 0;
			
			bottomPanel.add(rightArrow, newNewNewC);
			bottomPanel.setBorder(wholeBorder);
			
        c.gridx = 0;
        c.gridy = 5;
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
        frame = new JFrame("Revelation - Conceal (Step 3)");
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
	 * Application main method.  
	 * Parameters:  -Xms32m  (minimum heap memory)
	 * 				-Xmx128m (maximum heap memory)
	 */
    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SelectPasswordConceal(null).CreateGUI();
            }
        });
    }
	
	/**
	 * Default constructor
	 * @param concealControl a reference to concealControl
	 */
	public SelectPasswordConceal(ConcealControl concealControl) {
		this.concealControl = concealControl;
	}
	
	/**
	 * Creates and shows the GUI
	 */
	void CreateGUI()
	{
		//create a new instance if this is the first time
		if (concealControl == null)
			concealControl = new ConcealControl();
		
		createAndShowGUI();
		
		//set up the password checker
		pwChecker = new PasswordCheckerImpl();
		pwChecker.setUpReferences(passwordString1, strengthColor);
		pwChecker.init();
	
		//add action listeners, key listeners
		help.addActionListener(new helpButtonAction());
		rightArrow.addActionListener(new rightButtonAction());
		leftArrow.addActionListener(new leftButtonAction());
		cancel.addActionListener(new cancelButtonAction());
		passwordString1.addKeyListener(new passwordString1Action());
		passwordString2.addKeyListener(new passwordString2Action());
		defaultPassword.addActionListener(new defaultPasswordAction());
		yourPassword.addActionListener(new yourPasswordAction());
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
	 * Takes you to Step 4 (encoding step) of Conceal
	 * @author Sean Hamlin
	 */
	private class rightButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {		
			//if right arrow is enabled, password is either default, or verified user password
			if (defaultPassword.isSelected()) {
				concealControl.setPassword("password");
			} else {
				char[] pw1 = passwordString1.getPassword();
				StringBuffer temp = new StringBuffer();
				for (int i = 0 ; i < pw1.length ; i++ ){
					temp.append(pw1[i]);
				}
				concealControl.setPassword(temp.toString());
				
				//clear the char [] so it does not linger in memory
				Arrays.fill(pw1,'0');
				temp = null;
			}

			frame.dispose();
			EncodeStegoImage encodeStegoImage = new EncodeStegoImage(concealControl);
			encodeStegoImage.CreateGUI();
		}
	}
	
	/**
	 * Takes you back to the Conceal Step 2
	 * @author Sean Hamlin
	 */
	private class leftButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			SelectCoverFile selectCoverFile = new SelectCoverFile(concealControl);
			selectCoverFile.CreateGUI();
		}
	}
	
	/**
	 * Takes you to back to the main menu
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
			new HelpDialog(frame, "concealStepThree.html", "Help with Conceal (Step 3)");
		}
	}
	
	/**
	 * Listener on the password
	 * @author Sean Hamlin
	 */
	private class passwordString1Action implements KeyListener	{	
		//Unnecessary methods
		public void keyPressed(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
		
		//key has been pressed and character(s) left
		public void keyReleased(KeyEvent arg0) { 
			pwChecker.checkPassword(); 
			checkMatch();
		}
	}
	
	/**
	 * Not to sure what this does
	 * @author Sean Hamlin
	 */
	private class passwordString2Action implements KeyListener	{
		//Unnecessary methods
		public void keyTyped(KeyEvent arg0) {}
		public void keyPressed(KeyEvent arg0) {}

		//key has been pressed and character(s) left
		public void keyReleased(KeyEvent arg0) {
			checkMatch();
		}
	}
	
	/**
	 * Take care of the radio button being selected
	 * @author Sean Hamlin
	 */
	private class defaultPasswordAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			passwordString1.setBackground(Color.GRAY);
			passwordString2.setBackground(Color.GRAY);
			passwordString1.setForeground(Color.DARK_GRAY);
			passwordString2.setForeground(Color.DARK_GRAY);
			passwordString1.setEnabled(false);
			passwordString2.setEnabled(false);
			
			rightArrow.setEnabled(true);
			strengthColor.setText("None");
			strengthColor.setBackground(Color.RED);
		}
	}
	
	/**
	 * Take care of the radio button being selected
	 * @author Sean Hamlin
	 */
	private class yourPasswordAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			passwordString1.setBackground(Color.WHITE);
			passwordString2.setBackground(Color.WHITE);
			passwordString1.setForeground(Color.BLACK);
			passwordString2.setForeground(Color.BLACK);
			passwordString1.setEnabled(true);
			passwordString2.setEnabled(true);
			
			//check to see if passwords > 4 chars, and match
			pwChecker.checkPassword();
			checkMatch();
		}
	}
	
	private void checkMatch() {
		char[] pw1 = passwordString1.getPassword();
		char[] pw2 = passwordString2.getPassword();
		
		//passwords are too short
		if (pw1.length <= 4 || pw2.length <= 4) {
			rightArrow.setEnabled(false);
			return;
		} 
		//passwords are different lengths
		if (pw1.length != pw2.length) {
			rightArrow.setEnabled(false);
			return;
		}
		//passwords contain different characters
		for (int i = 0 ; i < pw1.length ; i++) {
			if (pw1[i] != pw2[i]) {
				rightArrow.setEnabled(false);
				return;
			}
		}
		rightArrow.setEnabled(true);
	}
}
