package app.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.freemarker.FreeMarkerUtil.readTemplate;
import static app.freemarker.FreeMarkerUtil.stringTemplateLoader;

@Slf4j
public class StringSplitListFreeMarker {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Configuration cfg = FreeMarkerUtil.getConfiguration();
        cfg.setClassForTemplateLoading(StringSplitListFreeMarker.class, "/");

        String templateName = "TEST_TEPLATE";

        String fileName = "split.txt";
        String temp = readTemplate(fileName);
        System.out.println(temp);
        cfg.setTemplateLoader(stringTemplateLoader(templateName, temp));

        Map<String, Object> templateData = new HashMap<>();

        templateData.put("val", "a,b,c,d");

        try (StringWriter out = new StringWriter()) {

            Template template = cfg.getTemplate(templateName);

            template.process(templateData, out);

            log.info("Formated message:" + out.getBuffer().toString());

        } catch (TemplateException e) {

            log.error("error occured :", e);
        }
    }
}



