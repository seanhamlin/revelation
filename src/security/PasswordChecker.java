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

package security;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author Sean Hamlin
 * @since 2.0
 *
 */
public interface PasswordChecker {
  
	/**
	 * Sets up the references to the GUI
	 * @param passwordString1 the first password field
	 * @param strengthColor the field to alter to display the password strength
	 */
	public void setUpReferences(JPasswordField passwordString1, JTextField strengthColor);
	
    /**
     * Initialises the Dictionary
     */
	public void init();
	
    /**
     * Checks the password
     */
	public void checkPassword();
}
