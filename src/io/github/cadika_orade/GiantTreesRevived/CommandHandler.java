package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Cad'ika Orade
 */
public class CommandHandler {

    private CreateTree CT;
    private Undo U;
    private BukkitScheduler BS;

    /**
     * Handles all the commands
     * @param b BukkitScheduler
     */
    public CommandHandler(BukkitScheduler b){
        CT = new CreateTree(b, true, GiantTreesRevived.getSettings().getWaitAfterCreation() * 1000);
        U = new Undo();
        BS = b;
    }
    /**
     * Parses a command a player sends
     * @param player player issuing command
     * @param command command in question
     */
    public void command(Player player, String[] command){
        if(command.length == 0){
            help(player);
        }
        else if(command.length == 1){
            if(command[0].equalsIgnoreCase("help")){
                help(player);
            }
            else if(command[0].equalsIgnoreCase("about")){
                about(player);
            }
            else if(command[0].equalsIgnoreCase("reload")){
                reload(player);
            }
            else if(command[0].equalsIgnoreCase("undo")){
                if(U.isDone() && !CT.isRunning()){
                    U = new Undo(player, player.getLocation(), BS);
                    try{
                        Thread t = new Thread(U);
                        t.start();
                    }catch(Exception e){
                        GiantTreesRevived.logSevere("Cannot start undo!");
                        e.printStackTrace();
                    }
                }
                else{
                    if(CT.isRunning()){
                        player.sendMessage(ChatColor.RED + "Cannot undo a tree while one is being built!");
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Only one undo can be done at once!");
                    }
                }
            }
            else{
                player.sendMessage(ChatColor.RED + "Incorrect command parameter: " + command[0]);
                player.sendMessage(ChatColor.RED + "Please see /gt help for correct usage!");
            }
        }
        else{
            //try and make a tree
            if(command.length == 2 || command.length == 4 || command.length == 5){
                //creates tree object
                Tree temp;
                if(command.length == 2){
                    temp = new Tree(player, command[0], command[1]);
                }
                else if(command.length == 4){
                    temp = new Tree(player, command[0], command[1], command[2], command[3]);
                }
                else{
                    temp = new Tree(player, command[0], command[1], command[2], command[3], command[4]);
                }
                //sees if any rules were broken
                if(temp.getHeight() < 12){
                    player.sendMessage(ChatColor.RED + "Tree must be at least 12 tall");
                }
                else if(temp.getWidth() < 4){
                    player.sendMessage(ChatColor.RED + "Tree must be at least 4 wide");
                }
                else if(temp.getHeight() > GiantTreesRevived.getSettings().getMaximumTreeHeight() && !GiantTreesRevived.checkPermission(player, "nolimit")){
                    player.sendMessage(ChatColor.RED + "You don't have permission to make a tree that tall");
                }
                else if(temp.getWidth() > GiantTreesRevived.getSettings().getMaximumTreeWidth() && !GiantTreesRevived.checkPermission(player, "nolimit")){
                    player.sendMessage(ChatColor.RED + "You don't have permission to make a tree that wide");
                }
                else if(temp.getDensity() > 35 || temp.getDensity() < 0){
                    player.sendMessage(ChatColor.RED + "You can't make a tree with that density!");
                }
                else{
                    if(GiantTreesRevived.checkPermission(player, "build")){
                        if(temp.isValid()){
                            if(temp.getMetaData().isDangerous()){
                                if(GiantTreesRevived.checkPermission(player, "customdangerous")){
                                    CT.addTree(temp);
                                }
                                else{
                                    player.sendMessage(ChatColor.RED + "You don't have permission to use that block");
                                }
                            }
                            else if(temp.getMetaData().getLogType().getId() != 17 || temp.getMetaData().getLeafType().getId() != 18){
                                if(GiantTreesRevived.checkPermission(player, "custom")){
                                    CT.addTree(temp);
                                }
                                else{
                                    player.sendMessage(ChatColor.RED + "You don't have permission to make custom trees");
                                }
                            }
                            else{
                                CT.addTree(temp);
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.RED + "Invalid tree!");
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "You don't have permission to make trees!");
                    }
                }
            }
            else{
                player.sendMessage(ChatColor.RED + "Invalid command format - did you specify log and leaf?");
            }
        }
    }
    /**
     * Help section of the program
     * @param player player to help
     */
    private void help(Player player){
        player.sendMessage(ChatColor.GREEN + "Command: /gt, /gtree, or /gianttree");
        player.sendMessage(ChatColor.GREEN + "Undo a Tree: /gt undo  - while standing near the tree");
        player.sendMessage(ChatColor.GREEN + "Reload Settings: /gt reload");
        player.sendMessage(ChatColor.GREEN + "Make a Tree: /gt <height> <width> <log> <leaf> <density>");
        player.sendMessage(ChatColor.GREEN + "----------------------------------------------------");
        player.sendMessage(ChatColor.GREEN + "<height> -  must be at least 4 larger than 3 times the width,");
        player.sendMessage(ChatColor.GREEN + "and may not exceed the height of the map. The height includes");
        player.sendMessage(ChatColor.GREEN + "the trunk and treetop.");
        player.sendMessage(ChatColor.GREEN + "<width> -  must be greater than 3; odd-sized widths ");
        player.sendMessage(ChatColor.GREEN + "will be moved up by 1 size (ex. 5 -> 6).");
        player.sendMessage(ChatColor.GREEN + "<log> and <leaf> can be block id's or a log/leaf type");
        player.sendMessage(ChatColor.GREEN + "Ex. birch, spruce, oak, or jungle");
        player.sendMessage(ChatColor.GREEN + "<density> - How dense leaf coverage is (0-35) - 20 is default");

    }
    /**
     * About section of the program
     * @param player player to send to
     */
    private void about(Player player){
        player.sendMessage(ChatColor.GREEN + "Giant Trees! Version " + GiantTreesRevived.getVersion());
        player.sendMessage(ChatColor.GREEN + "Created By Connor Mahaffey");
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "Check out https://github.com/CMahaff/Giant-Trees/wiki");
    }
    /**
     * Reloads all settings for the program. For most settings, the server will need
     * to be restarted
     * @param player player issuing command
     */
    private void reload(Player player){
        if(GiantTreesRevived.checkPermission(player, "reload")){
            GiantTreesRevived.getSettings().loadSettings();
            player.sendMessage(ChatColor.GREEN + "Config reloaded!");
        }
        else{
            player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
        }
    }

}
