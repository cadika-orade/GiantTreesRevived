package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.Location;

/**
 *
 * @author Cad'ika Orade
 */
public class TreeBranch {

    /**
     * Creates the branches on the tree
     */
    public TreeBranch(){
        
    }
    /**
     * Creates the branches on the tree
     * @param build build object
     * @param location location of the branches (center)
     * @param loopCount how many times to loop (density)
     * @param xVar amount of random x change
     * @param yVar amount of random y change
     * @param zVar amount of random z change
     */
    public void createTreeBranch(Build build, Location location, int loopCount, double xVar, double yVar, double zVar){
        for(int i = 0; i < loopCount; i++){
            location = build.changeBlock(location, Math.random() * xVar, Math.random() * yVar, Math.random() * zVar, build.getMetaData().getLogType());
        }
    }

}
