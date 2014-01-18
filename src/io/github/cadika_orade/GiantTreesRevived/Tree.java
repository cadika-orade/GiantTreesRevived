package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Cad'ika Orade
 */
public class Tree {

    private boolean valid = true, residingChunk = false;
    private int height, width, density;
    private Player player;
    private MetaData MD;
    private String worldName, playerName;
    private Location location;
    private Chunk chunk;

    /**
     * Holds all the data for a tree - height, width, owner, etc.
     * Assumes defaults for all other parameters
     * @param p owner
     * @param h height
     * @param w width
     */
    public Tree(Player p, String h, String w){
        player = p;
        location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        height = stringToInt(h);
        width = stringToInt(w);
        density = 20;
        MD = new MetaData("17", "18");
        worldName = p.getWorld().getName();
        playerName = p.getName();
    }
    /**
     * Holds all the data for a tree - height, width, owner, etc.
     * Assumes defaults for all other parameters
     * @param p owner
     * @param h height
     * @param w width
     * @param logt log type
     * @param leaft leaf type
     */
    public Tree(Player p, String h, String w, String logt, String leaft){
        player = p;
        location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        height = stringToInt(h);
        width = stringToInt(w);
        density = 20;
        MD = new MetaData(logt, leaft);
        worldName = p.getWorld().getName();
        playerName = p.getName();
    }
    /**
     * Holds all the data for a tree - height, width, owner, etc.
     * @param p owner
     * @param h height
     * @param w width
     * @param logt log type
     * @param leaft leaf type
     * @param d density
     */
    public Tree(Player p, String h, String w, String logt, String leaft, String d){
        player = p;
        location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        height = stringToInt(h);
        width = stringToInt(w);
        density = stringToInt(d);
        MD = new MetaData(logt, leaft);
        worldName = p.getWorld().getName();
        playerName = p.getName();
    }
    /**
     * Holds all the data for a tree - height, width, owner, etc.
     * For no player instances, use a location instead
     * This type of tree will return an owner of null and owner name of ""
     * @param loc location
     * @param h height
     * @param w width
     * @param logt log type
     * @param leaft leaf type
     * @param d density
     */
    public Tree(Location loc, String h, String w, String logt, String leaft, String d){
        player = null;
        location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
        height = stringToInt(h);
        width = stringToInt(w);
        density = stringToInt(d);
        MD = new MetaData(logt, leaft);
        worldName = loc.getWorld().getName();
        playerName = "";
    }
    /**
     * Is a valid size, type, etc.
     * @return true or false
     */
    public boolean isValid(){
        return valid && MD.isValid();
    }
    /**
     * The player who made the tree
     * @return player
     */
    public Player getPlayer(){
        return player;
    }
    /**
     * The name of the player who made the tree
     * @return name
     */
    public String getPlayerName(){
        return playerName;
    }
    /**
     * The location the tree will be centered on
     * @return location
     */
    public Location getLocation(){
        return location;
    }
    /**
     * The name of the world the tree is on
     * @return world name
     */
    public String getWorldName(){
        return worldName;
    }
    /**
     * Height of the tree
     * @return height
     */
    public int getHeight(){
        return height;
    }
    /**
     * The width of the tree
     * @return width
     */
    public int getWidth(){
        return width;
    }
    /**
     * The density of the tree
     * @return density
     */
    public int getDensity(){
        return density;
    }
    /**
     * The tree's metadata
     * @return MetaData object
     */
    public MetaData getMetaData(){
        return MD;
    }
    /**
     * Converts a string to an int
     * @param s the string
     * @return the int
     */
    private int stringToInt(String s){
        int x = -1;

        try{
            x = Integer.parseInt(s);
        }catch(Exception e){
            valid = false;
        }

        return x;
    }
    /**
     * Chunk this tree resides in. This fixes issues with TreePopulator spawning trees in non-active chunks.
     * This may eventually be needed for all events, but as it is right now, Bukkit seems to know if you are
     * changing blocks in a chunk and not to close it, so its only needed when chunks have just been generated
     * and aren't necessarily open.
     * @param c Chunk this tree resides in
     */
    public void setResidingChunk(Chunk c){
        chunk = c;
        residingChunk = true;
    }
    /**
     * Does this tree have a record of which chunk it resides in
     * @return has a chunk reference
     */
    public boolean hasResidingChunk(){
        return residingChunk;
    }
    /**
     * Get the chunk this tree resides in
     * @return chunk this tree resides in
     */
    public Chunk getResisidingChunk(){
        return chunk;
    }
}
