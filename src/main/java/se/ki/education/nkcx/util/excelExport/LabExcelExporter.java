package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.LabRes;

import java.util.List;

public class LabExcelExporter {
    public Workbook exportToExcel(List<LabRes> dataList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Name", "isInUse"
        };


        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        // Populate the data rows
        int rowNum = 1;
        for (LabRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String name = (data.getName() != null && !data.getName().isEmpty()) ? data.getName() : "";
            dataRow.createCell(0).setCellValue(name);

            String isInUse = String.valueOf((data.getIsInUse() != null && !data.getIsInUse()));
            dataRow.createCell(1).setCellValue(isInUse);

        }

        return workbook;
    }
}
