package reportGenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.*;

public class TemplateMaker {
	//Deals with creating the templates for each monthly report.  Checks to see if the files exist and adds in data for
	//teacher name and dates in the right places.
	
	private String name;
	private String blankReportLoc;
	private String outputLoc;
	
	public TemplateMaker(Config configuration) {
		name = configuration.getTeacherEnglishName();
		blankReportLoc = configuration.getBlankReportLoc();
		outputLoc = configuration.getOutputLoc();
	}
	
	/**
	 * Checks to see if file (filename) exists.  Returns true or false
	 * @param filename
	 * @return boolean
	 */
	private boolean reportExists(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Creates the template for the monthly report.  First checks to see if the file exists for the given month, 
	 * and then creates it if it does not.  Will add in all date and school info.
	 * @param time
	 */
	public void makeMonthlyTemplate(LocalDate time) {
		String filename = this.outputLoc + time.getYear() + " " + time.getMonth().toString() + " report " + this.name + ".xlsx";
		Path target = Paths.get(filename);
		Path source = Paths.get(this.blankReportLoc);
		
		//System.out.println("target " + target);
		//System.out.println("source " + source);
		
		if (!reportExists(filename)) {
			try {
				Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//TODO: add in days(month and day) and school info
	}
	
	
}
