package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.LaboratoryRes;
import se.ki.education.nkcx.dto.response.PersonRes;

import java.util.List;

public class LaboratoryExcelExporter {
    public Workbook exportToExcel(List<LaboratoryRes> dataList) {
        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Name", "IsInUse", "SosLab", "SosLabName", "SosLongName", "Region"

        };


        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        // Populate the data rows
        int rowNum = 1;
        for (LaboratoryRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);


            String name = (data.getName() != null && !data.getName().isEmpty()) ? data.getName() : "";
            dataRow.createCell(1).setCellValue(name);

            String isInUse = (data.getIsInUse() != null) ? String.valueOf(data.getIsInUse()) : "";
            dataRow.createCell(2).setCellValue(isInUse);

            String sosLab = (data.getSosLab() != null) ?  String.valueOf(data.getSosLab()) : "";
            dataRow.createCell(3).setCellValue(sosLab);

            String sosLabName = (data.getSosLabName() != null) ? data.getSosLabName() : "";
            dataRow.createCell(4).setCellValue(sosLabName);

            String sosLongName = (data.getSosLongName() != null) ? data.getSosLongName() : "";
            dataRow.createCell(5).setCellValue(sosLongName);

            String region = (data.getRegion() != null) ? data.getRegion() : "";
            dataRow.createCell(6).setCellValue(region);


        }

        return workbook;
    }
}
