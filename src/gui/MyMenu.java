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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class MyMenu {
	
	private JFrame myFrame;
	
	//default accelerator CTRL or Command
	//Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
	
	/**
	 * Default constructor
	 */
	public MyMenu(JFrame myFrame) {
		this.myFrame = myFrame;
	}

	/**
	 * Builds the menu, depends on host machine to what
	 * menu is built
	 * @return the Menu to add to the frame
	 */
	public JMenuBar buildTheMenu() {
		
		Color myGreen = new Color(16,119,27);
		Color myGold = new Color(252,188,56);
		
		JMenuBar menuBar = null;
		JMenu menu = null;
		JMenuItem menuItem = null;
		
		String osName = System.getProperty("os.name");
		
		//MAC SPECIFIC MENU ++++++++++++++++++++++++++++++++++++++++++++++++
		if (osName.substring(0,3).toUpperCase().equals("MAC")) {
			//System.setProperty("com.apple.macos.useScreenMenuBar","true");
			//System.setProperty("com.apple.mrj.application.apple.menu.about.name","Revelation");
			//System.setProperty("com.apple.mrj.application.growbox.intrudes","false");
			//System.setProperty("com.apple.mrj.application.live-resize","true");
			
			//		Create the menu bar.
			menuBar = new JMenuBar();
	
			//		Build the first menu.
			menu = new JMenu("File");
			menu.setMnemonic(KeyEvent.VK_F);
			menu.getAccessibleContext().setAccessibleDescription("The File Menu");
			menuBar.add(menu);
	
			//		now add JMenuItem
			menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.getAccessibleContext().setAccessibleDescription("Exits Revelation");
			menuItem.addActionListener(new closeButtonAction());
			menu.add(menuItem);
	
			//		Build second menu in the menu bar.
			menu = new JMenu("Help");
			menu.setMnemonic(KeyEvent.VK_H);
			menu.getAccessibleContext().setAccessibleDescription("The Help Menu");
			menuBar.add(menu);
			
			//		now add JMenuItem
			menuItem = new JMenuItem("About", KeyEvent.VK_A);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.getAccessibleContext().setAccessibleDescription("About Revelation");
			menuItem.addActionListener(new aboutButtonAction());
			menu.add(menuItem);
		}
		//WINDOWS SPECIFIC MENU ++++++++++++++++++++++++++++++++++++++++++++++++
		else {
			//		Create the menu bar.
			menuBar = new JMenuBar();
			menuBar.setBackground(Color.BLACK);
			menuBar.setBorder(BorderFactory.createEmptyBorder());
			//menuBar.setFont()
			
			UIManager.put("Menu.selectionBackground", myGreen);
			UIManager.put("MenuItem.selectionBackground", myGreen);
	
			//		Build the first menu.
			menu = new JMenu("File");
			menu.setMnemonic(KeyEvent.VK_F);
			menu.setForeground(myGold);
			menu.setBorder(BorderFactory.createEmptyBorder());
			menu.getAccessibleContext().setAccessibleDescription("The File Menu");
			menuBar.add(menu);
	
			//		now add JMenuItem
			menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
			menuItem.setForeground(myGold);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.getAccessibleContext().setAccessibleDescription("Exits Revelation");
			menuItem.addActionListener(new closeButtonAction());
			menu.add(menuItem);
	
			//		Build second menu in the menu bar.
			menu = new JMenu("Help");
			menu.setMnemonic(KeyEvent.VK_H);
			menu.setForeground(myGold);
			menu.setBorder(BorderFactory.createEmptyBorder());
			menu.getAccessibleContext().setAccessibleDescription("The Help Menu");
			menuBar.add(menu);
			
			//		now add JMenuItem
			menuItem = new JMenuItem("About", KeyEvent.VK_A);
			menuItem.setForeground(myGold);
			menuItem.setBorder(BorderFactory.createEmptyBorder());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem.getAccessibleContext().setAccessibleDescription("About Revelation");
			menuItem.addActionListener(new aboutButtonAction());
			menu.add(menuItem);
		}
		
		return menuBar;
	}
	
	/**
	 * Inner class that simply closes the dialog window
	 * @author Sean Hamlin
	 */
	private class closeButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			myFrame.dispose();
		}
	}
	
	/**
	 * Inner class that simply pops up a about dialog window
	 * @author Sean Hamlin
	 */
	private class aboutButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			new HelpDialog(myFrame, "about.html", "About Revelation");
		}
	}
}
