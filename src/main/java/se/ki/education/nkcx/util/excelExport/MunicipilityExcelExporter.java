package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.MunicipalityRes;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;

import java.util.List;

public class MunicipilityExcelExporter {

    public Workbook exportToExcel(List<MunicipalityRes> dataList) {
        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Id", "Name", "Year"
        };

        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        int rowNum = 1;
        for (MunicipalityRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String id = (data.getId() != null) ? String.valueOf(data.getId()) : "";
            dataRow.createCell(0).setCellValue(id);

            String name = (data.getName() != null) ? data.getName() : "";
            dataRow.createCell(1).setCellValue(name);

            String year = (data.getYear() != null) ? String.valueOf(data.getYear()) : "";
            dataRow.createCell(2).setCellValue(year);

        }
        return workbook;
    }
}
