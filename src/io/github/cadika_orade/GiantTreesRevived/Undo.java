package io.github.cadika_orade.GiantTreesRevived;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Cad'ika Orade
 */
public class Undo implements Runnable{

    private FileHandler FH;
    private ArrayList<Location> treeLocations;
    private Player player;
    private Location location;
    private BukkitScheduler BS;
    private boolean done;

    /**
     * Gets rid of an un-wanted tree from file and the world
     */
    public Undo(){
        done = true;
    }
    /**
     * Gets rid of an un-wanted tree from file and the world
     * @param p player undo-ing the tree
     * @param loc location of the player
     * @param b BukkitScheduler
     */
    public Undo(Player p, Location loc, BukkitScheduler b){
        FH = new FileHandler();
        treeLocations = new ArrayList<Location>();
        player = p;
        location = loc;
        BS = b;
        done = false;
    }
    /**
     * Finds the closest tree, removes it, then deletes the file
     */
    public void run(){
        if(FH.pathExists("Saves" + FH.separator + player.getWorld().getName() + FH.separator + "Trees.dat")){
            String[] data = FH.read("Saves" + FH.separator + player.getWorld().getName() + FH.separator + "Trees.dat");
            ArrayList<String> treeNumbers = new ArrayList<String>();
            ArrayList<String> players = new ArrayList<String>();
            for(int i = 0; i < data.length; i += 5){
                treeLocations.add(new Location(player.getWorld(), getNum(data[i + 1]), getNum(data[i + 2]), getNum(data[i + 3])));
                treeNumbers.add(data[i + 4]);
                players.add(data[i]);
            }
            int closest = getClosest(location);
            if(closest != -1){
                int treeFile = (int)getNum(treeNumbers.get(closest));
                String owner = getOwner(players.get(closest));
                if(owner.equalsIgnoreCase(player.getName()) || GiantTreesRevived.checkPermission(player, "undoall")){
                    player.sendMessage(ChatColor.GREEN + "Starting undo...");
                    doUndo(player.getWorld(), FH.readFromArchive("Saves" + FH.separator + player.getWorld().getName() + FH.separator + "Tree" + treeFile + ".zip"));
                    FH.delete("Saves" + FH.separator + player.getWorld().getName() + FH.separator + "Tree" + treeFile + ".zip");
                    String[] newData = getNewData(data, closest * 5);
                    if(newData.length > 0){
                        FH.write(newData, "Saves" + FH.separator + player.getWorld().getName() + FH.separator + "Trees.dat");
                    }
                    else{
                        FH.delete("Saves" + FH.separator + player.getWorld().getName() + FH.separator + "Trees.dat");
                    }

                    player.sendMessage(ChatColor.GREEN + "Tree has been undone!");
                    done = true;
                }
                else{
                    player.sendMessage(ChatColor.RED + "This tree is owned by " + owner);
                    done = true;
                }
            }
            else{
                player.sendMessage(ChatColor.RED + "No tree to undo within 25 blocks!");
                done = true;
            }
        }
        else{
            player.sendMessage(ChatColor.RED + "You haven't created any trees on this world yet!");
            done = true;
        }
    }
    /**
     * Creates new data file of tree locations to be written to file
     * @param data the tree location data
     * @param spot the spot to be removed
     * @return the new data
     */
    private String[] getNewData(String[] data, int spot){
        String[] newData = new String[data.length - 5];
        int newDataSpot = 0;
        for(int i = 0; i < data.length; i += 5){
            if(spot != i){
                newData[newDataSpot] = data[i];
                newData[newDataSpot + 1] = data[i + 1];
                newData[newDataSpot + 2] = data[i + 2];
                newData[newDataSpot + 3] = data[i + 3];
                newData[newDataSpot + 4] = data[i + 4];
                newDataSpot += 5;
            }
        }

        return newData;
    }
    /**
     * Removes the tree from the world
     * @param world the world
     * @param data the tree's data
     */
    private void doUndo(World world, String[] data){
        ArrayList<Block> blocks = new ArrayList<Block>();
        ArrayList<Material> materials = new ArrayList<Material>();
        for(int i = 0; i < data.length; i += 4){
            blocks.add(new Location(world, getNum(data[i]), getNum(data[i + 1]), getNum(data[i + 2])).getBlock());
            materials.add(Material.getMaterial((int)getNum(data[i + 3])));
        }
        UndoBlockChange UBC = new UndoBlockChange(blocks, materials);
        BS.scheduleSyncDelayedTask(GiantTreesRevived.getPlugin(), UBC);
        do{
            wait(50);
        }while(!UBC.isDone());
    }
    /**
     * Finds the closest tree to a given location
     * @param loc location of the person
     * @return the number of the tree it is
     */
    private int getClosest(Location loc){
        double closest = 100;
        double x, y, z, average;
        double[] close = {100, 100, 100};
        int treeMatch = 0;
        for(int i = 0; i < treeLocations.size(); i++){
            x = Math.abs(treeLocations.get(i).getX() - loc.getX());
            y = Math.abs(treeLocations.get(i).getY() - loc.getY());
            z = Math.abs(treeLocations.get(i).getZ() - loc.getZ());
            average = (x + y + z) / 3;
            if(average < closest){
                closest = average;
                close[0] = x;
                close[1] = y;
                close[2] = z;
                treeMatch = i;
            }
        }

        if(close[0] > 25 || close[1] > 25 || close[2] > 25){
            return -1;
        }
        else{
            return treeMatch;
        }
    }
    /**
     * Gets the owner's name from file
     * @param s full text
     * @return owner's name
     */
    private String getOwner(String s){
        s = s.replace("Owner:", "");
        return s;
    }
    /**
     * Get a double from a string
     * @param s string
     * @return double
     */
    private double getNum(String s){
        try{
            double d = Double.parseDouble(s);
            return d;
        }catch(Exception e){
            GiantTreesRevived.logSevere("Corrupt File Entry: " + s);
            return 0;
        }
    }
    /**
     * Wait a specified amount of time (milliseconds)
     * @param time time to wait
     */
    private void wait(int time){
        try{
            Thread.sleep(time);
        }catch(Exception e){
            GiantTreesRevived.logSevere("Could not wait!");
            e.printStackTrace();
        }
    }
    /**
     * Whether the undo is done or not
     * @return true or false
     */
    public boolean isDone(){
        return done;
    }

}
