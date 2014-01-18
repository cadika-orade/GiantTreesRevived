package io.github.cadika_orade.GiantTreesRevived;

import java.util.ArrayList;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author Cad'ika Orade
 */
public class Build {

    private MetaData MD;
    private ArrayList<Block> blockList;
    private HashSet<Block> blockListHashSet;
    private ArrayList<Material> oldTypeList;
    private ArrayList<Material> newTypeList;

    /**
     * Keeps track of the blocks changed and what they are changed to. Does not
     * actually change the blocks
     * @param m MetaData of the tree in question
     */
    public Build(MetaData m){
        MD = m;
        blockList = new ArrayList<Block>();
        blockListHashSet = new HashSet<Block>();
        oldTypeList = new ArrayList<Material>();
        newTypeList = new ArrayList<Material>();
    }
    /**
     * ArrayList of blocks changed
     * @return blocks changed
     */
    public ArrayList<Block> getBlockList(){
        return blockList;
    }
    /**
     * Hashset of blocks changed
     * @return blocks changed
     */
    public HashSet<Block> getBlockListHashSet(){
        return blockListHashSet;
    }
    /**
     * ArrayList of what material blocks used to be
     * @return blocks changed
     */
    public ArrayList<Material> getOldTypeList(){
        return oldTypeList;
    }
    /**
     * ArrayList of what blocks will be
     * @return material list
     */
    public ArrayList<Material> getNewTypeList(){
        return newTypeList;
    }
    /**
     * Return the MetaData object for this build
     * @return MetaData object
     */
    public MetaData getMetaData(){
        return MD;
    }
    /**
     * Has the block in question been changed
     * @param block the block
     * @return has been changed
     */
    public boolean isBlockChanged(Block block){
        return blockListHashSet.contains(block);
    }
    /**
     * Changes the block/adds the block to the blocklist and hashset
     * Will not add blocks already in the hashset
     * @param loc location of the block
     * @param shiftX x change to block location
     * @param shiftY y change to block location
     * @param shiftZ z change to block location
     * @param material material to change the block to (adds to material list)
     * @return location of the block changed
     */
    public Location changeBlock(Location loc, double shiftX, double shiftY, double shiftZ, Material material){
        Block block;
        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());

        try{
            location.setX(location.getX() + shiftX);
            location.setY(location.getY() + shiftY);
            location.setZ(location.getZ() + shiftZ);
            block = location.getBlock();
            if(!blockListHashSet.contains(block)){
                blockList.add(block);
                blockListHashSet.add(block);
                oldTypeList.add(block.getType());
                newTypeList.add(material);
            }

            return location;
            
        }catch(Exception e){
            GiantTreesRevived.logSevere("Problem changing block!");
            e.printStackTrace();
            return location;
        }
    }
    /**
     * Changes the block/adds the block to the blocklist and hashset
     * Will change block regardless of hashset
     * @param loc location of the block
     * @param shiftX x change to block location
     * @param shiftY y change to block location
     * @param shiftZ z change to block location
     * @param material material to change the block to (adds to material list)
     * @return location of the block changed
     */
    public Location changeBlockForce(Location loc, double shiftX, double shiftY, double shiftZ, Material material){
        Block block;
        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());

        try{
            location.setX(location.getX() + shiftX);
            location.setY(location.getY() + shiftY);
            location.setZ(location.getZ() + shiftZ);
            block = location.getBlock();
            blockList.add(block);
            blockListHashSet.add(block);
            oldTypeList.add(block.getType());
            newTypeList.add(material);

            return location;
        }catch(Exception e){
            GiantTreesRevived.logSevere("Problem force changing block!");
            e.printStackTrace();
            return location;
        }
    }
    /**
     * Changes the block/adds the block to the blocklist and hashset
     * Will only change block if not in hashset and the block being changed is air
     * @param loc location of the block
     * @param shiftX x change to block location
     * @param shiftY y change to block location
     * @param shiftZ z change to block location
     * @param material material to change the block to (adds to material list)
     * @return location of the block changed
     */
    public Location addBlock(Location loc, double shiftX, double shiftY, double shiftZ, Material material){
        Block block;
        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());

        try{
            location.setX(location.getX() + shiftX);
            location.setY(location.getY() + shiftY);
            location.setZ(location.getZ() + shiftZ);
            block = location.getBlock();
            if(!blockListHashSet.contains(block) && block.getTypeId() == 0){
                blockList.add(block);
                blockListHashSet.add(block);
                oldTypeList.add(block.getType());
                newTypeList.add(material);
            }

            return location;

        }catch(Exception e){
            GiantTreesRevived.logSevere("Problem adding block!");
            e.printStackTrace();
            return location;
        }
    }
}
