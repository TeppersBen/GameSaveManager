package com.managers;

public class IOManager {

    public static String convertSystemEnvironmentPathToAbsolutePath(String path) {
        if (path.contains("%")) {
            String systemEnv = path.substring(path.indexOf("%")+1, path.indexOf("%", path.indexOf("%")+1));
            path = path.substring(path.indexOf("%", path.indexOf("%")+1)+1);
            path = System.getenv(systemEnv) + path;
        }
        return path;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
