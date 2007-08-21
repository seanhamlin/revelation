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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * The Main class. The class is responsible for
 * displaying the initial screen in Revelation
 * @author Sean Hamlin
 * @version 1.1
 * @since 1.0
 */
public class Main
{
	static JFrame frame;
	static JLabel showBackground;
	static JButton conceal;
	static JButton reveal;
	static JButton help;
	static JButton exit;
	static JTextPane info1;
	static JTextPane info2;
	static JTextPane info3;
	
	static JPanel leftPanel;
	static JPanel middlePanel;
	static JPanel rightPanel;
	
	private static final String PATH = "data/images/";
	
    public void addComponentsToPane(Container pane) 
	{
		final ImageIcon bannerIcon = loadIcon("banner2.jpg");
		showBackground = new JLabel(bannerIcon);
		final ImageIcon concealIcon = loadIcon("insert.gif");
		conceal = new JButton("Conceal", concealIcon);
		final ImageIcon revealIcon = loadIcon("remove.gif");
		reveal = new JButton("Reveal", revealIcon);
		final ImageIcon helpIcon = loadIcon("help.gif");
		help = new JButton("Help", helpIcon);
		exit = new JButton("Exit");
		info1 = new JTextPane();
		info2 = new JTextPane();
		info3 = new JTextPane();
		
		//Font boldFont = new Font("Times New Roman", Font.PLAIN, 12);
		Color myGreen = new Color(16,119,27);

		Border myLineBorder = BorderFactory.createLineBorder(myGreen, 1);
		Border innerBorder = BorderFactory.createLineBorder(Color.BLACK, 5);
		CompoundBorder wholeBorder = new CompoundBorder(myLineBorder, innerBorder);
		
		/*Border concealBorder = BorderFactory.createTitledBorder(myLineBorder, "Conceal", 
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, boldFont , myGreen);
		Border revealBorder = BorderFactory.createTitledBorder(myLineBorder, "Reveal", 
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, boldFont , myGreen);
		Border helpBorder = BorderFactory.createTitledBorder(myLineBorder, "Help", 
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, boldFont , myGreen);
		*/
	    //b.setDisabledIcon(Icon x);
	    //b.setPressedIcon(Icon x);
	    //b.setSelectedIcon(Icon x);
	    //b.setDisabledSelectedIcon(Icon x);
	   
	    //b.setRolloverEnabled(boolean b); // turn on before rollovers work
	    //b.setRolloverIcon(Icon x);
	    //b.setRolloverSelectedIcon(Icon x);
		
        pane.setLayout(new GridBagLayout());
		pane.setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();

        //the Image Row ++++++++++
        c.weightx = 1.0;
		c.weighty = 0.0; //does not grow vertically
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
		c.gridwidth = 3;
		c.insets = new Insets(5,5,5,5);
        pane.add(showBackground, c);

		//The buttons row +++++++++++++
		//  !! LEFT !!
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		
		conceal.setToolTipText("Conceal a data file inside a image.");
		conceal.setMnemonic('C');
		conceal.setPreferredSize(new Dimension(100, 50));
		
		info1.setText("Encode a data file inside an image.");
		info1.setEditable(false);
		info1.setBackground(Color.BLACK);
		info1.setForeground(Color.ORANGE);
		info1.setPreferredSize(new Dimension(100, 50));
		
		leftPanel.add(BorderLayout.CENTER, conceal);
		leftPanel.add(BorderLayout.SOUTH, info1);
		leftPanel.setBorder(wholeBorder);
		
        c.gridx = 0;
        c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipady = 0;      //make this component tall
		c.ipadx = 0;
        pane.add(leftPanel, c);
		
		// !! MIDDLE !!
		
		middlePanel = new JPanel();
		middlePanel.setLayout(new BorderLayout());

		reveal.setToolTipText("Reveal a data file hidden inside an image.");
		reveal.setMnemonic('R');
		reveal.setPreferredSize(new Dimension(100, 50));
		
		info2.setText("Decode a data file hidden inside an image.");
		info2.setEditable(false);
		info2.setBackground(Color.BLACK);
		info2.setForeground(Color.ORANGE);
		info2.setPreferredSize(new Dimension(100, 50));
		
		middlePanel.add(BorderLayout.CENTER, reveal);
		middlePanel.add(BorderLayout.SOUTH, info2);
		middlePanel.setBorder(wholeBorder);
		
        c.gridx = 1;
        c.gridy = 1;
		c.ipady = 0; 
		c.ipadx = 0;
        pane.add(middlePanel, c);
		
		// !! RIGHT !!
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		help.setToolTipText("Learn about Revelation and Steganography.");
		help.setMnemonic('H');
		help.setPreferredSize(new Dimension(100, 50));
		
		info3.setText("Learn about this applicaiton and Steganography.");
		info3.setEditable(false);
		info3.setBackground(Color.BLACK);
		info3.setForeground(Color.ORANGE);
		info3.setPreferredSize(new Dimension(100, 50));
		
		rightPanel.add(BorderLayout.CENTER, help);
		rightPanel.add(BorderLayout.SOUTH, info3);
		rightPanel.setBorder(wholeBorder);
		
        c.gridx = 2;
        c.gridy = 1;
        c.ipady = 0;      //make this component tall
		c.ipadx = 0;
        pane.add(rightPanel, c);
		
		//the exit button +++++++++++++++++
		exit.setPreferredSize(new Dimension(112,26));
		exit.setMinimumSize(new Dimension(112,26));
		exit.setMnemonic('x');
        c.gridx = 2;     
        c.gridy = 2;  
        c.ipady = 0;       
		c.ipadx = 0;
        pane.add(exit, c);
    }

    /**
     * Create the GUI and show it.
     */
    private void createAndShowGUI() 
	{
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        frame = new JFrame("Revelation 2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set the icon
		URL url = ClassLoader.getSystemResource(PATH + "icon.gif");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		
		MyMenu myMenu = new MyMenu(frame);
		JMenuBar myMenuBar = myMenu.buildTheMenu();
		frame.setJMenuBar(myMenuBar);
		
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
                new Main();
            }
        });
    }
    
	/**
	 * Default constructor
	 */
	public Main()
	{
		System.setProperty("com.apple.macos.useScreenMenuBar","true");
		System.setProperty("apple.laf.useScreenMenuBar","true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name","Revelation");
		System.setProperty("com.apple.mrj.application.growbox.intrudes","false");
		System.setProperty("com.apple.mrj.application.live-resize","true");
		
		createAndShowGUI();	
		conceal.addActionListener(new concealButtonAction());
		exit.addActionListener(new exitButtonAction());
		help.addActionListener(new helpButtonAction());
		reveal.addActionListener(new revealButtonAction());
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
	 * Takes you to Step 1 of Conceal
	 * @author Sean Hamlin
	 */
	private class concealButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			new SelectDataFile(null).CreateGUI();
		}
	}
	
	/**
	 * Exits Revelation
	 * @author Sean Hamlin
	 */
	private class exitButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
		}
	}
	
	/**
	 * Pops-up the built in help
	 * @author Sean Hamlin
	 */
	private class helpButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			new HelpDialog(frame, "mainhelp.html", "Help with Revelation and Steganography");
		}
	}
	
	/**
	 * Takes you to Step 1 of Reveal
	 * @author Sean Hamlin
	 */
	private class revealButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			frame.dispose();
			new SelectStegoImage(null).CreateGUI();
		}
	}
}