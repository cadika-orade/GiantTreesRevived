package io.github.cadika_orade.GiantTreesRevived;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author Cad'ika Orade
 */
public class TreeBuilder implements Runnable{

    private Build build;
    private ArrayList<Material> typeList;
    private ArrayList<Block> blockList;
    private MetaData meta;
    private boolean done;
    private int spot;
    private Block block;
    private Material material;

    /**
     * Changes one block in-game
     * @param b build object
     */
    public TreeBuilder(Build b){
        build = b;
        blockList = build.getBlockList();
        typeList = build.getNewTypeList();
        meta = build.getMetaData();
        done = false;
        spot = 0;
    }
    /**
     * Changes one block in-game
     */
    public void run() {
        if(spot < blockList.size()){
            block = blockList.get(spot);
            material = typeList.get(spot);
            block.setType(material);
            if(material.getId() == meta.getLogType().getId() && meta.hasLogMeta()){
                block.setData(meta.getLogMeta());
            }
            if(material.getId() == meta.getLeafType().getId() && meta.hasLeafMeta()){
                block.setData(meta.getLeafMeta());
            }
            spot++;
            if(spot == blockList.size()){
                done = true;
            }
        }
    }
    /**
     * Tells if all the blocks have been changed yet
     * @return true or false
     */
    public boolean isDone(){
        return done;
    }

}
