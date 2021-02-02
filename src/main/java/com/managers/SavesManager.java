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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SavesManager {

    private String minecraftSavesFolder = System.getenv("APPDATA")+"/.minecraft/saves/";
    private List<Path> files;

    private Map<String, String> importantChunksEnd = Map.of(
            "r.0.0.mca","End Island SE",
            "r.0.-1.mca","End Island NE",
            "r.-1.0.mca","End Island SW",
            "r.-1.-1.mca","End Island NW",
            "r.-1.-4.mca","Wither Rose farm"
    );
    private Map<String, String> importantChunksOverworld = Map.of(
            "r.-4.-2.mca","Guardian Farm",
            "r.0.-3.mca","Quarry",
            "r.0.-1.mca","Main base, starter chunk",
            "r.0.0.mca","I am an idiot and part of the creeper farm is in this region [TO_BE_DELETED_SOON]",
            "r.0.-4.mca","Stronghold + Portal to the end."
    );
    private Map<String, String> importantChunksNether = Map.of(
            "r.0.-1.mca","Nether entry point + Portal to the stronghold",
            "r.-1.-1.mca","Guardian farm storage room",
            "r.-2.-1.mca","Gold farm above nether roof",
            "r.-2.0.mca","Magma farm above nether roof"
    );

    public Object[][] loadSavesContent() {
        File p = new File(minecraftSavesFolder);
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

    public String createBackup() {
        try {
            String targetLocation = minecraftSavesFolder + "/MinecraftJavaSurvival - (" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss")) + ")";
            TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                    minecraftSavesFolder + "/MinecraftJavaSurvival/",
                    targetLocation);
            Files.walkFileTree(Paths.get(minecraftSavesFolder + "/MinecraftJavaSurvival/"), fileVisitor);
            return new File(targetLocation).getName();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String purgeOldBackups() {
        loadSavesContent();
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

    public String purgeUnnecessaryChunks() {
        File overworld = new File(minecraftSavesFolder + "MinecraftJavaSurvival/region/");
        File nether = new File(minecraftSavesFolder + "MinecraftJavaSurvival/DIM-1/region/");
        File end = new File(minecraftSavesFolder + "MinecraftJavaSurvival/DIM1/region/");
        long sum = 0;
        sum += purgeSpecificMap(overworld, importantChunksOverworld);
        sum += purgeSpecificMap(nether, importantChunksNether);
        sum += purgeSpecificMap(end, importantChunksEnd);
        return "A total of " + calculateMiB(sum) + " MiB was purged!";
    }

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

    public String replaceWorldWithBackup() {
        try {
            Files.walk(new File(minecraftSavesFolder + "/MinecraftJavaSurvival/").toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            loadSavesContent();
            List<Path> availableBackups = files.stream().filter(e -> e.toFile().getName().contains("MinecraftJavaSurvival - (2")).collect(Collectors.toList());
            Path toCopy = availableBackups.get(availableBackups.size()-1);
            TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(
                    minecraftSavesFolder + "/" + toCopy.toFile().getName(),
                    minecraftSavesFolder + "/MinecraftJavaSurvival");
            Files.walkFileTree(Paths.get(minecraftSavesFolder + "/" + toCopy.toFile().getName()), fileVisitor);
            return "Replaced main world with latest backup!";
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    private long calculateMiB(long size) {
        return (long)((size - (size / 100 * 4.8576)) / 1000000);
    }

    public String getMinecraftSavesFolder() {
        return minecraftSavesFolder;
    }
}
