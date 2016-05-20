package reportGenerator;
/**
 * YearlySchedule is a class that contains the data for the yearly schedule.
 * Use to find what week schedule it is or what is happening on a given date.
 * 
 * checks the conf.txt file for location of yearly schedule .xls file.
 * 
 * Double check to see what format the schedule file is and use the appropriate class from the apache poi library
 * xlsx uses the XSSF class
 * xls uses the HSSF class
 * 
 * @author dtopal
 *
 */

import java.io.*;
import java.time.*;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

public class YearlySchedule {
	private String scheduleLoc;
	private String currentWeekSchedule;
	private HSSFSheet yearlyScheduleSheet;
	
	public YearlySchedule(Config configuration) throws IOException, Exception {
		scheduleLoc = configuration.getYearlyScheduleLoc();
		yearlyScheduleSheet = buildYearlySchedule();		
		//System.out.println(yearlyScheduleSheet.toString());		
		currentWeekSchedule = findCurrentWeekSchedule(LocalDate.now());
	}
	
	private HSSFSheet buildYearlySchedule() throws Exception, IOException, UnsupportedEncodingException  {
		//clones the HSSFSheet from the yearly schedule .xls file designated in conf.txt  Returns a HSSFSheet object.
		
		File file = new File(this.scheduleLoc);
		//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
		FileInputStream fr = new FileInputStream(file);
		HSSFWorkbook workbook = new HSSFWorkbook(fr);		
		HSSFSheet clone = workbook.cloneSheet(0);
		workbook.close();
		return clone;
	}
	
	private String findCurrentWeekSchedule(LocalDate givenDay){
		//Finds the weekly schedule for the current day.  Returns a String for either As, Ag, B, or an empty string for a day with no classes.

		//LocalDate currentTime= LocalDate.now();
		//int currentMonth = currentTime.getMonthValue(); //int from 1 to 12
		String month = monthIntToJapaneseString(givenDay.getMonthValue());
		int day = givenDay.getDayOfMonth(); //int from 1 to 31
		//int day = 27;
		
		//System.out.println(month + day);
		
		String weeklySched = null;
		
		HSSFRow months = this.yearlyScheduleSheet.getRow(2);
		int monthIndex = -1;
		for (Cell cell : months) {
			if (cell.getStringCellValue().contains(month)) {
				monthIndex = cell.getColumnIndex();
				break;
			}
		}
		//System.out.println("Month Index: " + monthIndex);
		for (int i = 2; i < this.yearlyScheduleSheet.getLastRowNum(); i++) {
			HSSFRow currentRow = this.yearlyScheduleSheet.getRow(i);
			if (currentRow.getCell(monthIndex).getCellType() == 0 && currentRow.getCell(monthIndex).getNumericCellValue() == day) {
				//System.out.println(currentRow.getCell(monthIndex + 1).getStringCellValue());
				weeklySched = currentRow.getCell(monthIndex + 1).getStringCellValue();
				break;
			}
		}
		//System.out.println(weeklySched);
		return weeklyScheduleTranslate(weeklySched);
	}
	
	private String monthIntToJapaneseString(int month) {
		//Takes the month (int from 1 to 12) and returns the corresponding month value in Japanese unicode as a String.
		String[] jMonths = {"１","２","３","４","５","６","７","８","９","１０","１１","１２"};
		HashMap<Integer, String> months = new HashMap<Integer, String>();
		for (int i = 0; i < jMonths.length; i++) {
			months.put(i + 1, jMonths[i]);
		}
		return months.get(month);
	}
	
	private String weeklyScheduleTranslate(String weekSched) {
		String[] schedNameOne = {"S", "G", "B", ""};
		String[] schedNameTwo = {"As", "Ag", "B", ""};
		HashMap<String, String> names = new HashMap<String, String>();
		for (int i = 0; i < schedNameOne.length; i++) {
			names.put(schedNameOne[i], schedNameTwo[i]);
		}
		return names.get(weekSched);
	}
	
	public String getCurrentWeeklySchedule() {
		return this.currentWeekSchedule;
	}
	
	public String getWeeklySchedule(int month, int day) {
		LocalDate givenDay = LocalDate.now();
		givenDay = givenDay.withDayOfMonth(day);
		givenDay = givenDay.withMonth(month);
		return findCurrentWeekSchedule(givenDay);
	}
}
