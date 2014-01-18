package io.github.cadika_orade.GiantTreesRevived;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Cad'ika Orade
 */
public class FileHandler {
    
    public final String folder = "plugins" + File.separator + "Giant Trees" + File.separator;
    public final String separator = File.separator;
    /**
     * Handles all file operations in the program
     */
    public FileHandler(){

    }
    /**
     * Does the path exist
     * @param location path
     * @return true or false
     */
    public boolean pathExists(String location){
        File file = new File(folder + location);
        return file.exists();
    }
    /**
     * Gets the folders in a given directory
     * @param location location of the directory
     * @return String array of folders
     */
    public String[] getFolders(String location){
        File path = new File(folder + location);
        String[] items = path.list();
        File temp;
        ArrayList<String> tempList = new ArrayList<String>();
        String[] folders;
        for(int i = 0; i <items.length; i++){
            temp = new File(folder + location + separator + items[i]);
            if(temp.isDirectory()){
                tempList.add(items[i]);
            }
        }
        folders = new String[tempList.size()];
        for(int k = 0; k < tempList.size(); k++){
            folders[k] = tempList.get(k);
        }

        return folders;
    }
    /**
     * Reads a plain text file
     * @param location path
     * @return String Array of data
     */
    public String[] read(String location){
        File file = new File(folder + location);
        try{
            String s = "", full = "";
            BufferedReader in = new BufferedReader(new FileReader(file));
            do{
                full += s + "\n";
                s = in.readLine();
            }while(s != null);
            in.close();
            full = full.substring(1);
            String[] data = full.split("\n");
            return data;
        }catch(Exception e){
            GiantTreesRevived.logSevere("Cannot read file: " + folder + location);
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Writes a plain text file
     * @param data String Array of what to write
     * @param location path
     */
    public void write(String[] data, String location){
        File file = new File(folder + location);
        File path = new File(file.getPath().substring(0, file.getPath().lastIndexOf(File.separator)));
        path.mkdirs();
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for(int i = 0; i < data.length; i++){
                out.write(data[i]);
                out.newLine();
            }
            out.close();
        }catch(Exception e){
            GiantTreesRevived.logSevere("Cannot write to file: " + folder + location);
            e.printStackTrace();
        }
    }
    /**
     * Deletes a file
     * @param location path
     */
    public void delete(String location){
        File file = new File(folder + location);
        boolean success = file.delete();
        if(!success){
            GiantTreesRevived.logWarning("Unable to delete: " + folder + location);
        }
    }
    /**
     * Writes a single file to an archive
     * @param zipLocation path the zip will be
     * @param fileLocation path the file is
     */
    public void writeToArchive(String zipLocation, String fileLocation){
        if(pathExists(folder + zipLocation)){
            delete(folder + zipLocation);
            createZipFile(zipLocation, fileLocation);
        }
        else{
            createZipFile(zipLocation, fileLocation);
        }
        
    }
    /**
     * Extracts and reads one file from an archive
     * @param location path
     * @return String Array of data
     */
    public String[] readFromArchive(String location){
        //Special Thanks: http://www.exampledepot.com/egs/java.util.zip/GetZip.html
        //http://java.sun.com/developer/technicalArticles/Programming/PerfTuning/
        try{
            String worldName = getWorldNameFromPath(location);
            File zip = new File(folder + location);
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)));
            ZipEntry zippedFile = in.getNextEntry();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(folder + "Saves" + File.separator + worldName + File.separator + zippedFile.getName().replace(".zip", ".dat")));
            byte[] buffer = new byte[getZipSize(folder + location)];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();

            String[] data = read("Saves" + File.separator + worldName + File.separator + zippedFile.getName().replace(".zip", ".dat"));
            delete("Saves" + File.separator + worldName + File.separator + zippedFile.getName().replace(".zip", ".dat"));
            return data;
        }catch(Exception e){
            GiantTreesRevived.logSevere("Could not extract file from archive: " + folder + location);
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Creates a zip file
     * @param zipLocation path the zip will be
     * @param fileLocation path the file is
     */
    private void createZipFile(String zipLocation, String fileLocation){
        //Special Thanks: http://www.exampledepot.com/egs/java.util.zip/CreateZip.html
        File zip = new File(folder + zipLocation);
        File file = new File(folder + fileLocation);
        try{
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)));
            out.putNextEntry(new ZipEntry(file.getName()));
            int len;
            byte[] buffer = new byte[(int)file.length()];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
            delete(fileLocation);
        }catch(Exception e){
            GiantTreesRevived.logSevere("Could not create archive: " + folder + zipLocation);
            e.printStackTrace();
        }
    }
    /**
     * Gets the world name from a path
     * @param path path
     * @return world name
     */
    private String getWorldNameFromPath(String path){
        path = path.substring(path.lastIndexOf("Saves" + File.separator) + 6, path.lastIndexOf(File.separator));
        return path;
    }
    /**
     * Gets the size of a zip entry
     * @param path path
     * @return size
     */
    private int getZipSize(String path){
        //thanks: http://vimalathithen.blogspot.com/2006/06/using-zipentrygetsize.html
        try{
            ZipFile zipfile = new ZipFile(path);
            Enumeration zipEnum = zipfile.entries();
            int size = -1;
            while(zipEnum.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry)zipEnum.nextElement();
                size = (int)zipEntry.getSize();
            }

            return size;
        }catch(Exception e){
            GiantTreesRevived.logSevere("Problem getting zip size!");
            e.printStackTrace();
            return -1;
        }
    }
}
