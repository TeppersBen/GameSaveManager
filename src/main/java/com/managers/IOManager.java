package com.managers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IOManager {

    public static void serializeHashMap(String source, Map<String, Map<String, String>> hmap) {
        try {
            FileOutputStream fos = new FileOutputStream(source + "\\AnvilMap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hmap);
            oos.close();
            fos.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("All")
    public static Map<String, Map<String, String>> deSerializeHashMap(String source) {
        try {
            FileInputStream fis = new FileInputStream(source + "\\AnvilMap.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap map = (HashMap) ois.readObject();
            ois.close();
            fis.close();
            return map;
        } catch(IOException | ClassNotFoundException ex) {
            Map map = new HashMap<String, Map<String, String>>();
            map.put("DIM -1", new HashMap<String, String>());
            map.put("DIM 0", new HashMap<String, String>());
            map.put("DIM 1", new HashMap<String, String>());
            return map;
        }
    }

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
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
