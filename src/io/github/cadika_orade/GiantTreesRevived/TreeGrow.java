package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Cad'ika Orade
 */
public class TreeGrow {

    private CreateTree CT;

    /**
     * Handles random and near-block tree growth
     * @param b BukkitScheduler
     */
    public TreeGrow(BukkitScheduler b){
        CT = new CreateTree(b, false, GiantTreesRevived.getSettings().getWaitAfterGrow() * 1000);
    }
    /**
     * Checks to see if a block or sapling placed is near another block/sapling
     * and should be a Giant Tree
     * Known Bug: Tree grows at location of last block placed, not the sapling
     * @param block the block
     * @param isSapling is it the sapling or not
     * @param player player who planted it
     * @return if a tree is grown or not
     */
    public boolean nearGrow(Block block, boolean isSapling, Player player){
        if(isSapling){
            if(isBlockNear(block, GiantTreesRevived.getSettings().getBlockNearGrowId(), -1)){
                createTree(block.getLocation(), player, block);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(isBlockNear(block, 6, 1)){
                //known bug, will create tree where block is placed, not sapling
                createTree(block.getLocation(), player, block);
                return true;
            }
            else{
                return false;
            }
        }
    }
    /**
     * Handles random growth of trees
     * @param block block placed
     * @param chance
     * @param player
     */
    public void randomGrow(Block block, int chance, Player player){
        double dChance = (double)chance / 100.0;
        if(Math.random() + 0.01 <= dChance){
            createTree(block.getLocation(), player, block);
        }
    }
    /**
     * Tells if a block is near another block
     * @param block the block
     * @param id the id
     * @param change up or down depending on if it's the sappling or not
     * @return true or false
     */
    private boolean isBlockNear(Block block, int id, int change){
        boolean isId = false;
        Location loc = block.getLocation();
        Location loc1, loc2, loc3;
        loc = new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() - 1);
        loc1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
        for(int y = 0; y < 2; y++){
            for(int z = 0; z < 3; z++){
                loc1 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY(), loc.getZ() + z);
                loc2 = new Location(loc1.getWorld(), loc1.getX() + 1, loc1.getY(), loc.getZ() + z);
                loc3 = new Location(loc1.getWorld(), loc1.getX() + 2, loc1.getY(), loc.getZ() + z);
                if(loc1.getBlock().getTypeId() == id || loc2.getBlock().getTypeId() == id || loc3.getBlock().getTypeId() == id){
                    isId = true;
                    break;
                }
            }
            loc1 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY() + change, loc1.getZ());
        }

        return isId;
    }
    /**
     * Get a random value between two numbers
     * @param max max number
     * @param min min number
     * @return string value of the random number
     */
    private String getRandom(int max, int min){
        int num = (int) (Math.random() * (max - min) + min);

        return String.valueOf(num);
    }
    /**
     * Add a tree to the list of trees to be created. If the block in question is the sappling, check its
     * data to see what type it is, and make the tree that type.
     * @param location location of the tree
     * @param player player who planted it
     * @param block block in question
     */
    private void createTree(Location location, Player player, Block block){
        Tree tree;
        player.sendMessage(ChatColor.GREEN + GiantTreesRevived.getSettings().getGrowMessage());
        if(block.getTypeId() == 6){
            String type = "oak";
            int data = (int)block.getData();
            if(data == 1){
                type = "spruce";
            }
            else if(data == 2){
                type = "birch";
            }
            else if(data == 3){
                type = "jungle";
            }
            tree = new Tree(location,
                            getRandom(GiantTreesRevived.getSettings().getMaximumGrowHeight(),
                            GiantTreesRevived.getSettings().getMinimumGrowHeight()),
                            getRandom(GiantTreesRevived.getSettings().getMaximumGrowWidth(),
                            GiantTreesRevived.getSettings().getMinimumGrowWidth()),
                            type, type, "20");
        }
        else{
            tree = new Tree(location,
                            getRandom(GiantTreesRevived.getSettings().getMaximumGrowHeight(),
                            GiantTreesRevived.getSettings().getMinimumGrowHeight()),
                            getRandom(GiantTreesRevived.getSettings().getMaximumGrowWidth(),
                            GiantTreesRevived.getSettings().getMinimumGrowWidth()),
                            "17", "18", "20");
        }
        CT.addTree(tree);
    }
}
