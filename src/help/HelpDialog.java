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
 
package help;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * The Help Dialog class. The class is responsible for
 * displaying all popup dialogs in Revelation
 * @author Sean Hamlin
 * @version 1.1
 * @since 1.0
 */
public class HelpDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String PATH = "data/help/";
	private String theHelpHtml;
	private JFrame parent;
	private JEditorPane editorPane;
		
	/**
	 * Adds all components to the dialog
	 * @param cp the Content Pane of the dialog
	 */
	private void addComponentsToPane(Container cp) {
		editorPane = new JEditorPane();
		JButton ok = new JButton();
				
		cp.setLayout(new GridBagLayout());
		cp.setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();

		editorPane.setEditable(false);
		editorPane.setBorder(BorderFactory.createEmptyBorder());
		editorPane.addHyperlinkListener(new Hyperactive());
		URL helpURL = ClassLoader.getSystemResource(PATH + theHelpHtml);
		if (helpURL != null) {
		    try {
		        editorPane.setPage(helpURL);
		    } catch (IOException e) {
				JOptionPane.showMessageDialog(parent  , "Attempted to read a bad URL : " + helpURL, "Error", JOptionPane.ERROR_MESSAGE);
		    }
		} else {
			JOptionPane.showMessageDialog(parent  , "Couldn't find file : " + helpURL, "Error", JOptionPane.ERROR_MESSAGE);
		}

		//Put the editor pane in a scroll pane.
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setPreferredSize(new Dimension(500, 300));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
        c.weightx = 1.0;
		c.weighty = 1.0; //does grow vertically
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(5,5,5,5);
        cp.add(editorScrollPane, c);
		
		ok.setText("OK");
		ok.setMnemonic('O');
		ok.addActionListener(new closeButtonAction());
        c.weightx = 0.0;
		c.weighty = 0.0; //does grow vertically
		c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(5,5,5,5);
        cp.add(ok, c);
	}

	/**
	 * Constructor for HelpDialog
	 * @param parent the JFrame parent, from which is invoking the dialog
	 * @param theHelpHtml the HTML file to display
	 * @param title the title of the new dialog being created
	 */
	public HelpDialog(final JFrame parent, String theHelpHtml, String title) {
	    super(parent, title, true);
		this.parent = parent;
		this.theHelpHtml = theHelpHtml;
		
		addComponentsToPane(getContentPane());
		
	    //setSize(300, 250);
		pack();
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2,
                (d.height - getSize().height) / 2);
		setVisible(true);
	}
	
	/**
	 * Inner class that simply closes the dialog window
	 * @author Sean Hamlin
	 */
	private class closeButtonAction implements ActionListener	{
		public void actionPerformed(ActionEvent arg0) {
			dispose();
		}
	}
	
	/**
	 * Inner class that listens for hyperlinks being clicked
	 * and actions them in the default browser
	 * @author Sean Hamlin
	 */
	private class Hyperactive implements HyperlinkListener {
		 
        public void hyperlinkUpdate(HyperlinkEvent e) {
	          if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				  
				  //System.out.println("link : " + e.getURL());
				  
				  String osName = System.getProperty("os.name");
					//System.out.println ( System.getProperty("os.name") );
		
					//windows specific
					if (osName.substring(0,6).toUpperCase().equals("WINDOW")) {
						try {
							Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + e.getURL());
						} catch (Exception ex) {
							System.out.println("Unable to open : " + e.getURL());
						}
					}
					//mac specific
					else if (osName.substring(0,3).toUpperCase().equals("MAC")){
						try {
							Runtime.getRuntime().exec("open " + e.getURL());
						} catch (Exception ex) {
							System.out.println("Unable to open : " + e.getURL());
						}
					}
					else {
						JOptionPane.showMessageDialog(parent  , "Windows and Mac Link only, " + osName + " is not supported !", "Information", JOptionPane.ERROR_MESSAGE);
					}
	          }
        }
    }
}
