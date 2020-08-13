package app.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Version;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FreeMarkerUtil {

    public static Configuration getConfiguration() {
        Configuration cfg = new Configuration(new Version("2.3.29"));
        cfg.setDefaultEncoding("UTF-8");
        return cfg;
    }

    public static StringTemplateLoader stringTemplateLoader(String templateName, String template) {
        StringTemplateLoader stl = new StringTemplateLoader();
        stl.putTemplate(templateName, template);
        return stl;
    }

    public static String readTemplate(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(FreeMarkerUtil.class.getClassLoader().getResource(fileName).toURI());
        Stream<String> lines = Files.lines(path);
        return lines.collect(Collectors.joining());
    }
}
