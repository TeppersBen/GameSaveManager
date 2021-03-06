package com.managers;

import com.utils.TreeCopyFileVisitor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class PropertiesManager {

    private static Properties properties;
    private static String baseLocation;

    static {
        try {
            baseLocation = System.getProperty("user.home") + "\\GameSaveManager";
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
            InputStream inputStream = new FileInputStream(baseLocation+"\\config.properties");

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
        if (result == null) result = "";
        if (result.contains("%")) {
            result = IOManager.convertSystemEnvironmentPathToAbsolutePath(result);
        }
        return result;
    }

    public static void saveProperty(String key, String value) {
        properties.setProperty(key, value);

        try (FileWriter output = new FileWriter(baseLocation+"\\config.properties")) {
            properties.store(output, "Saving Properties");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
