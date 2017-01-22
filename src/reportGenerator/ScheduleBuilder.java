package reportGenerator;

import java.io.*;
import java.time.*;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.*;

/**
 * Builds and adds in the schedule for a monthly report.
 * @author dtopal
 *
 */
public class ScheduleBuilder {
	
	private Config config;
	private YearlySchedule ys;
	private ClassSchedule cs;
	private TemplateMaker tm;
	
	public ScheduleBuilder(Config configuration) throws IOException, Exception {
		config = configuration;
		ys = new YearlySchedule(config);
		cs = new ClassSchedule(config);
		tm = new TemplateMaker(config);
	}
	
	/**
	 * adds the class schedule for a given date.
	 * @param day
	 * @param row
	 */
	public void addDayClasses(LocalDate day, XSSFRow row) {
		day = day.withDayOfMonth((int) row.getCell(0).getNumericCellValue());
		String week = this.ys.getWeeklySchedule(day);
		//System.out.println(week);
		HashMap<Integer, String> classes = this.cs.getDaySched(day,  week);
		Integer[] classPeriodIndex = {3, 5, 7, 9, 11, 13, 15}; //indexes of class periods in sheet

		if (!week.equals("")) {
			//input school name into cell after date, index 2
			XSSFCell school = row.getCell(2);
			school.setCellValue(config.getSchoolName());
			
			//input class date
			for (int period : classes.keySet()) {
				XSSFCell currentClass = row.getCell(classPeriodIndex[period - 1]);
				currentClass.setCellValue(classes.get(period));
			}
		}
	}
	
	/**
	 * Iterates over the rows in a sheet (starting at index 5) and adds
	 * class data to each day.
	 * @param day
	 * @param sheet
	 */
	public void addClasses(LocalDate day, XSSFSheet sheet) {
		int numDays = day.getMonth().maxLength();
		int dayIndex = 5; //index of where day info starts in sheet
		for (int i = 1; i <= numDays; i++) {
			XSSFRow currentRow = sheet.getRow(dayIndex);
			addDayClasses(day, currentRow);
			dayIndex += 2;
		}
	}
	
	/**
	 * Builds the monthly schedule for a given month.  Creates the excel file if necessary.
	 * Will write in all the appropriate class data.
	 * @param month
	 * @throws IOException
	 */
	public void buildMonth(int month) throws IOException {
		LocalDate time = LocalDate.now().withMonth(month);
		
		//REMOVE THIS LATER!!!!!!!!
		time = time.withYear(2016);
		
		String filename = this.tm.makeMonthlyTemplate(time);
		
		//open as workbook to prepare for data
		File file = new File(filename);
		FileInputStream fr = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fr);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		//put in data
		addClasses(time, sheet);
		
		//write data with an output stream
		FileOutputStream fileOut = new FileOutputStream(filename);
		workbook.write(fileOut);
		workbook.close();
		fileOut.close();
		
		
	}
}
