package se.ki.education.nkcx.util.excelExport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.ki.education.nkcx.dto.response.Cell6923Res;

import java.util.Date;
import java.util.List;

public class Cell6923ExcelExporter {
    public Workbook exportToExcel(List<Cell6923Res> dataList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data Sheet");

        Row headerRow = sheet.createRow(0);
        String[] headersArr = {
                "Cell_Id", "Person_Id", "Laboratory_Id", "County_id", "Sample_Date", "Sample_Type", "Referral_Number", "Referral_type",
                "Reference_Site", "Residc", "Residk", "X_Sample_Date", "X_Registration_Date", "X_Snomed", "Diag_Id", "Ans_Clinic",
                "Rem_Clinic", "Registration_Date", "Scr_Type", "Snomed", "Response_Date", "X_Response_Date", "Diff_Days"
        };

        for (int i = 0; i < headersArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headersArr[i]);
        }

        // Populate the data rows
        int rowNum = 1;
        for (Cell6923Res data : dataList) {
            Row dataRow = sheet.createRow(rowNum++);

            String cellId = (data.getId() != null) ? String.valueOf(data.getId()) : "";
            dataRow.createCell(0).setCellValue(cellId);

            String personId = (data.getPersonRes() != null)
                    ? String.valueOf(data.getPersonRes())
                    : "";
            dataRow.createCell(1).setCellValue(personId);

            String laboratoryId = (data.getLaboratoryRes() != null)
                    ? String.valueOf(data.getLaboratoryRes())
                    : "";
            dataRow.createCell(2).setCellValue(laboratoryId);

            String countyId = (data.getCountyRes() != null)
                    ? String.valueOf(data.getCountyRes())
                    : "";
            dataRow.createCell(3).setCellValue(countyId);

            String sampleDate = (data.getSampleDate() != null) ? (data.getSampleDate()) : "";
            dataRow.createCell(4).setCellValue(sampleDate);

            String sampleType = (data.getSampleType() != null) ? (data.getSampleType()) : "";
            dataRow.createCell(5).setCellValue(sampleType);

            String referralNumber = (data.getReferralNumber() != null) ? String.valueOf(data.getReferralNumber()) : "";
            dataRow.createCell(6).setCellValue(referralNumber);

            String referenceType = (data.getReferenceTypeRes() != null)
                    ? String.valueOf(data.getReferenceTypeRes())
                    : "";
            dataRow.createCell(7).setCellValue(referenceType);

            String referenceSite = (data.getReferenceSite() != null) ? (data.getReferenceSite()) : "";
            dataRow.createCell(8).setCellValue(referenceSite);

            String residc = (data.getResidc() != null) ? String.valueOf(data.getResidc()) : "";
            dataRow.createCell(9).setCellValue(residc);

            String residk = (data.getResidk() != null) ? String.valueOf(data.getResidk()) : "";
            dataRow.createCell(10).setCellValue(residk);

            String xSampleDate = (data.getXSampleDate() != null) ? (data.getXSampleDate()) : "";
            dataRow.createCell(11).setCellValue(xSampleDate);


            String xRegistrationDate = (data.getXRegistrationDate() != null) ? (data.getXRegistrationDate()) : "";
            dataRow.createCell(12).setCellValue(xRegistrationDate);

            String xSnomed = (data.getXSnomed() != null) ? (data.getXSnomed()) : "";
            dataRow.createCell(13).setCellValue(xSnomed);

            String diagId = (data.getDiagId() != null) ? (data.getDiagId()) : "";
            dataRow.createCell(14).setCellValue(diagId);

            String ansClinic = (data.getAnsClinic() != null) ? (data.getAnsClinic()) : "";
            dataRow.createCell(15).setCellValue(ansClinic);

            String debClinic = (data.getDebClinic() != null) ? (data.getDebClinic()) : "";
            dataRow.createCell(16).setCellValue(debClinic);

            String remClinic = (data.getRemClinic() != null) ? (data.getRemClinic()) : "";
            dataRow.createCell(17).setCellValue(remClinic);

            String registrationDate = (data.getRegistrationDate() != null) ? (data.getRegistrationDate()) : "";
            dataRow.createCell(18).setCellValue(registrationDate);

            String scrType = (data.getScrType() != null) ? String.valueOf(data.getScrType()) : "";
            dataRow.createCell(19).setCellValue(scrType);

            String snomed = (data.getSnomed() != null) ? (data.getSnomed()) : "";
            dataRow.createCell(20).setCellValue(snomed);

            String responseDate = (data.getResponseDate() != null) ? (data.getResponseDate()) : "";
            dataRow.createCell(21).setCellValue(responseDate);

            String xResponseDate = (data.getXResponseDate() != null) ? String.valueOf(data.getXResponseDate()) : "";
            dataRow.createCell(22).setCellValue(xResponseDate);

            String diffDays = (data.getDiffDays() != null) ? String.valueOf(data.getDiffDays()) : "";
            dataRow.createCell(23).setCellValue(diffDays);

        }
        return workbook;
    }
}
