package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.MunicipalityRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.ParishRes;

import java.util.List;

public class ParishExcelExporter {
    public Workbook exportToExcel(List<ParishRes> dataList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Id", "Name", "Register_Date", "Divided_Other_County", "Municipality_Id", "County_Id"
        };

        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        int rowNum = 1;
        for (ParishRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String id = (data.getId() != null) ? String.valueOf(data.getId()) : "";
            dataRow.createCell(0).setCellValue(id);

            String name = (data.getName() != null) ? data.getName() : "";
            dataRow.createCell(1).setCellValue(name);

            String registerDate = (data.getRegisterDate() != null) ? String.valueOf(data.getRegisterDate()) : "";
            dataRow.createCell(3).setCellValue(registerDate);

            String dividedOtherCounty = (data.getDividedOtherCounty() != null) ? String.valueOf(data.getDividedOtherCounty()) : "";
            dataRow.createCell(4).setCellValue(dividedOtherCounty);

            String municipalityId = (data.getMunicipalityRes() != null) ? String.valueOf(data.getMunicipalityRes()) : "";
            dataRow.createCell(5).setCellValue(municipalityId);

            String countyId = (data.getCountyRes() != null) ? String.valueOf(data.getCountyRes()) : "";
            dataRow.createCell(6).setCellValue(countyId);

        }
        return workbook;
    }
}
