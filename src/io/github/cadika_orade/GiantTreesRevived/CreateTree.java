package io.github.cadika_orade.GiantTreesRevived;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Cad'ika Orade
 */
public class CreateTree implements Runnable{

    private BukkitScheduler BS;
    private ArrayList<Tree> treeList;
    private boolean running = false;
    private boolean save = true;
    private int waitAfterCreation = 0, treeBuildDelay = 0;

    /**
     * Handles Creation of trees
     * @param b BukkitScheduler
     */
    public CreateTree(BukkitScheduler b){
        treeList = new ArrayList<Tree>();
        BS = b;
        treeBuildDelay = GiantTreesRevived.getSettings().getTreeBuildDelay();
    }
    /**
     * Optional parameters for creating trees, including the wait after creation
     * time and whether or not to save
     * @param b BukkitScheduler
     * @param s to save or not
     * @param wac wait after creation
     */
    public CreateTree(BukkitScheduler b, boolean s, int wac){
        treeList = new ArrayList<Tree>();
        BS = b;
        save = s;
        waitAfterCreation = wac;
        treeBuildDelay = GiantTreesRevived.getSettings().getTreeBuildDelay();
    }
    /**
     * Adds trees to the cache
     * @param t tree object
     */
    public void addTree(Tree t){
        treeList.add(t);
        if(!running){
            try{
                Thread th = new Thread(this);
                th.start();
            }catch(Exception e){
                GiantTreesRevived.logSevere("Unable to start tree creation thread!");
                e.printStackTrace();
            }
        }
        else{
            say(ChatColor.GREEN + "Added tree to cache...", t.getPlayer());
        }
    }
    /**
     * Creates a tree using other classes. As an independent thread it can wait while
     * the blocks in question are put into a list and then spawned in the world. This
     * thread must not crash, or it will shutdown the server.
     */
    public void run(){
        running = true;
        Player player;
        int taskNumber;
        do{
            //set up variables
            Tree tree = treeList.remove(0);
            player = tree.getPlayer();
            say(ChatColor.RED + "Stand Back!", player);
            //load chunk for tree populate events, since no players may be nearby
            if(tree.hasResidingChunk()){
                tree.getResisidingChunk().load();
            }
            Build build = new Build(tree.getMetaData());
            //figure out the base
            makeBase(build, tree);
            //figure out the branches
            makeBranches(build, tree);
            //figure out the leaves
            makeLeaves(build, tree);
            //give the user time to flee
            wait(5000);
            //set the blocks
            TreeBuilder TB;
            TB = new TreeBuilder(build);
            taskNumber = BS.scheduleSyncRepeatingTask(GiantTreesRevived.getPlugin(), TB, 0, treeBuildDelay);
            //wait till it is done
            do{
                wait(50);
            }while(!TB.isDone());
            BS.cancelTask(taskNumber);
            //unload and save the chunk if no one is around
            if(tree.hasResidingChunk()){
                tree.getResisidingChunk().unload(true, true);//(save, safe)
            }
            //write to file
            if(save){
                Save S = new Save();
                S.save(tree, build);
            }
            say(ChatColor.GREEN + "Giant Tree Created!", player);
            wait(waitAfterCreation);
        }while(!treeList.isEmpty());
        running = false;
    }
    /**
     * Makes the base of the tree using other classes
     * @param build build object
     * @param tree tree object
     */
    private void makeBase(Build build, Tree tree){
        Location location = tree.getLocation();
        TreeBase TB = new TreeBase();
        TB.createTreeBase(build, location, 3, tree.getWidth() + 2);
        TB.createTreeBase(build, location, 2, tree.getWidth() + 4);
        TB.createTreeBase(build, location, 1, tree.getWidth() + 6);
        TB.createTreeBase(build, location, tree.getHeight() - tree.getWidth() * 3 - 4, tree.getWidth());
    }
    /**
     * Makes the branches of the tree using other classes
     * @param build build object
     * @param tree tree object
     */
    private void makeBranches(Build build, Tree tree){
        Location location = tree.getLocation();
        location = new Location(location.getWorld(), location.getX(), location.getY() + tree.getHeight() - tree.getWidth() * 3 - 7, location.getZ());
        TreeBranch TBR = new TreeBranch();
        TBR.createTreeBranch(build, location, (int) (tree.getWidth() * 2.5), 1, 1, 1);//2, 1.5, 2
        TBR.createTreeBranch(build, location, (int) (tree.getWidth() * 2.5), -1, 1, 1);
        TBR.createTreeBranch(build, location, (int) (tree.getWidth() * 2.5), -1, 1, -1);
        TBR.createTreeBranch(build, location, (int) (tree.getWidth() * 2.5), 1, 1, -1);
    }
    /**
     * Makes the leaves of the tree using other classes
     * @param build build object
     * @param tree tree object
     */
    private void makeLeaves(Build build, Tree tree){
        Location location = tree.getLocation();
        location = new Location(location.getWorld(), location.getX(), location.getY() + tree.getHeight() - tree.getWidth() * 3 - 4, location.getZ());
        TreeLeaves TL = new TreeLeaves();
        TL.createTreeLeaves(build, location, tree.getWidth() * 3, tree.getWidth() * 2, tree.getDensity(), -1, 0, -1);
        TL.createTreeLeaves(build, location, tree.getWidth() * 3, tree.getWidth() * 2, tree.getDensity(), 0, 0, 1);
        TL.createTreeLeaves(build, location, tree.getWidth() * 3, tree.getWidth() * 2, tree.getDensity(), 1, 0, 0);
        TL.createTreeLeaves(build, location, tree.getWidth() * 3, tree.getWidth() * 2, tree.getDensity(), 1, 0, 1);
        TL.createTreeLeaves(build, location, tree.getWidth() * 3, tree.getWidth() * 2, tree.getDensity(), 0, 1, 0);
        TL.createTreeLeaves(build, location, tree.getWidth() * 3, tree.getWidth() * 2, tree.getDensity(), 0, -1, 0);
        TL.createTreeLeaves(build, location, tree.getWidth() * 3, tree.getWidth() * 2, tree.getDensity(), 0, 0, 0);
    }
    /**
     * Waits for a specified period of time (in milliseconds)
     * @param time time in milliseconds
     */
    private void wait(int time){
        try{
            Thread.sleep(time);
        }catch(Exception e){
            GiantTreesRevived.logWarning("Unable to wait!");
            e.printStackTrace();
        }
    }
    /**
     * There is currently trees in the cache or a tree being built
     * @return true or false
     */
    public boolean isRunning(){
        return running;
    }
    /**
     * Sends a message to a player if there is a player.
     * Avoids error if the tree being created doesn't have a specified owner
     * @param message message to send
     * @param player player to send to
     */
    private void say(String message, Player player){
        if(player != null){
            player.sendMessage(message);
        }
    }
    /**
     * Set whether to save tree files or not
     * @param b true or false
     */
    public void setSave(boolean b){
        save = b;
    }
    /**
     * Set the wait after creation time
     * @param time the time (milliseconds)
     */
    public void setWaitAfterCreation(int time){
        waitAfterCreation = time;
    }
    /**
     * Set the delay in ticks between placing blocks when building a tree
     * @param time ticks
     */
    public void setTreeBuildDelay(int time){
        treeBuildDelay = time;
    }
}
