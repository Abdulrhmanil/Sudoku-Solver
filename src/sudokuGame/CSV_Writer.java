package sudokuGame;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@code CSV_Writer} class is responsible to creating *.csv files,
 * and write information into it.
 * We use this class to create report (excel file) of the evolution to
 * find the algorithm (individual that contain tree-based GP) that succeed
 * to solve the sudoku board.
 * This class help us to create a report of the evolution progress.
 */
public class CSV_Writer {

	/** Delimiter used in CSV file */
	private static final String COMMA_DELIMITER = ",";

    /** Delimiter used in CSV file */
	private static final String NEW_LINE_SEPARATOR = "\n";

    /** The path of directory reports*/
    private static final String path="reports/";

    /** The name of the report file (it's should be creation time) */
	private String fileName;

    /** CSV file header */
	private static final String FILE_HEADER = "Genetic Sudoku Experiment";

    /**
     * Initialize the name of the report file
     * @param fileName is the name of the report file, (it's should be creation time).
     */
	public CSV_Writer(String fileName){
		this.fileName = fileName;
	}


    /**
     * Create a .csv report file in project directory
     * and write initial information.
     */
	public void createCsvFile() {
		FileWriter fileWriter ;
		try {
            (new File(path)).mkdirs();
			fileWriter = new FileWriter(path+fileName);
			/* Write the CSV file header */
			fileWriter.append(FILE_HEADER);
			/* Add a new line separator after the header*/
			fileWriter.append(NEW_LINE_SEPARATOR);
			/* write the timestamp of the experiment */
			fileWriter.append("Experiment Time: " + getCurrentTimeStamp());
			/* add a new line separator after the timestamp */
			fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.close();
		}
		catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}
	}


    /**
     * Appends new line to the report file (new row to excel file).
     * @param newLineArgs is the string that you want to append to the report file.
     */
	public void appendCsvFile(String[] newLineArgs) {
		FileWriter fileWriter ;
		try {
			fileWriter = new FileWriter(path+fileName, true);
			/* write the new information to the file */
			for(String s: newLineArgs){
				fileWriter.append(s);
				fileWriter.append(COMMA_DELIMITER);
			}
			/* Add a new line separator after the information */
			fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.close();
		}
		catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}
	}


    /**
     * Get the current time in a {@code String} format,
     * we use this to append the time to the report.
     * @return a String expression that represent the current time.
     */
	public static String getCurrentTimeStamp() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
	}
}


