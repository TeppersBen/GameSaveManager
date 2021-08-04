package com.frames.rimworld;

public class RimworldSaveManipulator {

    private StringBuilder saveFileContent;

    public void attachFileContent(StringBuilder saveFileContent) {
        this.saveFileContent = saveFileContent;
    }

    public void finishBuildings() {
        int start = 0;
        int lastSegmentEnd = 0;
        int oldSegmentLength = 0;
        String segment;
        int entitiesFound = 0;
        int modifications = 0;
        while ((start = saveFileContent.indexOf("<thing Class=\"Blueprint_Build\">", lastSegmentEnd)) != -1 || (start = saveFileContent.indexOf("<thing Class=\"Frame\">", lastSegmentEnd)) != -1) {
            segment = saveFileContent.substring(start, saveFileContent.indexOf("</thing>", start) + "</thing>".length());
            oldSegmentLength = segment.length();

            entitiesFound++;

            if (segment.contains("Frame_Battery") || segment.contains("Blueprint_Battery")) {
                segment = segment.replace("\"Frame\"", "\"Building_Battery\"");
                segment = segment.replace("\"Blueprint_Build\"", "\"Building_Battery\"");
                segment = segment.replaceAll("Frame_", "");
                segment = segment.replaceAll("Blueprint_", "");
                segment = segment.replace("<health>25</health>", "<health>100</health>");
                segment = segment.replace("<sourcePrecept>null</sourcePrecept>","<parentThing>null</parentThing>");
                segment = segment.replace("<everSeenByPlayer>True</everSeenByPlayer>","<storedPower>1</storedPower>");

                if (!segment.contains("health")) {
                    segment = segment.replace("</thing>", "\t<health>100</health>\n\t\t\t\t\t</thing>");
                }

                if (segment.contains("<resourceContainer")) {
                    int startResourceContainer = segment.indexOf("<resourceContainer");
                    segment = segment.substring(0, startResourceContainer);
                }

                modifications++;
            }

        /*else if (segment.contains("Blueprint_Turret_MiniTurret")) { TODO :: BROKEN, Turret spawns without head.
            segment = segment.replace("Blueprint_Build", "Building");
            segment = segment.replaceAll("Blueprint_", "");
            segment = segment.replace("<sourcePrecept>null</sourcePrecept>", "<instigator>null</instigator>");
            segment = segment.replace("<everSeenByPlayer>True</everSeenByPlayer>", "<thingsIgnoredByExplosion />");
            segment = segment.replace("<stuffToUse>Steel</stuffToUse>", "<fuel>18</fuel>");
            segment = segment.replace("</thing>", "\t<allowAutoRefuel>True</allowAutoRefuel>\n\t<health>100</health>\n\t\t\t\t\t</thing>");
        }*/

            lastSegmentEnd = start + segment.length();
            saveFileContent.replace(start, start+oldSegmentLength, segment);
        }
        System.out.printf("Entities Found: %s\nEntities Modified: %s\n", entitiesFound, modifications);
    }

    /**
     * Repairs all broken down devices
     */
    public void repairBrokenDownDevices() {
        String tagPower = "<powerOn>False</powerOn>";
        String tagBroken = "<brokenDown>True</brokenDown>";

        while (saveFileContent.indexOf(tagPower) != -1) {
            int start = saveFileContent.indexOf(tagPower);
            saveFileContent.replace(start, start+tagPower.length(), "");
        }

        while (saveFileContent.indexOf(tagBroken) != -1) {
            int start = saveFileContent.indexOf(tagBroken);
            saveFileContent.replace(start, start+tagBroken.length(), "");
        }
    }

    public String getContent() {
        return saveFileContent.toString();
    }

}
