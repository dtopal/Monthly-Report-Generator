package reportGenerator;

import java.io.*;

public class Config {
	private String yearlySchedule;
	private String classSchedule;
	private String nameJapanese; //name in Japanese for class schedule
	private String nameEnglish;
	private String blankReport;
	private String outputDirectory = "data/output/";
	
	
	public Config() throws Exception {
		yearlySchedule = getFileLoc("yearly");
		classSchedule = getFileLoc("class");
		nameJapanese = getFileLoc("teacher japanese name"); //the method is just looking for patterns so works even though name isn't a file
		nameEnglish = getFileLoc("teacher english name").toUpperCase();
		blankReport = getFileLoc("blank report");
	}
	
	public String getFileLoc(String sched) throws Exception {
		String filename = "data/conf.txt";
		File file = new File(filename);
		if (!file.exists()){
			System.out.println("conf.txt not found.  Check the data directory");
			System.exit(1);
		}
		FileInputStream fileInput = new FileInputStream(file);
		InputStreamReader iSR = new InputStreamReader(fileInput, "UTF8");
		BufferedReader br = new BufferedReader(iSR);
		
		String line = null;
		String loc = null;
		
		while ((line = br.readLine()) != null) {
			//System.out.println(line);
			if (line.contains(sched)) {
				//System.out.println(line.substring(line.indexOf(":") + 2));
				loc = line.substring(line.indexOf(":") + 2);
			}
		}
		
		br.close();
		return loc;
	}
	
	public String getYearlyScheduleLoc() {
		return this.yearlySchedule;
	}
	
	public String getClassScheduleLoc() {
		return this.classSchedule;
	}
	
	public String getTeacherJapaneseName() {
		return this.nameJapanese;
	}
	
	public String getTeacherEnglishName(){
		return this.nameEnglish;
	}
	
	public String getBlankReportLoc() {
		return this.blankReport;
	}
	
	public String getOutputLoc() {
		return this.getOutputLoc();
	}
}
