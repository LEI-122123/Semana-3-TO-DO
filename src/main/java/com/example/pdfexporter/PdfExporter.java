package com.example.pdfexporter;

import com.example.IFindAll;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.StreamResource;

import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Button pdfExportButton(HasComponents parentLayout, String prefix, IFindAll<T> dataSupplier)
    {
        Button downloadBtn = new Button("Download PDF");
        downloadBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        downloadBtn.addClickListener(e -> downloadPdfAction(parentLayout, downloadBtn, prefix, dataSupplier));
        return downloadBtn;
    }

    private void downloadPdfAction(HasComponents parentLayout, Button downloadBtn, String prefix, IFindAll<T> dataSupplier) {
        try {
            // Get the list of items dynamically from the supplier
            List<T> items = dataSupplier.findAll();

            // Create temporary PDF file
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
            String formattedDate = LocalDateTime.now().format(myFormatObj);
            String filename = prefix + "-" + formattedDate;
            File pdfFile = File.createTempFile(filename, ".pdf");

            // Export to PDF
            exportToPdf(items, pdfFile.getAbsolutePath());

            StreamResource resource = new StreamResource( filename + ".pdf",
                    () -> {
                        try
                        {
                            return new FileInputStream( pdfFile);
                        }
                        catch( FileNotFoundException e )
                        {
                            throw new RuntimeException( e );
                        }
                    }
            );

            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getStyle().set("display", "none"); // hide the anchor
            downloadBtn.getParent().ifPresent(parent -> ((HasComponents) parent).add(downloadLink));
            downloadLink.getElement().executeJs("this.click()");    // trigger the download

        } catch (Exception e) {
            Notification.show("Error generating PDF: " + e.getMessage());
        }
    }
}
