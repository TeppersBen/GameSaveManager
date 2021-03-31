package com.utils;

import com.managers.PropertiesManager;

public class Settings {
    public static String pathToMinecraftSaveFolder = PropertiesManager.getProperty("pathToMinecraftSaveFolder");
    public static String pathToMinecraftBackupFolder = PropertiesManager.getProperty("pathToMinecraftBackupFolder");

    public static String pathToFactorioSaveFolder = PropertiesManager.getProperty("pathToFactorioSaveFolder");
    public static String pathToFactorioBackupFolder = PropertiesManager.getProperty("pathToFactorioBackupFolder");
}
