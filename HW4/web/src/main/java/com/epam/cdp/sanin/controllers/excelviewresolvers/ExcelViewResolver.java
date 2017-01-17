package com.epam.cdp.sanin.controllers.excelviewresolvers;

import com.epam.cdp.sanin.controllers.services.ExcelBuilderService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class ExcelViewResolver extends AbstractExcelView {
    private static final Logger LOG = Logger.getLogger(ExcelViewResolver.class);

    @Autowired
    private ExcelBuilderService excelBuilderService;

    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        LOG.info(MessageFormat.format("resolving excel view for such params [{0}]", map));
        List<String> headerRowsName = (List<String>) map.get("headerRowsName");
        List<List<String>> dataColumns = (List<List<String>>) map.get("dataColumns");
        HSSFSheet sheet = hssfWorkbook.createSheet("document");
        excelBuilderService.populateDocument(headerRowsName, dataColumns, sheet);
    }
}
