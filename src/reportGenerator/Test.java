package reportGenerator;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;

public class Test {

	public static void main(String[] args) throws Exception{
				
		//testYearly();
		//testClassSched();
		testTemplateMaker();
	}
	
	public static void testYearly() throws Exception {
		String test = "yearly";		
		Config yr = new Config();
		String loc = yr.getFileLoc(test);
//		File file = new File(loc);
//		if (file.exists()) {
//			System.out.println("exists");
//		}
		System.out.println(loc);
		
		YearlySchedule ys = new YearlySchedule(yr);
		String ws = ys.getCurrentWeeklySchedule();
		System.out.println("Today's weekly schedule: " + ws);
		
		
		System.out.println("Weekly Schedule: " + ys.getWeeklySchedule(5, 27));
	}
	
	public static void testClassSched() throws Exception {
		//String test = "class";
		//System.out.println(test);
		LocalDate day = LocalDate.now();
		day = day.withMonth(5);
		day = day.withDayOfMonth(16);
		Config sched = new Config();
		//String loc = sched.getClassScheduleLoc();
		//System.out.println(loc);
		
		ClassSchedule cs = new ClassSchedule(sched);
		
		HashMap<Integer, String> dayClasses = cs.getDaySched(day, "B");
		for (int key : dayClasses.keySet()) {
			System.out.println(key + " - " + dayClasses.get(key));
		}
		
		
	}
	
	public static void testTemplateMaker() throws Exception {
		LocalDate day = LocalDate.now();
		day = day.withMonth(2);
		Config config = new Config();
		
		TemplateMaker tm = new TemplateMaker(config);
		
		tm.makeMonthlyTemplate(day);
	}
}
