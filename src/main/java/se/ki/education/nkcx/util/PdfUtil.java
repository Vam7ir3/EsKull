package se.ki.education.nkcx.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class PdfUtil {

    private static final String TEMP_DIRECTORY = System.getProperty("catalina.home") + File.separator + "tmp_file";

    public String createPdf(String fileNameInitial, List<String> pageHeader, List<String> heading, List<List<String>> data) throws IOException {
        Path path = Paths.get(TEMP_DIRECTORY);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        File file = new File(TEMP_DIRECTORY, fileNameInitial + "_" + (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000) + ".pdf");

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(file));
        Document document = new Document(pdfDocument);

        if (pageHeader != null) {
            pageHeader.forEach(header -> {
                Paragraph paragraph = new Paragraph(header);
                document.add(paragraph);
            });
        }

        Table table = new Table(UnitValue.createPercentArray(heading.size())).useAllAvailableWidth();

        this.addTableHeader(table, heading);

        for (List<String> rowData : data) {
            this.addTableRow(table, rowData);
        }

        document.add(table);
        document.close();
        return file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("tmp_file") - 1);
        //return file.getAbsolutePath();
    }

    private void addTableHeader(Table table, List<String> heading) {
        for (String head : heading) {
            table.addHeaderCell(head);
        }
    }

    private void addTableRow(Table table, List<String> rowData) {
        for (String data : rowData) {
            table.addCell(data);
        }
    }
}
