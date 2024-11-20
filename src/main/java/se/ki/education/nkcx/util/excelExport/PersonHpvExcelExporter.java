package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.PersonHpvRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;

import java.util.List;

public class PersonHpvExcelExporter {

    public Workbook exportToExcel(List<PersonHpvRes> dataList) {
        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Person ID", "Hpv Name"
        };

        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        // Populate the data rows
        int rowNum = 1;
        for (PersonHpvRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            // Extract person name
            String personId = (data.getPersonRes() != null && data.getPersonRes().getId() != null)
                    ? String.valueOf(data.getPersonRes().getId())
                    : "";
            dataRow.createCell(0).setCellValue(personId);

            // Extract hpv Name
            String hpvName = (data.getHpvRes() != null)
                    ? data.getHpvRes().getName()
                    : "";
            dataRow.createCell(1).setCellValue(hpvName);
        }

        return workbook;
    }
}
