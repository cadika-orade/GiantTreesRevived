package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Cad'ika Orade
 */
public class BlockPlaceHandler implements Listener{

    private TreeGrow TG;

    /**
     * Class declaration
     * @param b instance of the bukkit scheduler
     */
    public BlockPlaceHandler(BukkitScheduler b){
        TG = new TreeGrow(b);
    }
    /**
     * Gets a block place event for growing trees (random and near grow)
     * @param e the event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        boolean grow = false;

        if(GiantTreesRevived.getSettings().allowBlockNearGrow() && GiantTreesRevived.checkPermission(player, "grow")){
            if(block.getTypeId() == 6){
                grow = TG.nearGrow(block, true, e.getPlayer());
            }
            if(block.getTypeId() == GiantTreesRevived.getSettings().getBlockNearGrowId()){
                grow = TG.nearGrow(block, false, e.getPlayer());
            }
        }
        if(GiantTreesRevived.getSettings().allowRandomChanceGrow() && GiantTreesRevived.checkPermission(player, "grow")){
            if(block.getTypeId() == 6 && !grow){
                TG.randomGrow(block, GiantTreesRevived.getSettings().getPercentRandomChanceGrow(), e.getPlayer());
            }
        }
    }

}
