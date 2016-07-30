package reportGenerator;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

public class ClassSchedule {
	private String scheduleLoc;
	private XSSFSheet fullScheduleSheet;
	private String name;
	//private String scheduleSheetName = "2016年度0427確定版"; //from the source file  CHANGE THIS IF FILE CHANGES
	
	public ClassSchedule(Config configuration) throws Exception {
		scheduleLoc = configuration.getClassScheduleLoc();
		fullScheduleSheet = buildSched();
		name = configuration.getTeacherJapaneseName();
	}
	
	private XSSFSheet buildSched() throws Exception {
		//opens the source file and clones the sheet with the schedule.  Stores it as a XSSFSheet object.
		File file = new File(this.scheduleLoc);
		FileInputStream fr = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fr);
		//int sheetIndex = workbook.getSheetIndex(scheduleSheetName);
		//XSSFSheet clone = workbook.cloneSheet(sheetIndex);
		XSSFSheet clone = workbook.cloneSheet(0);
		workbook.close();
		return clone;
	}
	
	public HashMap<Integer, String> getDaySched(LocalDate date, String week) {
		
		//takes a given day (LocalDate object) and the week schedule (String of As, Ag, or B)
		//and returns a HashMap of the classes for that day mapped to the period they occur.
		
		HashMap<Integer, String> daySched = new HashMap<Integer, String>();  //class period (int) to class (String)
		int teacherIndex = findTeacherClasses(); //row index of classes associated with the teacher
		XSSFRow teacherRow = this.fullScheduleSheet.getRow(teacherIndex); //row of classes for teacher
		
		int weeklyIndex = findWeeklyClasses(week); //starting column index of that week schedule.  if this is 0, last index of that week is 33
		XSSFRow dayRow = this.fullScheduleSheet.getRow(2);
		
		String dayOfWeek = getDayOfWeek(date); // returns day of week in Japanese
//		System.out.println(dayOfWeek);
//		System.out.println("weekly index " + weeklyIndex);
//		System.out.println(this.name);
//		System.out.println("teacher index " + teacherIndex);
		
		for (int i = (weeklyIndex + 2); i <= weeklyIndex + 33; i++) {
			XSSFCell currentCell = dayRow.getCell(i);
			
			if (currentCell != null && currentCell.getCellType() == 1 && currentCell.getStringCellValue().equals(dayOfWeek)) {
//				System.out.println("cell index " + i + " current cell " + currentCell.getStringCellValue());
				XSSFCell teacherCell = teacherRow.getCell(i);
				if (teacherCell != null && teacherCell.getCellType() == 1 && !teacherCell.getStringCellValue().equals("")) {
					int classPeriod = Integer.parseInt(this.fullScheduleSheet.getRow(3).getCell(i).getStringCellValue());
					String classInSched = teacherCell.getStringCellValue();
					
//					System.out.println(classPeriod + " " + classInSched);
					
					daySched.put(classPeriod, classInSched);
				}
			}
		}
		
		return daySched;
	}
	
	public String getDayOfWeek(LocalDate date) {
		int dayIndex = date.getDayOfWeek().getValue() - 1;
		String[] jpDays = {"月", "火", "水", "木", "金", "土", "日"};
		return jpDays[dayIndex];
	}
	
	private int findWeeklyClasses(String week) {
		int weeklyIndex = -1;
		XSSFRow row = this.fullScheduleSheet.getRow(2);
		for (Cell cell : row) {
			if (cell.getCellType() == 1 && cell.getStringCellValue().equals(week)) {
				weeklyIndex = cell.getColumnIndex();
			}
		}
		return weeklyIndex;
	}
	
	private int findTeacherClasses() {
		int teacherIndex = -1;
		for (Row row : this.fullScheduleSheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == 1 && cell.getStringCellValue().equals(this.name)) {
					teacherIndex = cell.getRowIndex();
				}
			}
		}	
		return teacherIndex;
	}
}
