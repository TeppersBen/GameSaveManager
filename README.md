# GameSaveManager

A utility aimed at assisting backup geeks to help manage their game saves and or backups. 
The utility ranges from saves/backups managers all the way to additional save file alternations and so on.
The main goal is to increase the quality of life of a host or regular single player user to rapidly replace a world
with a backup in case there was a slight hic-up.

## Features

### General
- Every game has the default saves and backups window available in case the files are saved local
- Customize your sidebar to only include the games you play.
- Application comes with default game save paths, you might have to modify the path in settings in case you did
an express installation.
- Button that navigates directly towards the backups folder in case you want to do manual modifications.
- Wipe feature will remove all application data located on the computer.

### Available Games
- Minecraft
- Factorio
- Satisfactory
- Rimworld
- Sims4
- Valheim

### Game Specific
- Minecraft:
  - <b>Chunk list</b>: protected some anvil files, so they can't be removed.
  - <b>Wipe Unnecessary Chunks</b>: Wipes every chunk, unless the anvil file is within the Chunk List above.
  - <b>Minecraft Anvil Selector</b>: Manipulation file that allows you to move anvils around.
- Rimworld:
  - <b>Save File Manipulator</b>: Toggle a mixture of manipulations that alters your save in different ways, current
    alters are: <span style="color:red">(Use at your own risk!)</span>
    - Finish all Buildings <span style="color:red">(Not Finished)</span>.
    - Cure Colonists.
    - Max Out All Item Stacks.
    - Clean Whole Area.
    - Repair Broken Electronics.
    - All Pawns 999 Honor.
    - Fully Grow All Crops.
    - Force Happy Pawns.

## Installation
Navigate towards the releases and download the jar file that is located within the assets.
Make sure that you have [JDK 11.0.12](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) installed, it is the only JDK I can assure you it will work with. This is due to the
limitations caused by one of the dependencies used within this project.

in case you have multiple Java versions installed, you will have to do some additional steps in order to run the application.

###steps
- Open your 'CMD'
- Type: cd [PATH_TO_DOWNLOADED_JAR_FILE]
- Type: "C:\Program Files\Java\jdk-11.0.12\bin\java" -jar [JAR_NAME]

## Default config.properties
```INI
javaFXSDKLibraryFolder=

gameTabs=SettingsFrame

pathToMinecraftSaveFolder=%appdata%\\.minecraft\\saves\\
pathToFactorioSaveFolder=%appdata%\\Factorio\\saves\\
pathToSatisfactorySaveFolder=%localappdata%\\FactoryGame\\Saved\\SaveGames\\
pathToRimworldSaveFolder=%appdata%\\..\\Locallow\\Ludeon Studios\\RimWorld by Ludeon Studios\\Saves\\
pathToSims4SaveFolder=%userprofile%\\OneDrive\\Documents\\Electronic Arts\\The Sims 4\\saves\\
pathToValheimSaveFolder=%appdata%\\..\\Locallow\\ronGate\\Valheim\\worlds\\
```