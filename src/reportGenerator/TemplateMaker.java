package reportGenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.*;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author dtopal
 *
 */
public class TemplateMaker {
	//Deals with creating the templates for each monthly report.  Checks to see if the files exist and adds in data for
	//teacher name and dates in the right places.
	
	private String name;
	private String blankReportLoc;
	private String outputLoc;
	private ClassSchedule classSched;
	
	public TemplateMaker(Config configuration) throws Exception {
		name = configuration.getTeacherEnglishName();
		blankReportLoc = configuration.getBlankReportLoc();
		outputLoc = configuration.getOutputLoc();
		classSched = new ClassSchedule(configuration);
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
	 * @throws IOException 
	 */
	public void makeMonthlyTemplate(LocalDate time) throws IOException {
		String filename = this.outputLoc + time.getYear() + " " + time.getMonth().toString() + " report " + this.name + ".xlsx";
		Path target = Paths.get(filename);
		Path source = Paths.get(this.blankReportLoc);
		
		System.out.println("target " + target);
		System.out.println("source " + source);
		
		if (!reportExists(filename)) {
			System.out.println("target doesn't exit");
			try {
				Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);
				System.out.println("Copied!");
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//TODO: add in days(month and day) and school info
		
		//open file as a workbook
		File file = new File(filename);
		FileInputStream fr = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fr);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		//fill in month data
		setMonthData(time, sheet);
		
		//fill in class data  (get each day's week schedule and then the classes for that day
		
		
		//close the file
		//workbook.close();
		
		FileOutputStream fileOut = new FileOutputStream(filename);
		workbook.write(fileOut);
		workbook.close();
		fileOut.close();
	}
	
	/**
	 * Adds in monthly data such as dates
	 * @param time
	 * @param workbook
	 */
	public void setMonthData(LocalDate time, XSSFSheet sheet) {
		Month month = time.getMonth();
		int numDays = month.maxLength();
		
		
		int dayIndex = 5; //where day info starts in workbook
		for (int i = 1; i <= numDays; i++) {
			XSSFRow currentRow = sheet.getRow(dayIndex);
			dayIndex += 2;
			//put in date
			XSSFCell currentDate = currentRow.getCell(0);
			currentDate.setCellValue(i);
			//put in day in Japanese
			XSSFCell currentWeekday = currentRow.getCell(1);
			time = time.withDayOfMonth(i);
			currentWeekday.setCellValue(classSched.getDayOfWeek(time));
			
			
		}
	}
	
	
}
