package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.CountyRes;

import java.util.List;

public class CountyExcelExporter {
    public Workbook exportToExcel(List<CountyRes> dataList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Name"
        };


        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        int rowNum = 1;
        for (CountyRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String name = (data.getName() != null && !data.getName().isEmpty()) ? data.getName() : "";
            dataRow.createCell(1).setCellValue(name);
        }

        return workbook;
    }
}
