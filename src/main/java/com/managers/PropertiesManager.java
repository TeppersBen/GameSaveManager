package com.managers;

import com.utils.TreeCopyFileVisitor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class PropertiesManager {

    private static Properties properties;
    public static String baseLocation;

    static {
        try {
            baseLocation = System.getProperty("user.home") + "\\GameSaveManager";
            if (!new File(baseLocation + "\\config.properties").exists()) {
                boolean dirsCreated = new File(baseLocation).mkdirs();
                if (dirsCreated) {
                    String source = Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("config.properties")).toString().substring(8);
                    TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                            source,
                            baseLocation + "\\config.properties");
                    Files.walkFileTree(Paths.get(source), fileVisitor);
                }
            }

            properties = new Properties();
            InputStream inputStream = new FileInputStream(baseLocation+"\\config.properties");

            properties.load(inputStream);
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
