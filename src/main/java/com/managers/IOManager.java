package com.managers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    @SuppressWarnings("all")
    public static void createFolderIfNotExists(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
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

    public static StringBuilder readFileContent(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                stringBuilder.append(data + "\n");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
        return stringBuilder;
    }

    public static void writeToFile(String path, String content) {
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
