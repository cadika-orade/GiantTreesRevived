package io.github.cadika_orade.GiantTreesRevived;

import java.util.List;
import org.bukkit.World;

/**
 *
 * @author Cad'ika Orade
 */
public class Load {

    private FileHandler FH;

    /**
     * Creates folders for worlds if they don't exist. Will eventually convert old files
     * to the new format
     */
    public Load(){
        FH = new FileHandler();
    }
    /**
     * Creates world folders if they don't exist
     * @param worlds world list
     */
    public void load(List<World> worlds){
        String worldName, path;
        String[] treeFile = new String[1];
        do{
            worldName = worlds.remove(0).getName();
            path = "Saves" + FH.separator + worldName + FH.separator + "CurrentTree.dat";
            if(!FH.pathExists(path)){
                GiantTreesRevived.logInfo("Creating new world folder: " + worldName);
                treeFile[0] = "0";
                FH.write(treeFile, path);
            }
        }while(!worlds.isEmpty());
    }
    /**
     * Loads and starts convert for new file format
     * @param worlds world list
     */
    public void convert(){
        if(FH.pathExists("Undo Saves") && !FH.pathExists("Saves")){
            GiantTreesRevived.logInfo("Converting old files to new format...");
            GiantTreesRevived.logInfo("DO NOT shutdown the server during this process!");
            Convert C = new Convert();
            C.doConversion();
            GiantTreesRevived.logInfo("Done! Thanks for your patience!");
        }
    }
}
