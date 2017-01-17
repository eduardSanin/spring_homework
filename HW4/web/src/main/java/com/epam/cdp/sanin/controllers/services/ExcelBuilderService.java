package com.epam.cdp.sanin.controllers.services;

import jxl.common.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelBuilderService {
    private static final Logger LOG = Logger.getLogger(ExcelBuilderService.class);

    public void populateDocument(List<String> headerRowsName, List<List<String>> dataColumns, HSSFSheet sheet) {
        LOG.debug("creating headers and columns with data rows");
        createHeaderRows(headerRowsName, sheet);
        createColumns(dataColumns, sheet);
    }

    private void createColumns(List<List<String>> dataColumns, HSSFSheet sheet) {
        Iterator<List<String>> dataColumnsIterator = dataColumns.iterator();
        for (int columnNumber = 1; dataColumnsIterator.hasNext(); columnNumber++) {
            LOG.debug(MessageFormat.format("[{0}} column was created", columnNumber));
            Iterator<String> dataRowIterator = dataColumnsIterator.next().iterator();
            createDataRowForColumn(sheet, columnNumber, dataRowIterator);

        }
    }

    private void createDataRowForColumn(HSSFSheet sheet, int columnNumber, Iterator<String> dataRowIterator) {
        HSSFRow row = sheet.createRow(columnNumber);
        for (int rowNumber = 0; dataRowIterator.hasNext(); rowNumber++) {
            String rowValue = dataRowIterator.next();
            LOG.debug(MessageFormat.format("row [{0}] with value [{1}] for [{2}] column was created", rowNumber, rowValue, columnNumber ));
            row.createCell(rowNumber).setCellValue(rowValue);
        }
    }

    private void createHeaderRows(List<String> headerRowsName, HSSFSheet sheet) {
        HSSFRow header = sheet.createRow(0);
        Iterator<String> headerRowsIterator = headerRowsName.iterator();
        for (int i = 0; headerRowsIterator.hasNext(); i++) {
            String headerValue = headerRowsIterator.next();
            header.createCell(i).setCellValue(headerValue);
        }
    }
}
