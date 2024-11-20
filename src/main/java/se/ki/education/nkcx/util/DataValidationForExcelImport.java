package se.ki.education.nkcx.util;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.ZoneId;

public class DataValidationForExcelImport {

     public static DataFormatter dataFormatter = new DataFormatter();

    public static String getCellValueOrDefault(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            return dataFormatter.formatCellValue(cell);
        }
        return "No data";
    }

    public static LocalDate getLocalDateCellValueOrDefault(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return LocalDate.of(1970, 1, 1);
    }

    public static int getCellAgeValueOrDefault(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            return  Integer.parseInt(dataFormatter.formatCellValue(cell));
        }
        return 0;
    }

    public static Long getLongCellValueOrDefault(Row row, int cellIndex) {
        String cellValue = getCellValueOrDefault(row, cellIndex);
        try {
            return Long.parseLong(cellValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}

