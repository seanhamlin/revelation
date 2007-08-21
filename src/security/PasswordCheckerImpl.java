 /* 
  * Copyright (c) 2007 Sean Hamlin
  * All Rights Reserved.
  * 
  * http://revelation.atspace.biz
  * 
  * This source is provided free of charge
  * as long as you reference me, and my
  * website. Thanks
  * 
  * Many thanks to Code and Coffee for the inspiration for the password strength generator
  * this can be found here : http://www.codeandcoffee.com/2007/07/16/how-to-make-a-password-strength-meter-like-google-v20/
  */

package security;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PasswordCheckerImpl implements PasswordChecker {

	JPasswordField passwordString1;
	JTextField strengthColor;
	
	/**
	 * Set up the dictionary
	 */
	public void init() {

	}

	/**
	 * Sets up class references
	 */
	public void setUpReferences(JPasswordField passwordString1,	JTextField strengthColor) {
		this.passwordString1 = passwordString1;
		this.strengthColor = strengthColor;
	}

	
	/**
	 * Updates the Password Strength meter on the fly based on:
	 *
	 * 		Password Length:
	 *	        5 Points: Less than 4 characters
	 *	        10 Points: 5 to 7 characters
	 *	        25 Points: 8 or more
	 *	
	 *	    Letters:
	 *	        0 Points: No letters
	 *	        10 Points: Letters are all lower case OR all upper case
	 *	        20 Points: Letters are upper case and lower case
	 *	
	 *	    Numbers:
	 *	        0 Points: No numbers
	 *	        10 Points: 1 or 2 numbers
	 *	        20 Points: 3 or more numbers
	 *	
	 *	    Characters:
	 *	        0 Points: No characters
	 *	        10 Points: 1 character
	 *	        25 Points: More than 1 character
	 *	
	 *	    Bonus:
	 *	        2 Points: Letters and numbers
	 *	        3 Points: Letters, numbers, and characters
	 *	        5 Points: Mixed case letters, numbers, and characters
	 *	
	 *		Password strength is measure by the percent of the above:
	 *		    >= 90: Very Secure
	 *		    >= 80: Secure
	 *		    >= 70: Very Strong
	 *	 	    >= 60: Strong
	 *		    >= 50: Average
	 *		    >= 25: Weak
	 *		    >= 0: Very Weak
	 *
	 * @author Sean Hamlin
	 * @since 2.0
	 * @see http://www.scism.sbu.ac.uk/jfl/Appa/appa4.html
	 */
	public void checkPassword() {		
		char[] pw = passwordString1.getPassword();
		int score = 0;
		String name = "";
		Color background = Color.BLACK;
		int numberOfLowerLetters = 0;
		int numberOfUpperLetters = 0;
		int numberOfNumbers = 0;
		int numberOfCharacters = 0;
		
		if (pw.length == 0) {
			strengthColor.setText(" ");
			strengthColor.setBackground(Color.BLACK);
			return;
		}
		
		for (int i = 0 ; i < pw.length ; i++ ) {
			if (Character.isLowerCase(pw[i])) { 
				numberOfLowerLetters++;
			} else if (Character.isUpperCase(pw[i])) { 
				numberOfUpperLetters++;
			} else if (Character.isDigit(pw[i])){
				numberOfNumbers++;
			} else if (pw[i] >= 32 && pw[i] <= 126) { 
				numberOfCharacters++;
			}
		}
		
		//debug ++++++++++++++++++++++++++++
		/*System.out.print("pw : ");
		for (int i = 0 ; i < pw.length ; i++ ) {
			System.out.print(pw[i]); 
		}
		System.out.println("\numberOfLowerLetters : " + numberOfLowerLetters);
		System.out.println("\numberOfUpperLetters : " + numberOfUpperLetters);
		System.out.println("numberOfNumbers : " + numberOfNumbers);
		System.out.println("numberOfCharacters : " + numberOfCharacters + "\n");
		*/
		
		//Length
		if (pw.length > 0 && pw.length <= 5) {
			score += 5;
		} else if (pw.length >= 6 && pw.length <= 10) {
			score += 10;
		} else if (pw.length >= 11) {
			score += 25;
		}
		
		//Letters
		if (numberOfLowerLetters == 0 && numberOfUpperLetters == 0) {
			score += 0;
		} else if (numberOfLowerLetters == 0 || numberOfUpperLetters == 0) {
			score += 10;
		} else {
			score += 20;
		}
		
		//Numbers
		if (numberOfNumbers > 0 && numberOfNumbers <= 2) {
			score += 10;
		} else if (numberOfNumbers >= 3) {
			score += 20;
		}
		
		//Characters
		if (numberOfCharacters == 1) {
			score += 10;
		} else if (numberOfCharacters >= 2) {
			score += 25;
		}
		
		//Bonus
		if ((numberOfLowerLetters > 0 || numberOfUpperLetters > 0) && numberOfNumbers > 0) {
			score += 2;
		}
		if ((numberOfLowerLetters > 0 || numberOfUpperLetters > 0) && numberOfNumbers > 0 && numberOfCharacters > 0) {
			score += 3;
		}
		if (numberOfLowerLetters > 0 && numberOfUpperLetters > 0 && numberOfNumbers > 0 && numberOfCharacters > 0) {
			score += 5;
		}
		
		if (score < 25) {
			name = "Very Weak";
			background = Color.RED;
		} else if (score < 50) {
			name = "Weak";
			background = Color.ORANGE;
		} else if (score < 60) {
			name = "Average";
			background = Color.YELLOW;
		} else if (score < 70) {
			name = "Strong";
			background = Color.CYAN;
		} else if (score < 80) {
			name = "Very Strong";
			background = Color.BLUE;
		} else if (score < 90) {
			name = "Secure";
			background = Color.MAGENTA;
		} else if (score >= 90) {
			name = "Very Secure";
			background = Color.GREEN;
		}
		
		strengthColor.setText(name + " - score: " + score);
		strengthColor.setBackground(background);
		
		//clear the char [] so it does not linger in memory
		Arrays.fill(pw,'0');
	}
}
