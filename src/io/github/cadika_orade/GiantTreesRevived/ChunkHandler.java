package io.github.cadika_orade.GiantTreesRevived;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Cad'ika Orade
 */
public class ChunkHandler implements Listener{
    
    private ArrayList<TreePopulator> treePopulators;
    private ArrayList<String> names;

    /**
     * Creates list of worlds to create naturally occuring trees on for reference
     * @param b BukkitScheduler
     * @param w List of worlds
     */
    public ChunkHandler(BukkitScheduler b, List<World> w){
        treePopulators = new ArrayList<TreePopulator>();
        names = new ArrayList<String>();
        String world;
        do{
            world = w.remove(0).getName();
            if(GiantTreesRevived.getSettings().getOccurWorldList().contains(world)){
                treePopulators.add(new TreePopulator(b));
                names.add(world);
            }
        }while(!w.isEmpty());
    }
    /**
     * Watches for chunk population and if on a world set to spawn trees in the wild,
     * sends the chunk to be parsed
     * @param e Chunk Event
     */
    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent e){
        Chunk chunk = e.getChunk();
        int index = names.indexOf(chunk.getWorld().getName());
        if(index != -1){
            treePopulators.get(index).parseChunk(chunk);
        }
    }
}
