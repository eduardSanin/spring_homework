package com.epam.cdp.sanin.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;

@Controller
@RequestMapping("/upload")
public class UploadingController {
    private static Logger LOG = Logger.getLogger(UploadingController.class);
    @Autowired
    private DataSource dataSource;

    /**
     * @return - redirect to file upload page
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getUploadPage(){
        return "fileUpload";
    }

    /**
     * @param file - file with sql instructions to be loaded
     * @return "OK" if success or redirect to the error page otherwise
     * @throws IOException if file uploading was failed
     * @throws SQLException if sql cannot be loaded to the database
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String uploadSqlScript(@RequestParam MultipartFile file) throws IOException, SQLException {
        LOG.info(MessageFormat.format("file [{0}] is being uploaded", file.getName()));
        Resource resource = new ByteArrayResource(file.getBytes());
        ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
        return "OK";
    }
}
