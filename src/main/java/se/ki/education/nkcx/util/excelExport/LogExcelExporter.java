package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.LogRes;
import se.ki.education.nkcx.dto.response.PersonCellRes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogExcelExporter {
    public Workbook exportToExcel(List<LogRes> dataList) {
        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Description", "Operation", "UserId", "TimeStamp"
        };

        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        // Populate the data rows
        int rowNum = 1;
        for (LogRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String description = (data.getDescription() != null && !data.getDescription().isEmpty()) ? data.getDescription() : "";
            dataRow.createCell(0).setCellValue(description);

            String operation = (data.getOperation() != null && !data.getOperation().isEmpty()) ? data.getOperation() : "";
            dataRow.createCell(1).setCellValue(operation);

            // Extract UserId
            String userId = (data.getUserId() != null && data.getUserId().getId() != null)
                    ? String.valueOf(data.getUserId().getId())
                    : "";
            dataRow.createCell(2).setCellValue(userId);

            // Set Timestamp
            LocalDateTime timestamp = data.getTimestamp();
            String formattedTimestamp = (timestamp != null)
                    ? timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : "";
            dataRow.createCell(3).setCellValue(formattedTimestamp);
        }



        return workbook;
    }
}
