package io.github.cadika_orade.GiantTreesRevived;

import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Cad'ika Orade
 */
public class GiantTreesRevived extends JavaPlugin{

    private static final String version = "0.4.3";//remember to change plugin.yml version too!
    private static final String settingsVersion = "0.4";
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Plugin GiantTreesRevived;
    private static Settings S;
    private CommandHandler CH;
    private Load L;
    private PluginManager PM;
    private ChunkHandler ChH;
    private BlockPlaceHandler BPH;

    /**
     * Code to run when disabled
     */
    @Override
    public void onDisable() {
        log.info("[Giant Trees] version " + version + " is disabled");
    }
    /**
     * Code to run when enabled. Loads settings, worlds, turns on necessary events,
     * and sets up permissions
     */
    @Override
    public void onEnable() {
        GiantTreesRevived = this;
        S = new Settings();
        S.loadSettings();
        L = new Load();
        L.convert();
        L.load(getServer().getWorlds());
        CH = new CommandHandler(getServer().getScheduler());
        PM = getServer().getPluginManager();
        if(S.allowNaturallyOccurring()){
            ChH = new ChunkHandler(getServer().getScheduler(), getServer().getWorlds());
            PM.registerEvents(ChH, this);
        }
        if(S.allowGiantTreePlant()){
            BPH = new BlockPlaceHandler(getServer().getScheduler());
            PM.registerEvents(BPH, this);
        }
        log.info("[Giant Trees] version " + version + " is enabled");
    }
    /**
     * Code to run when a command is used
     * @param sender sent the command
     * @param cmd command
     * @param commandLabel front of the command
     * @param args arguements of the command
     * @return command used properly or not
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(commandLabel.equalsIgnoreCase("gianttree") || commandLabel.equalsIgnoreCase("gtree") || commandLabel.equalsIgnoreCase("gt")){
            if(sender instanceof Player){
                CH.command((Player)sender, args);
            }
            else{
                logWarning("You cannot execute commands from console");
            }
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * Plugin object version of Giant Trees
     * @return Giant Trees plugin object
     */
    protected static Plugin getPlugin(){
        return GiantTreesRevived;
    }
    /**
     * Add an info message to the log
     * @param content what to be sent
     */
    protected static void logInfo(String content){
        log.info("[Giant Trees] " + content);
    }
    /**
     * Add a warning message to the log
     * @param content what to be sent
     */
    protected static void logWarning(String content){
        log.warning("[Giant Trees] " + content);
    }
    /**
     * Add a severe message to the log
     * @param content what to be sent
     */
    protected static void logSevere(String content){
        log.severe("[Giant Trees] " + content);
    }
    /**
     * Version of Giant Trees running
     * @return Giant Trees version
     */
    protected static String getVersion(){
        return version;
    }
    /**
     * Bug fixes change the version number, but not the settings. A new version would over-write old settings,
     * this fixes that.
     * @return settings version
     */
    protected static String getSettingsVersion(){
        return settingsVersion;
    }
    /**
     * Instance of the Settings
     * @return Settings
     */
    protected static Settings getSettings(){
        return S;
    }
    /**
     * Checks if a user has permission to do something
     * @param player player in question
     * @param perm permission in question
     * @return true or false
     */
    protected static boolean checkPermission(Player player, String perm){
        return player.hasPermission("gianttrees." + perm);
    }
}
