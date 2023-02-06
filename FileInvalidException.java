/**
 * Mateo Nieto ; 40192918
 * Syed Ayaan Jilani ; 40209519
 * COMP249
 * Assignment 3 Part I
 * 7th October 2022
 */

/**
 * An exception class that extends Exception which displays an error message when a file has an empty tag. 
 * 
 * @author Syed Ayaan Jilani (40209519) and Matteo Nietto (40192918)
 */
public class FileInvalidException extends Exception {
	
	/**
	 * Default constructor that has a default error message
	 */
	public FileInvalidException () {
		super("Error: Input file cannot be parsed due to missing information (i.e. month={}, title={}, etc.)");
	}
	
	/**
	 * Parameterized constructor that takes the error message to be displayed as a parameter
	 * 
	 * @param errorMessageToBeDisplayed the error message passed
	 */
	public FileInvalidException (String errorMessageToBeDisplayed) {
		super(errorMessageToBeDisplayed);
	}
	
	/**
	 * Method that returns the error message
	 * 
	 * @return String The error message
	 */
	public String getMessage(){
		return super.getMessage();
	}

}
