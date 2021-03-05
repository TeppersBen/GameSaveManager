package com.utils;

import com.managers.PropertiesManager;

public class Settings {
    public static String pathToMinecraftSaveFolder = PropertiesManager.getProperty("pathToMinecraftSaveFolder");
    public static String pathToMinecraftBackupFolder = PropertiesManager.getProperty("pathToMinecraftBackupFolder");
}
