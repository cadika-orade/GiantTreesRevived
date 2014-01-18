package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Cad'ika Orade
 */
public class TreePopulator{

    private CreateTree CT;

    /**
     * Populates the world with trees
     * @param b BukkitScheduler
     */
    public TreePopulator(BukkitScheduler b){
        CT = new CreateTree(b, false, GiantTreesRevived.getSettings().getWaitAfterOccur() * 1000);
        CT.setTreeBuildDelay(0);
    }
    /**
     * Sees if chunk will have a tree, and if so adds it to the cache
     * @param chunk chunk in question
     */
    public void parseChunk(Chunk chunk){
        if(getRandomChance(GiantTreesRevived.getSettings().getPercentChanceOccur())){
            Location loc = getSurface(chunk.getWorld(), chunk.getX() * 16, chunk.getZ() * 16);
            if(loc != null){
                if(GiantTreesRevived.getSettings().showShowXZLocationConsole()){
                    GiantTreesRevived.logInfo(loc.getWorld().getName() + " populated at X=" + loc.getX() + "   Z=" + loc.getZ());
                }
                Tree tree = new Tree(loc,
                        getRandom(GiantTreesRevived.getSettings().getMaximumOccurHeight(), GiantTreesRevived.getSettings().getMinimumOccurHeight()),
                        getRandom(GiantTreesRevived.getSettings().getMaximumOccurWidth(), GiantTreesRevived.getSettings().getMinimumOccurWidth()),
                        "17", "18", "20");
                tree.setResidingChunk(chunk);
                CT.addTree(tree);
            }
        }
    }
    /**
     * Finds the surface of the world, returns null if it can't be built on or gets to the bottom
     * @param world world
     * @param x x location of chunk
     * @param z z location of chunk
     * @return location of surface
     */
    private Location getSurface(World world, int x, int z){
        x = (int)(Math.random() * 16 + x);
        z = (int)(Math.random() * 16 + z);
        Location loc = new Location(world, x, 0, z);
        int id = 0;
        for(int y = world.getMaxHeight() - 1; y > 0; y--){
              loc = new Location(world, x, y, z);
              id = loc.getBlock().getTypeId();
              if(id != 0){
                  if(id == Material.STATIONARY_WATER.getId() ||
                     id == Material.WATER.getId() ||
                     id == Material.STATIONARY_LAVA.getId() ||
                     id == Material.LAVA.getId() ||
                     id == Material.ICE.getId() ||
                     id == Material.BEDROCK.getId()){
                      loc = null;
                      break;
                  }
                  else if(id == Material.GRASS.getId() ||
                          id == Material.DIRT.getId() ||
                          id == Material.STONE.getId() ||
                          id == Material.GRAVEL.getId() ||
                          id == Material.SAND.getId()){
                      break;
                  }
              }
              if(y == 1){
                  loc = null;
                  break;
              }
        }

        return loc;
    }
    /**
     * Gets a random number between two ints
     * @param max max amount
     * @param min min amount
     * @return string of random int
     */
    private String getRandom(int max, int min){
        int num = (int) (Math.random() * (max - min) + min);

        return String.valueOf(num);
    }
    /**
     * Gets the random chance something will occur
     * @param percentChance chance it will occur
     * @return if it occured or not
     */
    private boolean getRandomChance(int percentChance){
        double percent = (double)(percentChance) / 100.0;
        if(Math.random() + 0.01 <= percent){
            return true;
        }
        else{
            return false;
        }
    }
}
