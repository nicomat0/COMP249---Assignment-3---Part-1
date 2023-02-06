import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

/**
 * Mateo Nieto (40192918) and Syed Ayaan Jilani (40209519)
 * COMP249
 * Assignment 3 Part I
 * 7th October 2022
 */

/**
 * @author Syed Ayaan Jilani (40209519) and Mateo Nieto (40192918)
 *
 */
public class BibliographyFactory {
	
	/**
	 * This method takes in all the three output file objects along with the input latex.bib file in the scanner object.
	 * The method checks for the validity of the input file. If any blank/empty field is found, all the associated output
	 * files are deleted and the user is informed about the details and why the file was invalid.
	 * 
	 * @param sc input Latex file object
	 * @param ieeeF output IEEE file object
	 * @param acmF output ACM file object
	 * @param njF output NJ file object
	 * @param index index of the Latex and the corresponding output files
	 * 
	 * @return boolean valid(true) or invalid(false)
	 */
	public static boolean processFilesForValidation(Scanner sc, PrintWriter ieeeF, PrintWriter acmF, PrintWriter njF, int index) {
		
		StringTokenizer content = null;
		
		String nextLine = null;
		String field = null;
		String acmAuthor = null;
		
		String author = null;
		String journal = null;
		String title = null;
		String year = null;
		String volume = null;
		String number = null;
		String pages = null;
		String keywords = null;
		String doi = null;
		String ISSN = null;
		String month = null;
		
		while (sc.hasNextLine()) {
			
			try {
			
			if (sc.nextLine().contains("ARTICLE")) {
				
				while(true) {
					
					nextLine = sc.nextLine();
					
					if (nextLine.equals("}")) {
						break;
					}
					
					if (nextLine.equals("") || !nextLine.contains("={")) {
						continue;
					}
					
					content = new StringTokenizer(nextLine,"={},");
					
					field = content.nextToken();
				
					if (nextLine.contains("={},")) {
						throw new FileInvalidException("Error: Detected Empty Field!\n"
							+ "============================\n\nProblem detected with input file: Latex"+(index+1)+".bib\n"
							+ "File is invalid: Field \""+field+"\" is empty. Processing stopped at this point. Other empty fields may exist.\n");
					}
					
					if (field.equals("author")) {
						author = content.nextToken();
					}
					else if (field.equals("journal")){
						journal = content.nextToken();
					}
					else if (field.equals("title")){
						title = content.nextToken();
					}
					else if (field.equals("year")){
						year = content.nextToken();
					}
					else if (field.equals("volume")){
						volume = content.nextToken();
					}
					else if (field.equals("number")){
						number = content.nextToken();
					}
					else if (field.equals("pages")){
						pages = content.nextToken();
					}
					else if (field.equals("keywords")){
						keywords = content.nextToken();
					}
					else if (field.equals("doi")){
						doi = "https://doi.org/" + content.nextToken();
					}
					else if (field.equals("ISSN")){
						ISSN = content.nextToken();
					}
					else if (field.equals("month")){
						month = content.nextToken();
					}
					
				}
				
				if (author.contains("and")) {
					acmAuthor = author.substring(0,author.indexOf("and"));
				}
				else {
					acmAuthor = author + " ";
				}
				
				ieeeF.println(author+". \""+title+"\", "+journal+", vol. "+volume+", no. "+number+", p. "+pages+", "+month+" "+year+".\n");
				acmF.println("[" +(index+1)+"]" + "\t" + acmAuthor+"et al. "+year+". "+title+". "+journal+". "+volume+", "+number+" ("+year+"), "+pages+". DOI:"+doi+".\n");
				njF.println(author.replace("and", "&")+". "+title+". "+journal+". "+volume+", "+pages+"("+year+").\n");
				
			}	
			}
				
			catch(FileInvalidException e) {
				System.out.println(e.getMessage());
				ieeeF.close();
				acmF.close();
				njF.close();
				File f1 = new File("IEEE"+(index+1)+".json");
				f1.delete();
				File f2 = new File("ACM"+(index+1)+".json");
				f2.delete();
				File f3 = new File("NJ"+(index+1)+".json");
				f3.delete();
				return false;
			}
			
			}
		
		ieeeF.close();
		acmF.close();
		njF.close();
		return true;
	}
	
	/**
	 *This method is called when any input Latex file does not exist then all the output files created need to be deleted.
	 *
	 *@param ieeeF array of IEEE file objects
	 *@param acmF array of ACM file objects
	 *@param njF array of NJ file objects
	 */
	public static void deleteOutputFiles(PrintWriter[] ieeeF, PrintWriter[] acmF, PrintWriter[] njF) {
		
		for (int i = 0; i < 10; i++) {
			
			if (ieeeF[i]!=null) {
				File f = new File("IEEE"+(i+1)+".json");
				f.delete();
			}
			if (acmF[i]!=null) {
				File f = new File("ACM"+(i+1)+".json");
				f.delete();
			}
			if (njF[i]!=null) {
				File f = new File("NJ"+(i+1)+".json");
				f.delete();
			}
		
		}
		
	}
	
	/**
	 * This methods reads the content from a file using BufferedReader and displays it.
	 * 
	 * @param br file object of the file to be read/displayed
	 * @param fName name of file to be read/displayed
	 * 
	 * @throws IOException might occur while reading contents of file
	 */
	public static void readContentsOfFile(BufferedReader br, String fName) throws IOException {
		
		System.out.println("\n==================CONTENTS OF " + fName + "==================\n");
		
		int x;
		x = br.read();
		
		while(x != -1) {
			System.out.print((char)x);
			x = br.read();		
		}
		
		System.out.println("====================END OF " + fName + "=====================\n");
		
		br.close();
	}

	/**
	 * @param args an array of sequence of characters (Strings)
	 */
	public static void main(String[] args) {
		
		System.out.println("WELCOME TO BIBLIOGRAPHY FACTORY!\n");
		
		int invalid = 0;
		Scanner sc = null;
		
		PrintWriter[] ieeeF = new PrintWriter[10];
		PrintWriter[] acmF = new PrintWriter[10];
		PrintWriter[] njF = new PrintWriter[10];
		
		for (int i = 0; i < 10; i++) {
			
			try {
				sc = new Scanner(new FileInputStream("Latex" + (i+1) + ".bib"));
			}
			catch(FileNotFoundException e) {
				System.out.println("Could not open input file Latex" + i + ".bib for reading.\n\n" + 
			    "Please check if file exists! Program will terminate after closing any opened files.\n");
				System.exit(0);
			}
			
			try {
				ieeeF[i] = new PrintWriter(new FileOutputStream("IEEE" + (i+1) + ".json"));
			}
			catch(FileNotFoundException e) {
				System.out.println("Could not create IEEE file for input file Latex" + (i+1) + ".\n\n" + 
					    "Program will terminate after closing any opened files and deleting all the other output files.\n");
				if (sc!=null) {sc.close();}
				deleteOutputFiles(ieeeF,acmF,njF);
				System.exit(0);
			}
			
			try {
				acmF[i] = new PrintWriter(new FileOutputStream("ACM" + (i+1) + ".json"));
			}
			catch(FileNotFoundException e) {
				System.out.println("Could not create ACM file for input file Latex" + (i+1) + ".\n\n" + 
					    "Program will terminate after closing any opened files and deleting all the other output files.\n");
				if (sc!=null) {sc.close();}
				if (ieeeF[i]!=null) {ieeeF[i].close();}
				deleteOutputFiles(ieeeF,acmF,njF);
				System.exit(0);
			}
			
			try {
				njF[i] = new PrintWriter(new FileOutputStream("NJ" + (i+1) + ".json"));
			}
			catch(FileNotFoundException e) {
				System.out.println("Could not create NJ file for input file Latex" + (i+1) + ".\n\n" + 
			            "Program will terminate after closing any opened files and deleting all the other output files.\n");
				if (sc!=null) {sc.close();}
				if (ieeeF[i]!=null) {ieeeF[i].close();}
				if (acmF[i]!=null) {acmF[i].close();}
				deleteOutputFiles(ieeeF,acmF,njF);
				System.exit(0);
			}
			
			if (!processFilesForValidation(sc, ieeeF[i], acmF[i], njF[i], i)) {
				invalid++;
			}
				
			sc.close();
			
		}
		
		System.out.println("A total of "+invalid+" files were invalid, and could not be processed. " + 
		                   "All other "+(10-invalid)+" \"Valid\" files have been created.");
		
		System.out.print("\nPlease enter the name of one of the files that you need to review: ");
		
		Scanner input = new Scanner(System.in);
		String fName = input.next();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(fName));
			readContentsOfFile(br,fName);
		}
		
		catch(FileNotFoundException e1) {
			
			System.out.println("Could not open input file. File does not exist; possibly it could not be created!\n\n" + 
			                   "However, you will be allowed another chance to enter another file name.\n");
			System.out.print("Please enter the name of one of the files that you need to review: ");
			
			fName = input.next();
			
			try {
				br = new BufferedReader(new FileReader(fName));
				readContentsOfFile(br,fName);
			}
			
			catch(FileNotFoundException e3) {
				
				System.out.println("\n\nCould not open input file again! Either file does not exist or could not be created.\n" + 
				                   "Sorry! I am unable to display your desired files! Program will exit!");
				input.close();
				System.exit(0);
			}
			
			catch(IOException e4) {
				
				System.out.println("\n\nError occoured while attempting to read from the input file!\n" + 
				                   "Sorry! I am unable to display your desired files! Program will exit!");
				input.close();
				System.exit(0);
			}
		}
		
		catch(IOException e2) {
			
			System.out.println("Error occoured while attempting to read from the input file!\n\n" + 
	                   "However, you will be allowed another chance to enter another file name.\n");
	        System.out.print("Please enter the name of one of the files that you need to review: ");
	
	        fName = input.next();
	        
	        try {
	        	br = new BufferedReader(new FileReader(fName));
	        	readContentsOfFile(br,fName);
	        }
	        
	        catch(FileNotFoundException e5) {
				
				System.out.println("\n\nCould not open input file again! Either file does not exist or could not be created.\n" + 
				                   "Sorry! I am unable to display your desired files! Program will exit!");
				input.close();
				System.exit(0);
			}
	        
	        catch(IOException e6) {
		
	        	System.out.println("\n\nError occoured while attempting to read from the input file again.\n" + 
		                   "Sorry! I am unable to display your desired files! Program will exit!");
	        	input.close();
	        	System.exit(0);
	        }
		}
		
		input.close();
		System.out.println("\nTHANKYOU FOR USING BIBLIOGRAPHY FACTORY ");
		
	}

}
