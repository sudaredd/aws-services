package app.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static app.freemarker.FreeMarkerUtil.readTemplate;
import static app.freemarker.FreeMarkerUtil.stringTemplateLoader;

@Slf4j
public class MapFreeMarker {

    public static void main(String[] args) throws IOException, URISyntaxException {

        Configuration cfg = FreeMarkerUtil.getConfiguration();
        cfg.setClassForTemplateLoading(MapFreeMarker.class, "/");

        String templateName = "TEST_TEPLATE";

        String fileName = "map.txt";

        String t = "<#assign stateInd = FA_STATE_IND>\n" +
                "<#if stateInd=\"I\">\n" +
                "<#assign stateInd = \"Y\">\n" +
                "<#elseif stateInd=\"A\">\n" +
                "<#assign stateInd = \"Y\">\n" +
                "<#elseif stateInd=\"C\">\n" +
                "<#assign stateInd = \"Y\">\n" +
                "<#elseif stateInd=\"R\">\n" +
                "<#assign stateInd = \"N\">\n" +
                "</#if>\n" +
                "{\"IdVal\":\"${ID_VAL}\",\n" +
                "\"idType\":\"${ID_TYPE}\",\n" +
                "\"stateInd\":\"${stateInd}\"\n" +
                "}";

//       String temp = readTemplate(fileName);

        cfg.setTemplateLoader(stringTemplateLoader(templateName, t));

        Map<String, Object> templateData = new HashMap<>();


        templateData.put("FA_STATE_IND", "R");
        templateData.put("ID_VAL", "CR123");
        templateData.put("ID_TYPE", "CRN");

        try (StringWriter out = new StringWriter()) {

            Template template = cfg.getTemplate(templateName);

            template.process(templateData, out);

            log.info("Formated message:" + out.getBuffer().toString());

        } catch (TemplateException e) {

            log.error("error occured :", e);
        }
    }
}



