package treadmakestop;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author brett
 */
public class WorkFile {

    /**
     * Starts a new instance of WorkFile
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        new WorkFile();

    }

    /**
     * Starts the gul building process.
     */
    public WorkFile() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//                   UIManager.setLookAndFeel(info.getClassName());
                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        System.out.println(info.getName());
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }

                    }// UIManager.put("ProgressBar.selectionBackground", Color.RED);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                }
                JFrame frame = new JFrame("TvCOnvert");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new WorkerGui());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setSize(415, 250);
                frame.setResizable(false);
                frame.setVisible(true);

            }
        });
    }
}
