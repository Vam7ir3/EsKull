package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.PersonRes;

import java.util.List;

public class PersonExcelExporter {


    public Workbook exportToExcel(List<PersonRes> dataList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Person_Id", "PNR", "DateOfBirth", "IsValidPNR"
        };

        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        // Populate the data rows
        int rowNum = 1;
        for (PersonRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String personId = (data.getId() != null) ? String.valueOf(data.getId()) : "";
            dataRow.createCell(0).setCellValue(personId);

            String pnr = (data.getPnr() != null) ? String.valueOf(data.getPnr()) : "";
            dataRow.createCell(1).setCellValue(pnr);

            String dateOfBirth = (data.getDateOfBirth() != null) ? String.valueOf(data.getDateOfBirth()) : "";
            dataRow.createCell(2).setCellValue(dateOfBirth);

            String isValidPNR = (data.getIsValidPNR() != null) ? String.valueOf(data.getIsValidPNR()) : "";
            dataRow.createCell(3).setCellValue(isValidPNR);



        }
        return workbook;
    }
}

