package com.managers;

import com.utils.Settings;
import com.utils.TreeCopyFileVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SavesManager {

    private List<Path> files;
    private final String backupPrefix = " - (" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss")) + ")";

    public Object[][] loadSavesContent(String path) {
        File p = new File(path);
        try {
            files = Files.list(p.toPath()).collect(Collectors.toList());
            files.forEach(System.out::println);
            Object[][] data = new Object[files.size()][2];

            for (int i = 0; i < files.size(); i++) {
                data[i][0] = files.get(i).getFileName();
                long size = Files.walk(files.get(i)).filter(pa -> pa.toFile().isFile()).mapToLong(pa -> pa.toFile().length()).sum();
                data[i][1] = calculateMiB(size);
            }
            return data;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String createBackup(String folderName) {
        try {
            String targetLocation = Settings.pathToMinecraftBackupFolder + "/"+folderName+backupPrefix;
            TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                    Settings.pathToMinecraftSaveFolder + "/"+ folderName +"/",
                    targetLocation);
            Files.walkFileTree(Paths.get(Settings.pathToMinecraftSaveFolder + "/"+ folderName +"/"), fileVisitor);
            return new File(targetLocation).getName();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    @SuppressWarnings("All")
    public String purgeWorldFolder(String directory, String fileName) {
        loadSavesContent(directory);
        List<Path> toDelete = files.stream().filter(e -> e.toFile().getName().contains(fileName)).collect(Collectors.toList());
        AtomicLong sum = new AtomicLong();
        try {
            Files.walk(toDelete.get(0)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(e -> {
                sum.addAndGet(e.length());
                e.delete();
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "A total of " + calculateMiB(sum.longValue()) + " MiB was purged!";
    }

    @SuppressWarnings("All")
    public String purgeOldBackups() {
        loadSavesContent(Settings.pathToMinecraftBackupFolder);
        List<Path> toDeleted = files.stream().filter(e -> e.toFile().getName().contains("MinecraftJavaSurvival - (2")).collect(Collectors.toList());
        if (toDeleted.size()-1 > 0) {
            AtomicLong sum = new AtomicLong();
            for (int i = 0; i < toDeleted.size()-1; i++) {
                try {
                    Files.walk(toDeleted.get(i)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(e -> {
                        sum.addAndGet(e.length());
                        e.delete();
                    });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return "A total of " + calculateMiB(sum.longValue()) + " MiB was purged!";
        }
        return "No old backups were located!";
    }

    public String purgeUnnecessaryChunks(String worldName) {
        File overworld = new File(Settings.pathToMinecraftSaveFolder + worldName + "/region/");
        File nether = new File(Settings.pathToMinecraftSaveFolder + worldName + "/DIM-1/region/");
        File end = new File(Settings.pathToMinecraftSaveFolder + worldName + "/DIM1/region/");
        long sum = 0;
        Map<String, Map<String, String>> anvilMap = IOManager.deSerializeHashMap(Settings.pathToMinecraftSaveFolder + worldName);
        sum += purgeSpecificMap(overworld, anvilMap.get("DIM 0"));
        sum += purgeSpecificMap(nether, anvilMap.get("DIM -1"));
        sum += purgeSpecificMap(end, anvilMap.get("DIM 1"));
        return "A total of " + calculateMiB(sum) + " MiB was purged!";
    }

    @SuppressWarnings("All")
    private long purgeSpecificMap(File file, Map<String, String> map) {
        try {
            List<Path> anvils = Files.list(file.toPath()).collect(Collectors.toList());
            AtomicLong totalDeletedInSize = new AtomicLong();
            anvils.forEach(e -> {
                boolean found = false;
                for (String val : map.keySet()) {
                    if (e.toFile().getName().contains(val)) {
                        found = true;
                    }
                }
                if (!found) {
                    totalDeletedInSize.addAndGet(e.toFile().length());
                    e.toFile().delete();
                }
            });
            return totalDeletedInSize.longValue();
        } catch (IOException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @SuppressWarnings("all")
    public String replaceWorldWithBackup(String worldName) {
        try {
            loadSavesContent(Settings.pathToMinecraftBackupFolder);
            List<Path> availableBackups = files.stream().filter(e -> e.toFile().getName().contains(worldName)).collect(Collectors.toList());
            if (availableBackups.size() == 0) {
                return "No backups available for this world!";
            }
            Files.walk(new File(Settings.pathToMinecraftSaveFolder + "/"+ worldName +"/").toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            Path toCopy = availableBackups.get(availableBackups.size()-1);
            TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                    Settings.pathToMinecraftBackupFolder + "/" + toCopy.toFile().getName(),
                    Settings.pathToMinecraftSaveFolder + "/" + worldName);
            Files.walkFileTree(Paths.get(Settings.pathToMinecraftBackupFolder + "/" + toCopy.toFile().getName()), fileVisitor);
            return "Replaced main world with latest backup!";
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    @SuppressWarnings("all")
    public String recoverBackup(String backupName) {
        File backup = new File(Settings.pathToMinecraftBackupFolder + "\\" + backupName);
        File saveWorld = new File(Settings.pathToMinecraftSaveFolder + "\\" + backupName.substring(0, backupName.length()-24));
        TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                backup.toString(),
                saveWorld.toString()
        );
        try {
            if (saveWorld.exists()) {
                Files.walk(Path.of(saveWorld.getPath())).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
            Files.walkFileTree(Paths.get(backup.getPath()), fileVisitor);
            return backupName + " has been recovered!";
        } catch (IOException ex) {
            ex.printStackTrace();
            return "something went wrong!";
        }
    }

    private long calculateMiB(long size) {
        //TODO :: make this formula dynamic. 4.8576 only applies to a curtain size.
        return (long)((size - (size / 100 * 4.8576)) / 1000000);
    }
}
