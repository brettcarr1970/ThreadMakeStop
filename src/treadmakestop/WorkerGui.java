package treadmakestop;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author brett
 */
public class WorkerGui extends JPanel {

    private final JButton browseButton;
    private final JTextField browseTextField;
    private final JButton exitButton;
    private JProgressBar tvpb;
    private JFileChooser fileChooser;
    private JButton startButton;
    private JButton cancelButton;
    private final JLabel progressInfo;
    private final JLabel information;
    private Worker worker;
    private File file;

    /**
     * Creates the GUI components and add them to the WorkerGui panel
     */
    @SuppressWarnings("Convert2Lambda")
    public WorkerGui() {

        startButton = new JButton();
        startButton.setMnemonic('S');
        cancelButton = new JButton();
        cancelButton.setMnemonic('C');
        exitButton = new JButton();
        exitButton.setMnemonic('E');
        browseButton = new JButton();
        browseButton.setMnemonic('B');
        tvpb = new JProgressBar();
        progressInfo = new JLabel();
        information = new JLabel();
        browseTextField = new JTextField();
        information.setText(TOOL_TIP_TEXT_KEY);
        progressInfo.setText("You Eat Doo Doo");
        progressInfo.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        startButton.setEnabled(false);
        cancelButton.setEnabled(false);
        browseButton.setText("Browse");
        startButton.setText("Start");
        cancelButton.setText("Cancel");
        exitButton.setText("Exit");

javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(exitButton)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(browseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseTextField)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progressInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(information, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(tvpb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {startButton, cancelButton, exitButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseButton)
                    .addComponent(browseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(information, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tvpb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(cancelButton)
                    .addComponent(exitButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tvpb.addChangeListener((ChangeEvent e) -> {
            System.out.println(tvpb.getValue());
            if (tvpb.getValue() >= 100) {
                worker = null;
                startButton.setEnabled(false);
                cancelButton.setEnabled(false);
                file = null;
                browseTextField.setText(null);
                setProgressInfo("WORKFILE IS CREATED... ");
            }
        });

        tvpb.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println(tvpb.getValue());
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setProgressInfo("");
                fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(WorkerGui.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    if (file != null) {
                        browseTextField.setText(file.getAbsolutePath());
                        startButton.setEnabled(true);
                    }
                } else {
                    System.out.println("File access cancelled by user.");
                }
            }
        });

        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setProgressInfo("CREATING WORK COPY OF ORIGINAL FILE");
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if (worker == null) {
                    cancelButton.setEnabled(true);
                    worker = new Worker(tvpb, file);
                    Thread t = new Thread(worker, "WorkFileCreate");
                    t.start();
                    startButton.setEnabled(false);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                worker.pause();
                tvpb.setIndeterminate(true);
                int n = JOptionPane.showConfirmDialog(
                        null,
                        "Sure you want to delete this File Copy?",
                        "Kill Operation",
                        JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    worker.stop();
                    worker = null;
                    file = null;
                    browseTextField.setText(null);
                    tvpb.setIndeterminate(false);
                    startButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    tvpb.setValue(0);
                    setProgressInfo("PROCESS WAS CANCELED WORK FILE DELETED");
                } else if (n == JOptionPane.NO_OPTION) {
                    tvpb.setIndeterminate(false);
                    worker.resume();
                }
            }
        });

        exitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
    }

    /**
     *
     * @param msg Message to the user as file is being re-sampled and copied.
     */
    public final void setProgressInfo(String msg) {
        progressInfo.setText(msg);
    }
}
