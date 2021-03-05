package com.managers;

import com.utils.Settings;
import com.utils.TreeCopyFileVisitor;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class PropertiesManager {

    private static Properties properties;

    static {
        try {
            String baseLocation = System.getProperty("user.home") + "\\GameSaveManager";
            if (!new File(baseLocation + "\\config.properties").exists()) {
                boolean failedToProcess = new File(baseLocation).mkdirs();
                if (!failedToProcess) {
                    String source = Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("config.properties")).toString().substring(8);
                    TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                            source,
                            baseLocation + "\\config.properties");
                    Files.walkFileTree(Paths.get(source), fileVisitor);
                }
            }

            properties = new Properties();
            String fileName = "config.properties";
            InputStream inputStream = PropertiesManager.class.getClassLoader().getResourceAsStream(fileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file " + fileName + " not found!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        String result = properties.getProperty(key);
        if (result.contains("%")) {
            String systemEnv = result.substring(result.indexOf("%")+1, result.indexOf("%", result.indexOf("%")+1));
            result = result.substring(result.indexOf("%", result.indexOf("%")+1)+1);
            result = System.getenv(systemEnv) + result;
        }
        return result;
    }

    public static void saveProperty(String key, String value) {
        //TODO :: Design the save feature.
    }
}
