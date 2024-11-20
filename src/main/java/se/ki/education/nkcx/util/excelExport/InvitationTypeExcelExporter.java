package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.InvitationTypeRes;

import java.util.List;

public class InvitationTypeExcelExporter {

    public Workbook exportToExcel(List<InvitationTypeRes> dataList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Id","Type", "XType", "Description"

        };

        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        int rowNum = 1;
        for (InvitationTypeRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String type = (data.getType() != null && !data.getType().isEmpty()) ? data.getType() : "";
            dataRow.createCell(1).setCellValue(type);

            String xType = (data.getXtype() != null && !data.getType().isEmpty()) ? data.getXtype() : "";
            dataRow.createCell(2).setCellValue(xType);

            String description = (data.getDescription() != null && !data.getDescription().isEmpty()) ? data.getDescription() : "";
            dataRow.createCell(3).setCellValue(description);
        }

        return workbook;
    }
}
