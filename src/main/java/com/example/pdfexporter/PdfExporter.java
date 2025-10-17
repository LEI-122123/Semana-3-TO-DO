package com.example.pdfexporter;

import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class PdfExporter<T> {

    public void exportToPdf(List<T> items, String filePath) throws Exception {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("No data to export");
        }

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Title
        String titleText = items.getFirst().getClass().getSimpleName() + " List";
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph(titleText, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Build dynamic table
        Field[] fields = items.getFirst().getClass().getDeclaredFields();
        PdfPTable table = new PdfPTable(fields.length);
        table.setWidthPercentage(100);

        // Header row
        for (Field field : fields) {
            field.setAccessible(true);
            PdfPCell header = new PdfPCell(new Phrase(field.getName()));
            header.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(header);
        }

        // Data rows
        for (T item : items) {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(item);
                table.addCell(value != null ? value.toString() : "");
            }
        }

        document.add(table);
        document.close();
    }
}
