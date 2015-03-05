/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treadmakestop;

import javax.swing.JLabel;

/**
 *
 * @author brett
 */
public class WorkFileProgressLabel {
    private final JLabel PU;
    private String[] updates;

    /**
     *
     * @param progressInfo The label that give the user the status of operation.
     */
    public WorkFileProgressLabel(JLabel progressInfo){
        PU = progressInfo;
        updates[0] = "CREATING WORK COPY OF ORIGINAL FILE";
        updates[1] = "WORKFILE IS CREATED... ";
        updates[2] = "STARTING RESAMPLE";
        
    }
    
    /**
     *
     * @param update the number of the array item that contains text for the update label
     */
    public void updateLabel(int update){
        PU.setText(updates[update]);
    }
    
    
    
    /**
     *
     */
    
    
    
    
}
