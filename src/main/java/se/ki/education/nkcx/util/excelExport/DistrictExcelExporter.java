package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.DistrictRes;

import java.util.List;

public class DistrictExcelExporter {
    public Workbook exportToExcel(List<DistrictRes> dataList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Id", "District", "District_Name"

        };

        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        int rowNum = 1;
        for (DistrictRes data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String districtId = (data.getId() != null) ? String.valueOf(data.getId()) : "";
            dataRow.createCell(0).setCellValue(districtId);

            String district = (data.getDistrict() != null && !data.getDistrict().isEmpty()) ? data.getDistrict() : "";
            dataRow.createCell(1).setCellValue(district);

            String districtName = (data.getDistrictName() != null && !data.getDistrictName().isEmpty()) ? data.getDistrictName() : "";
            dataRow.createCell(1).setCellValue(districtName);
        }

        return workbook;
    }
}
