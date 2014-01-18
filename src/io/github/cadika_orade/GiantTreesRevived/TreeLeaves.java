package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.Location;

/**
 *
 * @author Cad'ika Orade
 */
public class TreeLeaves {

    /**
     * Adds leaves to trees
     */
    public TreeLeaves(){

    }
    /**
     * Adds leaves to trees
     * @param build build information
     * @param location location of the leaf cluster
     * @param height height of the leaf cluster
     * @param width width of the leaf cluster
     * @param density density of the leaf cluster
     * @param xOffset how much to offset each placement - X
     * @param yOffset how much to offset each placement - Y
     * @param zOffset how much to offset each placement - Z
     * @return the build
     */
    public Build createTreeLeaves(Build build, Location location, int height, int width, int density, int xOffset, int yOffset, int zOffset){
        //changed all * 2 to * 1 so halfed some values - width must be 2 times what it was (at least)
        //height is now height of the leaves - might make trees shorter
        double x, y, z;
        double oX, oY, oZ;
        double upRCornerX, upRCornerZ, upLCornerX, upLCornerZ, botRCornerX, botRCornerZ,
               botLCornerX, botLCornerZ;
        int xRand, yRand, zRand;
        
        xRand = 4 + xOffset;
        yRand = 4 + yOffset;
        zRand = 4 + zOffset;

        oX = location.getX();
        oY = location.getY();
        oZ = location.getZ();

        upRCornerX = width / 2 + oX;
        upRCornerZ = width / 2 + oZ;
        upLCornerX = -1 * width / 2 + oX;
        upLCornerZ = width / 2 + oZ;
        botRCornerX = width / 2 + oX;
        botRCornerZ = -1 * width / 2 + oZ;
        botLCornerX = -1 * width / 2 + oX;
        botLCornerZ = -1 * width / 2 + oZ;

        for(int i = 0; i < (density / 4) * (width - 3) * (width - 3); i++){//height * (((double)density / 200.0) * height) * (width - 4)
            x = Math.random() * 8 - xRand;
            y = Math.random() * 8 - yRand;
            z = Math.random() * 8 - zRand;
            //stops leaves from going outside boundaries
            if(location.getX() + x > width + oX || location.getX() + x < -1 * width + oX){
                x = -1 * x;
            }
            if(location.getY() + y > height + oY || location.getY() + y < oY){
                y = -1 * y;
            }
            if(location.getZ() + z > width + oZ || location.getZ() + z < -1 * width + oZ){
                z = -1 * z;
            }
            //makes slightly rounded edges on trees
            if(location.getX() + x > upRCornerX && location.getZ() + z > upRCornerZ){
                z -= width / 4;
            }
            else if(location.getX() + x < upLCornerX && location.getZ() + z > upLCornerZ){
                x += width / 4;
            }
            else if(location.getX() + x > botRCornerX && location.getZ() + z < botRCornerZ){
                x -= width / 4;
            }
            else if(location.getX() + x < botLCornerX && location.getZ() + z < botLCornerZ){
                z += width / 4;
            }
            //makes tree smaller near the top
            if(location.getY() + y > .76 * (double)height + oY){
                if(location.getX() + x > .75 *(double)width + oX){
                    x -= width / 4;
                }
                if(location.getX() + x < .75 *(double)width * -1 + oX){
                    x += width / 4;
                }
                if(location.getZ() + z > .75 *(double)width + oZ){
                    z -= width / 4;
                }
                if(location.getZ() + z < .75 *(double)width * -1 + oZ){
                    z += width / 4;
                }
            }
            //makes tree smaller near the bottom
            if(location.getY() + y < .26 * (double)height + oY){
                if(location.getX() + x > .75 *(double)width + oX){
                    x -= width / 4;
                }
                if(location.getX() + x < .75 *(double)width * -1 + oX){
                    x += width / 4;
                }
                if(location.getZ() + z > .75 *(double)width + oZ){
                    z -= width / 4;
                }
                if(location.getZ() + z < .75 *(double)width * -1 + oZ){
                    z += width / 4;
                }
            }
            //do the leaves
            build.changeBlock(location, x + 1, y, z, build.getMetaData().getLeafType());
            build.changeBlock(location, x - 1, y, z, build.getMetaData().getLeafType());
            build.changeBlock(location, x, y, z + 1, build.getMetaData().getLeafType());
            build.changeBlock(location, x, y, z - 1, build.getMetaData().getLeafType());
            build.changeBlock(location, x, y + 1, z, build.getMetaData().getLeafType());
            build.changeBlock(location, x, y - 1, z, build.getMetaData().getLeafType());
            build.changeBlock(location, x + 1, y, z + 1, build.getMetaData().getLeafType());
            build.changeBlock(location, x - 1, y, z + 1, build.getMetaData().getLeafType());
            build.changeBlock(location, x + 1, y, z - 1, build.getMetaData().getLeafType());
            build.changeBlock(location, x - 1, y, z - 1, build.getMetaData().getLeafType());
            location = build.changeBlockForce(location, x, y, z, build.getMetaData().getLogType());
        }
        return build;
    }

}
