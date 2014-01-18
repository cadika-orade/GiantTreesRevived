package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.Location;

/**
 *
 * @author Cad'ika Orade
 */
public class TreeBase {

    /**
     * Creates the tree's base
     */
    public TreeBase(){
        
    }
    /**
     * Creates the trees base
     * @param build build object
     * @param location base location (center)
     * @param height height
     * @param width width
     */
    public void createTreeBase(Build build, Location location, int height, int width){
        double innerSpot = 1;
        int trunkWidthTemp = 0;
        for(int i = 0; i < height; i++){//does height, Y ways
            trunkWidthTemp = width;
            innerSpot = 1;
            do{//does width Z ways
                for(int k = -1 * (trunkWidthTemp / 2); k < trunkWidthTemp / 2; k++){//does width X ways
                    //this is the part thats off by one
                    innerSpot = (-1 * innerSpot) + 1;
                    build.changeBlock(location, k, i, innerSpot, build.getMetaData().getLogType());
                    innerSpot = (-1 * innerSpot) + 1;
                    build.changeBlock(location, k, i, innerSpot, build.getMetaData().getLogType());
                }
                innerSpot++;
                trunkWidthTemp -= 2;
            }while(trunkWidthTemp >= 2);
        }
    }
}
