package com.itranswarp.recurring.ddl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.persistence.Entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.itranswarp.recurring.common.util.ClassScanner;

public class DDLGenerator {

    final Log log = LogFactory.getLog(getClass());

    public void export(String basePackage) throws Exception {
        List<Class<?>> classes = new ClassScanner().scan(basePackage, c-> {
        	return c.isAnnotationPresent(Entity.class);
        });
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.hbm2ddl.auto", "create");
        cfg.setProperty("hibernate.dialect", MySQL5InnoDBDialect.class.getName());
        for (Class<?> clazz : classes) {
            cfg.addAnnotatedClass(clazz);
        }
        SchemaExport se = new SchemaExport(cfg);
        se.setDelimiter(";");
        File file = new File(".").getAbsoluteFile();
        String schemaOutput = file.getCanonicalPath() + File.separator + "target" + File.separator + "ddl.sql";
        se.setOutputFile(schemaOutput);
        se.execute(true, false, false, false);
        String initOutput = file.getCanonicalPath() + File.separator + "target" + File.separator + "init.sql";
        generateInitOutput(initOutput);
        log.info("Database initialize script was successfully exported to file: " + initOutput);
        log.info("DDL script was successfully exported to file: " + schemaOutput);
    }

    void generateInitOutput(String file) throws Exception {
        String propertyName = "default.properties";
        URL resource = getClass().getClassLoader().getResource(propertyName);
        if (resource == null) {
            throw new IOException("Properties file not found: " + propertyName);
        }
        Properties props = new Properties();
        props.load(resource.openStream());
        String url = props.getProperty("jdbc.url");
        String user = props.getProperty("jdbc.user");
        String password = props.getProperty("jdbc.password");
        String database = url.substring(url.lastIndexOf("/") + 1);
        final String END = ";\n\n";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.write("drop database if exists " + database + END);
            writer.write("create database " + database + END);
            writer.write("grant all on " + database + ".* to \'" + user + "\'@\'localhost\' identified by \'" + password + "\'" + END);
            writer.write("use " + database + END);
        }
    }

    public static void main(String[] args) throws Exception {
        DDLGenerator generator = new DDLGenerator();
        generator.export("com.itranswarp.recurring");
    }
}
