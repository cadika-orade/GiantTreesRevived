package io.github.cadika_orade.GiantTreesRevived;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author Cadika Orade
 */
public class Save {

    private FileHandler FH;

    /**
     * Saves a tree's undo data to file
     */
    public Save(){
        FH = new FileHandler();
    }
    /**
     * Saves a tree to file
     * @param tree the tree
     * @param build the build
     */
    public void save(Tree tree, Build build){
        Location treeLocation = tree.getLocation();
        ArrayList<Material> materialList = build.getOldTypeList();
        ArrayList<Location> locationList = getLocations(build.getBlockList());
        int num = getCurrentTreeFile("Saves" + FH.separator + tree.getWorldName() + FH.separator + "CurrentTree.dat");
        num++;
        int spot;
        String[] ownerFile;
        if(!FH.pathExists("Saves" + FH.separator + tree.getWorldName() + FH.separator + "Trees.dat")){
            spot = 0;
            ownerFile = new String[5];
        }
        else{
            String[] oldOwnerFile = FH.read("Saves" + FH.separator + tree.getWorldName() + FH.separator + "Trees.dat");
            spot = oldOwnerFile.length;
            ownerFile = new String[spot + 5];
            for(int i = 0; i < oldOwnerFile.length; i++){
                ownerFile[i] = oldOwnerFile[i];
            }
        }
        
        ownerFile[spot] = "Owner:" + tree.getPlayerName();
        ownerFile[spot + 1] = String.valueOf(treeLocation.getX());
        ownerFile[spot + 2] = String.valueOf(treeLocation.getY());
        ownerFile[spot + 3] = String.valueOf(treeLocation.getZ());
        ownerFile[spot + 4] = String.valueOf(num);
        FH.write(ownerFile, "Saves" + FH.separator + tree.getWorldName() + FH.separator + "Trees.dat");

        String[] treeFile = new String[locationList.size() * 4];
        Location location;
        int materialSpot = 0;
        for(int i = 0; i < treeFile.length; i+= 4){
            location = locationList.remove(0);
            treeFile[i] = String.valueOf(location.getX());
            treeFile[i + 1] = String.valueOf(location.getY());
            treeFile[i + 2] = String.valueOf(location.getZ());
            treeFile[i + 3] = String.valueOf(materialList.get(materialSpot).getId());
            materialSpot++;
        }
        FH.write(treeFile, "Saves" + FH.separator + tree.getWorldName() + FH.separator + "Tree" + num + ".dat");
        FH.writeToArchive("Saves" + FH.separator + tree.getWorldName() + FH.separator + "Tree" + num + ".zip",
                          "Saves" + FH.separator + tree.getWorldName() + FH.separator + "Tree" + num + ".dat");
        String[] currentTree = {String.valueOf(num)};
        FH.write(currentTree, "Saves" + FH.separator + tree.getWorldName() + FH.separator + "CurrentTree.dat");
    }

    /**
     * Changes a list of blocks to a list of locations
     * @param blockList The list of blocks
     * @return The list of locations
     */
    private ArrayList<Location> getLocations(ArrayList<Block> blockList){
        ArrayList<Location> locationList = new ArrayList<Location>();
        Location location;
        for(int i = 0; i < blockList.size(); i++){
            location = blockList.get(i).getLocation();
            locationList.add(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()));
        }

        return locationList;
    }

    /**
     * Gets the current tree file number
     * @param path Path of the tree file; CurrentTree.dat
     * @return Tree file number
     */
    private int getCurrentTreeFile(String path){
        int x = 1;
        try{
            String[] temp = FH.read(path);
            x = Integer.parseInt(temp[0]);
            return x;
        }catch(Exception e){
            return 1;
        }
    }
}
