package readingInfo;

import org.apache.poi.xssf.usermodel.*;
import java.io.*;

public class ReadingWorkbook {

	public static void main(String args[]) throws Exception {
		File file = new File("data/test.xlsx");
		FileInputStream fileInput = new FileInputStream(file);
		
		XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
		if(file.isFile() && file.exists()) {
			System.out.println("test.xlsx file open successfully.");
		}
		else {
			System.out.println("Error to open test.xlsx file.");
		}
		
		System.out.println("workbook to string");
		System.out.println(workbook.toString());
		
//		XSSFSheet sheet = workbook.getSheetAt(0);
//		String sheetName = sheet.getSheetName();
//		System.out.println(sheetName);
		
//		System.out.println("Number of sheets: " + workbook.getNumberOfSheets());
//		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//			System.out.println(workbook.getSheetAt(i).getSheetName());
//		}
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = sheet.getRow(2);
		System.out.println(row.getRowNum());
		XSSFCell cell = row.getCell(1);
		System.out.println(cell.getStringCellValue());
		
	}
}
