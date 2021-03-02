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

    private final Map<String, String> importantChunksEnd = Map.of(
            "r.0.0.mca","End Island SE",
            "r.0.-1.mca","End Island NE",
            "r.-1.0.mca","End Island SW",
            "r.-1.-1.mca","End Island NW",
            "r.-1.-4.mca","Wither Rose farm"
    );
    private final Map<String, String> importantChunksOverworld = Map.of(
            "r.-4.-2.mca","Guardian Farm",
            "r.0.-3.mca","Quarry",
            "r.0.-1.mca","Main base, starter chunk",
            "r.0.0.mca","I am an idiot and part of the creeper farm is in this region [TO_BE_DELETED_SOON]",
            "r.0.-4.mca","Stronghold + Portal to the end."
    );
    private final Map<String, String> importantChunksNether = Map.of(
            "r.0.-1.mca","Nether entry point + Portal to the stronghold",
            "r.-1.-1.mca","Guardian farm storage room",
            "r.-2.-1.mca","Gold farm above nether roof",
            "r.-2.0.mca","Magma farm above nether roof"
    );

    public Object[][] loadSavesContent(String path) {
        File p = new File(path);
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

    public String createBackup(String folderName) {
        try {
            String targetLocation = Settings.DEFAULT_MINECRAFT_BACKUP_FOLDER + "/"+folderName+" - (" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss")) + ")";
            TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                    Settings.DEFAULT_MINECRAFT_SAVE_FOLDER + "/"+ folderName +"/",
                    targetLocation);
            Files.walkFileTree(Paths.get(Settings.DEFAULT_MINECRAFT_SAVE_FOLDER + "/"+ folderName +"/"), fileVisitor);
            return new File(targetLocation).getName();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    @SuppressWarnings("All")
    public String purgeWorldFolder(String fileName) {
        loadSavesContent(Settings.DEFAULT_MINECRAFT_SAVE_FOLDER);
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
        loadSavesContent(Settings.DEFAULT_MINECRAFT_BACKUP_FOLDER);
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
        File overworld = new File(Settings.DEFAULT_MINECRAFT_SAVE_FOLDER + worldName + "/region/");
        File nether = new File(Settings.DEFAULT_MINECRAFT_SAVE_FOLDER + worldName + "/DIM-1/region/");
        File end = new File(Settings.DEFAULT_MINECRAFT_SAVE_FOLDER + worldName + "/DIM1/region/");
        long sum = 0;
        sum += purgeSpecificMap(overworld, importantChunksOverworld);
        sum += purgeSpecificMap(nether, importantChunksNether);
        sum += purgeSpecificMap(end, importantChunksEnd);
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

    @SuppressWarnings("All")
    public String replaceWorldWithBackup(String worldName) {
        try {
            Files.walk(new File(Settings.DEFAULT_MINECRAFT_SAVE_FOLDER + "/"+ worldName +"/").toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            loadSavesContent(Settings.DEFAULT_MINECRAFT_BACKUP_FOLDER);
            List<Path> availableBackups = files.stream().filter(e -> e.toFile().getName().contains(worldName)).collect(Collectors.toList());
            Path toCopy = availableBackups.get(availableBackups.size()-1);
            TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                    Settings.DEFAULT_MINECRAFT_BACKUP_FOLDER + "/" + toCopy.toFile().getName(),
                    Settings.DEFAULT_MINECRAFT_SAVE_FOLDER + "/" +worldName);
            Files.walkFileTree(Paths.get(Settings.DEFAULT_MINECRAFT_BACKUP_FOLDER + "/" + toCopy.toFile().getName()), fileVisitor);
            return "Replaced main world with latest backup!";
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    private long calculateMiB(long size) {
        return (long)((size - (size / 100 * 4.8576)) / 1000000);
    }
}
