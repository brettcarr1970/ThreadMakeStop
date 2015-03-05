/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treadmakestop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JOptionPane;

/**
 *
 * @author brett
 */
public class WorkFileInviroment extends WorkerGui {

    private String fileName;
    private final File WORKINGDIR = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "ConvertedTvFiles" + File.separator);
    private File FileOut;
    WorkerGui wg = new WorkerGui();

    public WorkFileInviroment() {
            
    }
    
    
    

    /**
     *
     * @return checks if the work directory exist and if not creates it while
     * alerting the user
     */
    public File checkWorkDirStatus() {
        try {
              wg.setProgressInfo("CHECKING WORK DIRECTORY EXISTS :" + WORKINGDIR);
            Files.newDirectoryStream(WORKINGDIR.toPath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "The work directory is missing it \n will be created now! \n" + WORKINGDIR, "Directory Missing", 1);
            WORKINGDIR.mkdir();
            wg.setProgressInfo("CHECKING WORK DIRECTORY EXISTS :" + WORKINGDIR);

        }
        return WORKINGDIR;
    }

    /**
     *
     * @param fileIn The file being resample
     * @return returns the work file copied from the users selection
     */
    public File createOutFile(File fileIn) {

        try {
            fileName = fileIn.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            FileOut = new File(WORKINGDIR + File.separator + fileName + ".mp4");
        } catch (NullPointerException ex) {
            System.out.println("Bummer no file");
        }

        return FileOut;
    }
}
