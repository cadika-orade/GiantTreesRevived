package io.github.cadika_orade.GiantTreesRevived;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author Cad'ika Orade
 */
public class UndoBlockChange implements Runnable{

    private ArrayList<Block> blocks;
    private ArrayList<Material> materials;
    private boolean done;

    /**
     * Gets rid of the tree's blocks in the world
     * @param b blocks in question
     * @param m material to change them to
     */
    public UndoBlockChange(ArrayList<Block> b, ArrayList<Material> m){
        blocks = b;
        materials = m;
        done = false;
    }
    /**
     * Changes the blocks
     */
    public void run(){
        for(int i = 0; i < blocks.size(); i++){
            blocks.get(i).setType(materials.get(i));
        }
        done = true;
    }
    /**
     * Is done changing the blocks
     * @return true or false
     */
    public boolean isDone(){
        return done;
    }

}
