package io.github.cadika_orade.GiantTreesRevived;

import java.util.ArrayList;

/**
 *
 * @author Cad'ika Orade
 */
public class Settings {

    private String[] settings;

    private final String[] config = {"Giant Trees Config for version " + GiantTreesRevived.getSettingsVersion(),
                                    "For more information see: https://github.com/CMahaff/Giant-Trees/wiki/Settings",
                                    "---Settings for creating trees---",
                                    "Maximum Tree Height:255",
                                    "Maximum Tree Width:16",
                                    "Tree Build Delay:0",
                                    "Wait between tree creation:0",
                                    "---Settings for planting trees---",
                                    "Allow Giant Trees to be planted(Y/N):N",
                                    "Random Chance Grow(Y/N):N",
                                    "Percent Random Chance:15",
                                    "Block Near Grow (Y/N):N",
                                    "Block Near Grow ID:41",
                                    "Minimum Height:24",
                                    "Minimum Width:4",
                                    "Maximum Height:34",
                                    "Maximum Width:6",
                                    "Message when planted:Power radiates from the sapling...",
                                    "Wait after tree grow:10",
                                    "---Settings for naturally occuring giant trees---",
                                    "Allow naturally occurring giant trees(Y/N):N",
                                    "Percent Chance (per chunk):5",
                                    "Minimum Height:24",
                                    "Minimum Width:4",
                                    "Maximum Height:34",
                                    "Maximum Width:6",
                                    "Wait after tree occur:20",
                                    "Occur on Worlds:world",
                                    "Show X/Z Location in Console(Y/N):N"};

    /**
     * Holds all the settings for the program
     */
    public Settings(){

    }
    /**
     * Loads all the settings for the program. If the version information doesn't
     * match up or the config is the wrong lenght, the default values are written.
     */
    public void loadSettings(){
        String version;
        FileHandler FH = new FileHandler();
        if(!FH.pathExists("config.txt")){
            writeDefaultConfig();
            settings = new String[config.length];
            settings = config;
        }
        else{
            String[] dat = FH.read("config.txt");
            if(dat.length == config.length){
                version = dat[0];
                if(!version.equals("Giant Trees Config for version " + GiantTreesRevived.getSettingsVersion())){
                    GiantTreesRevived.logInfo("Writing new config file!");
                    writeDefaultConfig();
                    settings = new String[config.length];
                    settings = config;
                }
                else{
                    settings = new String[dat.length];
                    settings = dat;
                }
            }
            else{
                GiantTreesRevived.logSevere("Config file written incorrectly! Writing default values!");
                writeDefaultConfig();
                settings = new String[config.length];
                settings = config;
            }
        }
    }
    /**
     * Gets an integer from a setting value
     * @param s the setting string
     * @return the int
     */
    private int getInt(String s){
        int x = -1;

        try{
            s = s.substring(s.indexOf(":") + 1, s.length());
            x = Integer.parseInt(s);
        }catch(Exception e){
            GiantTreesRevived.logSevere("Config file written incorrectly! Value: " + s);
        }

        return x;
    }
    /**
     * Gets a boolean from a setting value
     * @param s the setting string
     * @return the boolean
     */
    private boolean getBool(String s){
        boolean x = false;

        try{
            s = s.substring(s.indexOf(":") + 1, s.length());
            if(s.equalsIgnoreCase("y")){
                x = true;
            }
        }catch(Exception e){
            GiantTreesRevived.logSevere("Config file written incorrectly! Value: " + s);
        }

        return x;
    }
    /**
     * Gets a string from a setting value
     * @param s the setting string
     * @return the string value
     */
    private String getString(String s){
        try{
            s = s.substring(s.indexOf(":") + 1, s.length());
        }catch(Exception e){
            GiantTreesRevived.logSevere("Config file written incorrectly! Value: " + s);
        }

        return s;
    }
    /**
     * Gets a string ArrayList from a setting value
     * @param s the setting string
     * @return the ArrayList
     */
    private ArrayList<String> getList(String s){
        ArrayList<String> list = new ArrayList<String>();
        try{
            s = s.substring(s.indexOf(":") + 1, s.length());
            s = s.replace(" ", "");
            String[] dat = s.split(",");
            for(int i = 0; i < dat.length; i++){
                if(!dat[i].equals("")){
                    list.add(dat[i]);
                }
            }
        }catch(Exception e){
            GiantTreesRevived.logSevere("Config file written incorrectly! Value: " + s);
        }
        
        return list;
    }
    /**
     * Writes the default config to file
     */
    private void writeDefaultConfig(){
        FileHandler FH = new FileHandler();
        FH.write(config, "config.txt");
    }
    /**
     * Maximum Tree Height Setting
     * @return Maximum Tree Height
     */
    public int getMaximumTreeHeight(){
        return getInt(settings[3]);
    }
    /**
     * Maximum Tree Width Setting
     * @return Maximum Tree Width
     */
    public int getMaximumTreeWidth(){
        return getInt(settings[4]);
    }
    /**
     * Tree build delay
     * @return delay
     */
    public int getTreeBuildDelay(){
        return getInt(settings[5]);
    }
    /**
     * Wait after creation of a tree
     * @return time
     */
    public int getWaitAfterCreation(){
        return getInt(settings[6]);
    }
    /**
     * Giant Trees can be planted
     * @return true or false
     */
    public boolean allowGiantTreePlant(){
        return getBool(settings[8]);
    }
    /**
     * Giant Trees planted can randomly grow
     * @return true or false
     */
    public boolean allowRandomChanceGrow(){
        return getBool(settings[9]);
    }
    /**
     * The percent chance a giant tree will randomly grow
     * @return
     */
    public int getPercentRandomChanceGrow(){
        return getInt(settings[10]);
    }
    /**
     * Giant Trees will grow if a specified block is near them
     * @return true or false
     */
    public boolean allowBlockNearGrow(){
        return getBool(settings[11]);
    }
    /**
     * The ID a giant tree must grow near
     * @return ID
     */
    public int getBlockNearGrowId(){
        return getInt(settings[12]);
    }
    /**
     * The minimum height a tree will grow
     * @return height
     */
    public int getMinimumGrowHeight(){
        return getInt(settings[13]);
    }
    /**
     * The minimum width a tree will grow
     * @return width
     */
    public int getMinimumGrowWidth(){
        return getInt(settings[14]);
    }
    /**
     * The maximum height a tree will grow
     * @return height
     */
    public int getMaximumGrowHeight(){
        return getInt(settings[15]);
    }
    /**
     * The maximum width a tree will grow
     * @return width
     */
    public int getMaximumGrowWidth(){
        return getInt(settings[16]);
    }
    /**
     * Message displayed when a tree is about to grow
     * @return message
     */
    public String getGrowMessage(){
        return getString(settings[17]);
    }
    /**
     * Wait time after a tree grows
     * @return time
     */
    public int getWaitAfterGrow(){
        return getInt(settings[18]);
    }
    /**
     * Trees can naturally spawn in the world
     * @return
     */
    public boolean allowNaturallyOccurring(){
        return getBool(settings[20]);
    }
    /**
     * The percent chance a tree will spawn in the world
     * @return
     */
    public int getPercentChanceOccur(){
        return getInt(settings[21]);
    }
    /**
     * The minimum occur height
     * @return height
     */
    public int getMinimumOccurHeight(){
        return getInt(settings[22]);
    }
    /**
     * The minimum occur width
     * @return
     */
    public int getMinimumOccurWidth(){
        return getInt(settings[23]);
    }
    /**
     * The maximum occur height
     * @return height
     */
    public int getMaximumOccurHeight(){
        return getInt(settings[24]);
    }
    /**
     * The maximum occur width
     * @return width
     */
    public int getMaximumOccurWidth(){
        return getInt(settings[25]);
    }
    /**
     * Wait time after occuring
     * @return time
     */
    public int getWaitAfterOccur(){
        return getInt(settings[26]);
    }
    /**
     * List of worlds trees will naturally occur on
     * @return String ArrayList of world names
     */
    public ArrayList<String> getOccurWorldList(){
        return getList(settings[27]);
    }
    /**
     * Get if the locations of trees being populated should be shown in console
     * @return show or do not show
     */
    public boolean showShowXZLocationConsole(){
        return getBool(settings[28]);
    }
}
