package com.frames.rimworld;

public class RimworldSaveManipulator {

    private StringBuilder saveFileContent;

    public void attachFileContent(StringBuilder saveFileContent) {
        this.saveFileContent = saveFileContent;
    }

    /**
     * TODO:: NOT FINISHED
     * Builds all buildings on the map, even frames that are not finished.
     */
    public void finishBuildings() {
        int start;
        int lastSegmentEnd = 0;
        int oldSegmentLength;
        String segment;
        while ((start = saveFileContent.indexOf("<thing Class=\"Blueprint_Build\">", lastSegmentEnd)) != -1 || (start = saveFileContent.indexOf("<thing Class=\"Frame\">", lastSegmentEnd)) != -1) {
            segment = saveFileContent.substring(start, saveFileContent.indexOf("</thing>", start) + "</thing>".length());
            oldSegmentLength = segment.length();

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

    /**
     * Recovers all colonist pawns.
     * Cures: Injuries & Missing Parts
     */
    public void pawnsCureAllColonists() {
        int start;
        int lastSegmentEnd = 0;
        String segment;
        int heathTrackerStart;
        while ((start = saveFileContent.indexOf("<thing Class=\"Pawn\">", lastSegmentEnd)) != -1) {
            segment = saveFileContent.substring(start, saveFileContent.indexOf("</thing>", start) + "</thing>".length());
            if (segment.contains("<faction>Faction_11</faction>")) {
                while ((heathTrackerStart = saveFileContent.indexOf("<li Class=\"Hediff_MissingPart\">", start)) != -1
                || (heathTrackerStart = saveFileContent.indexOf("<li Class=\"Hediff_Injury\">", start)) != -1
                || (heathTrackerStart = saveFileContent.indexOf("<li Class=\"HediffWithComps\">", start)) != -1) {
                    saveFileContent.replace(heathTrackerStart, saveFileContent.indexOf("</li>", heathTrackerStart)+"</li>".length(), "");
                }
            }
            lastSegmentEnd = start + segment.length();
        }
    }

    /**
     * Give all Loyalists 999 honor.
     */
    public void pawnsGiveAllLoyalists999Honor() {
        int start;
        int lastSegmentEnd = 0;
        String segment;
        int seeker;
        while ((start = saveFileContent.indexOf("<thing Class=\"Pawn\">", lastSegmentEnd)) != -1) {
            segment = saveFileContent.substring(start, saveFileContent.indexOf("</thing>", start) + "</thing>".length());

            if ((seeker = saveFileContent.indexOf("<royalty>", start)) != -1) {
                if ((seeker = saveFileContent.indexOf("<favor>", seeker)) != -1) {
                    if ((seeker = saveFileContent.indexOf("<values>", seeker)) != -1) {
                        saveFileContent.replace(
                                saveFileContent.indexOf("<li>", seeker),
                                saveFileContent.indexOf("</li>", seeker)+"</li>".length(),
                                "<li>999</li>"
                        );
                    }
                }
            }
            lastSegmentEnd = start + segment.length();
        }
    }

    /**
     * Maxes out all item stacks.
     */
    public void itemMaxOutStackCount() {
        int start;
        int lastSegmentEnd = 0;
        String segment;
        int seeker;
        while ((start = saveFileContent.indexOf("<thing Class=\"ThingWithComps\">", lastSegmentEnd)) != -1) {
            segment = saveFileContent.substring(start, saveFileContent.indexOf("</thing>", start) + "</thing>".length());

            if ((seeker = saveFileContent.indexOf("<stackCount>", start)) != -1) {
                saveFileContent.replace(seeker,
                        saveFileContent.indexOf("</stackCount>", seeker)+"</stackCount>".length(),
                        "<stackCount>9999</stackCount>");
            }
            lastSegmentEnd = start + segment.length();
        }
    }

    /**
     * Fully grows all plants.
     */
    public void plantForcedMaxGrowth() {
        int start;
        int lastSegmentEnd = 0;
        String segment;
        int seeker;
        while ((start = saveFileContent.indexOf("<thing Class=\"Plant\">", lastSegmentEnd)) != -1) {
            segment = saveFileContent.substring(start, saveFileContent.indexOf("</thing>", start) + "</thing>".length());

            if (segment.contains("Corn")) {
                if ((seeker = saveFileContent.indexOf("<growth>", start)) != -1) {
                    saveFileContent.replace(seeker,
                            saveFileContent.indexOf("</growth>", seeker)+"</growth>".length(),
                            "<growth>1</growth>");
                }
            }

            lastSegmentEnd = start + segment.length();
        }
    }

    /**
     * Cleans the whole map of Filth.
     */
    public void cleanWholeArea() {
        int start;
        int oldStart = 0;
        int lastSegmentEnd;
        String segment;
        while ((start = saveFileContent.indexOf("<thing Class=\"Filth\">", oldStart)) != -1) {
            segment = saveFileContent.substring(start, saveFileContent.indexOf("</thing>", start) + "</thing>".length());
            oldStart = start;
            lastSegmentEnd = start + segment.length();
            saveFileContent.replace(start, lastSegmentEnd, "");
        }
    }

    /**
     * Force all colonists to be very happy.
     * TODO :: tends to loop several times before wrapping up.
     */
    public void forceVeryHappyColonists() {
        int start;
        int lastSegmentEnd = 0;
        String segment;
        int seeker;
        int curStart;
        int curEnd;
        while ((start = saveFileContent.indexOf("<thing Class=\"Pawn\">", lastSegmentEnd)) != -1) {
            segment = saveFileContent.substring(start, saveFileContent.indexOf("</thing>", start) + "</thing>".length());

            curEnd = start;
            while ((seeker = saveFileContent.indexOf("<li Class=\"Need_", curEnd)) != -1) {
                curStart = saveFileContent.indexOf("<curLevel>", seeker);
                curEnd = saveFileContent.indexOf("</curLevel>", seeker)+"</curLevel>".length();
                saveFileContent.replace(
                        curStart,
                        curEnd,
                        "<curLevel>1</curLevel>"
                );
                seeker = curEnd;
                System.out.printf("%s\t%s\t%s\n", seeker, saveFileContent.indexOf("<curLevel>", seeker), saveFileContent.indexOf("</curLevel>", seeker)+"</curLevel>".length());
            }
            lastSegmentEnd = start + segment.length();
        }
    }

    public String getContent() {
        return saveFileContent.toString();
    }

}
