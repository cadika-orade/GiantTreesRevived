package io.github.cadika_orade.GiantTreesRevived;

import java.io.File;

/**
 *
 * @author Cad'ika Orade
 */
public class Convert {

    private FileHandler FH;

    /**
     * Converts old files to the new format
     */
    public Convert(){
        FH = new FileHandler();
    }
    /**
     * Converts old save files to the new format. Old files/folders are deleted last
     * to minimize damage if a user stops the server, or it crashes for some reason.
     */
    public void doConversion(){
        String worldName;
        String fileName;
        String number = "0";
        String[] oldData;
        String[] newData;
        String[] worlds = FH.getFolders("Undo Saves");
        for(int i = 0; i < worlds.length; i++){
            worldName = worlds[i];
            File folder = new File(FH.folder + "Undo Saves" + FH.separator + worldName);
            GiantTreesRevived.logInfo("Converting saves for " + worldName + "...");
            File[] files = folder.listFiles();
            for(int k = 0; k < files.length; k++){
                fileName = files[k].getName();
                if(!fileName.equals("Trees.dat")){
                    number = fileName.replace("Tree", "");
                    number = number.replace(".dat", "");
                    oldData = FH.read("Undo Saves" + FH.separator + worldName + FH.separator + fileName);
                    if(isThere(oldData[0])){
                        append(oldData, number, "Saves" + FH.separator + worldName + FH.separator + "Trees.dat");
                        newData = new String[oldData.length - 4];
                        for(int j = 0; j < newData.length; j += 4){
                            //need to re-order in new format: location first, type last
                            newData[j] = oldData[j + 5];
                            newData[j + 1] = oldData[j + 6];
                            newData[j + 2] = oldData[j + 7];
                            newData[j + 3] = oldData[j + 4];
                        }
                        FH.write(newData, "Saves" + FH.separator + worldName + FH.separator + fileName);
                        FH.writeToArchive("Saves" + FH.separator + worldName + FH.separator + fileName.replace(".dat", ".zip"),
                                          "Saves" + FH.separator + worldName + FH.separator + fileName);
                    }
                }
            }
            String[] current = {number};
            FH.write(current, "Saves" + FH.separator + worldName + FH.separator + "CurrentTree.dat");
            number = "0";
        }

        GiantTreesRevived.logInfo("Removing old stuff...");

        for(int i = 0; i < worlds.length; i++){
            worldName = worlds[i];
            File folder = new File(FH.folder + "Undo Saves" + FH.separator + worldName);
            File[] files = folder.listFiles();
            for(int k = 0; k < files.length; k++){
                files[k].delete();
            }
            folder.delete();
        }
        File temp = new File(FH.folder + "Undo Saves");
        temp.delete();
    }
    /**
     * Appends data to the end of a text file
     * @param info data to be appended
     * @param number number of the file
     * @param path path of the file
     */
    private void append(String[] info, String number, String path){
        String[] oldData;
        String[] newData;

        if(FH.pathExists(path)){
            oldData = FH.read(path);
            newData = new String[oldData.length + 5];
        }
        else{
            oldData = new String[0];
            newData = new String[oldData.length + 5];
        }
        
        for(int i = 0; i < oldData.length; i++){
            newData[i] = oldData[i];
        }
        newData[oldData.length] = info[0];
        newData[oldData.length + 1] = info[1];
        newData[oldData.length + 2] = info[2];
        newData[oldData.length + 3] = info[3];
        newData[oldData.length + 4] = number;

        FH.write(newData, path);
    }
    /**
     * Checks if a tree was deleted in the old file system (file remained in the old version)
     * @param owner owner of the tree
     * @return if it was deleted or not
     */
    private boolean isThere(String owner){
        owner = owner.replace("Owner:", "");
        if(owner.equalsIgnoreCase("IS UNDONE")){
            return false;
        }
        else{
            return true;
        }
    }
}
