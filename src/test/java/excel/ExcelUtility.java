package excel;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility 
{
    private static Workbook workbook; 
    private static Sheet sheet; 

    // Loads the Excel file into memory
    public static void loadExcelData(String filePath) 
    {
        try (FileInputStream fis = new FileInputStream(filePath)) 
        { 
            workbook = new XSSFWorkbook(fis); 
        } 
        catch (IOException e) {
            throw new RuntimeException("Error loading Excel file: " + e.getMessage());
        }
    }

    // Sets the active sheet by name
    public static void setSheet(String sheetName) 
    {
        if (workbook == null) {  // Ensure workbook is loaded before setting the sheet
            throw new IllegalStateException("Workbook is not initialized. Call loadExcelData() first.");
        }

        sheet = workbook.getSheet(sheetName); 
        if (sheet == null) { 
            throw new IllegalArgumentException("Sheet " + sheetName + " does not exist in the workbook");
        }
    }

    // Retrieves cell data as a String from the specified row and column
    public static String getCellData(int rowNum, int colNum)
    {
        if (sheet == null)  
        {
            throw new IllegalStateException("Sheet has not been set. Call setSheet() first.");
        }
        DataFormatter formatter = new DataFormatter();
        try {
            return formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum)); 
        }
        catch (NullPointerException e) { 
            return "Cell not found";
        }
    }
    
    // Getter for workbook 
    public static Workbook getWorkbook() {
        return workbook;
    }

    // Getter for sheet 
    public static Sheet getSheet() {
        return sheet;
    }
}
