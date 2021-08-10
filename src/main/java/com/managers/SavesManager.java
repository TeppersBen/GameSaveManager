package com.managers;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SavesManager {

    private List<Path> files;
    private final String backupPrefix = " - (" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss")) + ")";
    public static String backupCoreFolder = PropertiesManager.baseLocation + "\\backups\\";

    /**
     * Gathers all files located at the location's source.
     * @param location String
     * @return Object[FileName][MiB]
     */
    public Object[][] loadSavesContent(String location) {
        File p = new File(location);
        try {
            files = Files.list(p.toPath()).collect(Collectors.toList());
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

    public String createBackup(String savesPath, String gameName, String folderName) {
        try {
            String targetLocation = backupCoreFolder + gameName + "/"+folderName+backupPrefix;
            TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                    savesPath + "/"+ folderName +"/",
                    targetLocation);
            Files.walkFileTree(Paths.get(savesPath + "/"+ folderName +"/"), fileVisitor);
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
        loadSavesContent(PropertiesManager.getProperty("pathToMinecraftBackupFolder"));
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
        File overworld = new File(PropertiesManager.getProperty("pathToMinecraftSaveFolder") + worldName + "/region/");
        File nether = new File(PropertiesManager.getProperty("pathToMinecraftSaveFolder") + worldName + "/DIM-1/region/");
        File end = new File(PropertiesManager.getProperty("pathToMinecraftSaveFolder") + worldName + "/DIM1/region/");
        long sum = 0;
        Map<String, Map<String, String>> anvilMap = IOManager.deSerializeHashMap(PropertiesManager.getProperty("pathToMinecraftSaveFolder") + worldName);
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
    public String replaceWorldWithBackup(String savesFolder, String gameName, String worldName) {
        try {
            String backupFolder = backupCoreFolder + gameName;
            loadSavesContent(backupFolder);
            List<Path> availableBackups = files.stream().filter(e -> e.toFile().getName().contains(worldName)).collect(Collectors.toList());
            if (availableBackups.size() == 0) {
                return "No backups available for this world!";
            }
            Files.walk(new File(savesFolder + "/"+ worldName +"/").toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            Path toCopy = availableBackups.get(availableBackups.size()-1);
            TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                    backupFolder + "/" + toCopy.toFile().getName(),
                    savesFolder + "/" + worldName);
            Files.walkFileTree(Paths.get(backupFolder + "/" + toCopy.toFile().getName()), fileVisitor);
            return "Replaced main world with latest backup!";
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    @SuppressWarnings("all")
    public String recoverBackup(String savesFolder, String gameName, String backupName) {
        String backupFolder = backupCoreFolder + gameName;
        File backup = new File(backupFolder + "\\" + backupName);
        File saveWorld = new File(savesFolder + "\\" + backupName.substring(0, backupName.length()-24));
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

    public String wipeSims4Checkpoints(String path, String gameName) {
        try {
            List<Path> checkpoints = Files.list(new File(path.substring(0,path.length()-gameName.length())).toPath()).collect(Collectors.toList());
            checkpoints = checkpoints.stream().filter(c -> c.getFileName().toString().contains(gameName) && c.getFileName().toString().contains(".save.ver")).collect(Collectors.toList());
            AtomicBoolean somethingHasBeenRemoved = new AtomicBoolean(false);
            checkpoints.forEach(e -> somethingHasBeenRemoved.set(e.toFile().delete()));
            if (somethingHasBeenRemoved.get()) {
                return "Checkpoints were wiped.";
            }
            return "There were no checkpoints for this save.";
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    private long calculateMiB(long size) {
        //TODO :: make this formula dynamic. 4.8576 only applies to a curtain size.
        return (long)((size - (size / 100 * 4.8576)) / 1000000);
    }
}
