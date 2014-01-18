package io.github.cadika_orade.GiantTreesRevived;

import org.bukkit.Material;

/**
 *
 * @author Cad'ika Orade
 */
public class MetaData {

    private boolean valid = true;
    private Material logType, leafType;
    private int logMeta = -1, leafMeta = -1;
    private final int[] bannedList = {0, 6, 26, 27, 28, 31, 32, 34, 36, 37, 38, 39, 40, 50,
                                      55, 59, 63, 64, 65, 66, 68, 69, 70, 71, 72, 75, 76,
                                      77, 78, 81, 83, 85, 90, 93, 94, 96};
    private final int[] dangerousList = {7, 8, 9, 10, 11, 12, 13, 23, 29, 33, 44, 46, 51,
                                         52, 53, 54, 67, 79};

    /**
     * Holds the metadata/material for a tree
     * Spruce, birch, or oak
     * Or a block ID
     * @param logt log type
     * @param leaft leaf type
     */
    public MetaData(String logt, String leaft){
        if(isInt(logt)){
            logType = Material.getMaterial(Integer.parseInt(logt));
        }
        else{
            logType = Material.LOG;
            logMeta = getMeta(logt);
        }

        if(isInt(leaft)){
            leafType = Material.getMaterial(Integer.parseInt(leaft));
        }
        else{
            leafType = Material.LEAVES;
            leafMeta = getMeta(leaft);
        }

        if(logType == null || leafType == null){
            valid = false;
        }
        else if(contains(logType.getId(), bannedList) ||
                contains(leafType.getId(), bannedList) ||
                logType.getId() >= 115 || leafType.getId() >= 115
                || logType.getId() < 0 || leafType.getId() < 0){
            valid = false;
        }
    }
    /**
     * Is a valid type of block
     * @return true or false
     */
    public boolean isValid(){
        return valid;
    }
    /**
     * The log type
     * @return log type
     */
    public Material getLogType(){
        return logType;
    }
    /**
     * The leaf type
     * @return leaf type
     */
    public Material getLeafType(){
        return leafType;
    }
    /**
     * The log type is spruce, birch, or oak
     * @return true or false
     */
    public boolean hasLogMeta(){
        if(logMeta == -1){
            return false;
        }
        else{
            return true;
        }
    }
    /**
     * The leaf type is spruce, birch, or oak
     * @return true or false
     */
    public boolean hasLeafMeta(){
        if(leafMeta == -1){
            return false;
        }
        else{
            return true;
        }
    }
    /**
     * The log metadata
     * @return byte data
     */
    public byte getLogMeta(){
        return (byte)logMeta;
    }
    /**
     * The leaf metadata
     * @return byte data
     */
    public byte getLeafMeta(){
        return (byte)leafMeta;
    }
    /**
     * Int type of metadata for a given type (spruce, birch, oak)
     * 0 for none
     * @param s type
     * @return byte in int form
     */
    public int getMeta(String s){
        if(s.equalsIgnoreCase("oak") || s.equalsIgnoreCase("normal")){
            return 0;
        }
        else if(s.equalsIgnoreCase("spruce")){
            return 1;
        }
        else if(s.equalsIgnoreCase("birch")){
            return 2;
        }
        else if(s.equalsIgnoreCase("jungle")){
            return 3;
        }
        else{
            valid = false;
            return 0;
        }
    }
    /**
     * Checks if the log or leaf type is on the dangerous list
     * @return true or false
     */
    public boolean isDangerous(){
        if(contains(logType.getId(), dangerousList) || contains(leafType.getId(), dangerousList)){
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * Given String is an int
     * @param s string
     * @return true or false
     */
    private boolean isInt(String s){
        try{
            int a = Integer.parseInt(s);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    /**
     * Sees if an int is in an int array
     * Used for checking dangerous blocks / banned blocks
     * @param type the int
     * @param data the array
     * @return true or false
     */
    private boolean contains(int type, int[] data){
        for(int i = 0; i < data.length; i++){
            if(data[i] == type){
                return true;
            }
        }
        return false;
    }

}
