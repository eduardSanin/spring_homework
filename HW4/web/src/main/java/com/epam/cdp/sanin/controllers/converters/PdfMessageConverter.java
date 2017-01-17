package com.epam.cdp.sanin.controllers.converters;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import model.impl.Tickets;
import model.interfaces.Ticket;
import org.fest.util.Collections;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.Charset;

public class PdfMessageConverter extends AbstractHttpMessageConverter<Tickets> {


    public PdfMessageConverter() {
        super(new MediaType("application", "pdf", Charset.forName("UTF-8")));
    }

    @Override
    public boolean canRead(Class aClass, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class aClass, MediaType mediaType) {
        return mediaType != null && "pdf".equals(mediaType.getSubtype());
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return aClass.equals(Tickets.class);
    }

    @Override
    protected Tickets readInternal(Class<? extends Tickets> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void writeInternal(Tickets tickets, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, httpOutputMessage.getBody());
            document.open();
            addContent(tickets, document);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void addContent(Tickets tickets, Document document) throws DocumentException {
        if (Collections.isEmpty(tickets.getTicketEntries())) {
            document.add(new Paragraph("No tickets found!"));
        } else {
            document.add(new Paragraph("Tickets"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.addCell(getTableHeadCell("ID"));
            table.addCell(getTableHeadCell("USER"));
            table.addCell(getTableHeadCell("EVENT"));
            table.addCell(getTableHeadCell("PLACE"));
            table.addCell(getTableHeadCell("CATEGORY"));
            table.setHeaderRows(1);
            for (Ticket ticket : tickets.getTicketEntries()) {
                table.addCell(String.valueOf(ticket.getId()));
                table.addCell(String.valueOf(ticket.getUserId()));
                table.addCell(String.valueOf(ticket.getEventId()));
                table.addCell(String.valueOf(ticket.getPlace()));
                table.addCell(ticket.getCategory().name());
            }
            document.add(table);
        }
    }

    private static PdfPCell getTableHeadCell(String name) {
        PdfPCell c1 = new PdfPCell(new Phrase(name));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c1;
    }
}
