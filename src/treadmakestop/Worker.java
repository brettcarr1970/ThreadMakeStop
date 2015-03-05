package treadmakestop;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author brett
 */
public class Worker implements Runnable {

    private final ReentrantLock PAUSELOCK;
    private final Condition PAUSECONDITION;
    private final AtomicBoolean PAUSED;
    private final AtomicBoolean KEEPRUNNING;
    private final WorkFileInviroment WFI;
    private File FILEIN;
    private final File FILEOUT;
    private final JProgressBar TVPG;
    private long LENGTH = 0;
    private final byte[] b = new byte[1024];
    private FileOutputStream FOUTS;
    private FileInputStream FINS;
    private int r;

    private long Counter = 1;

    /**
     *
     * @param tvpg Progress bar to pass to the worker
     * @param file File selected from the file chooser to be worked on
     */
    public Worker(JProgressBar tvpg, File file) {
        WFI = new WorkFileInviroment();
        PAUSED = new AtomicBoolean();
        KEEPRUNNING = new AtomicBoolean(true);
        PAUSELOCK = new ReentrantLock();
        PAUSECONDITION = PAUSELOCK.newCondition();
        TVPG = tvpg;
        FILEIN = file;
        FILEOUT = WFI.createOutFile(FILEIN);
        LENGTH = FILEIN.length();

    }

    /**
     * This is the main thread and copy file operation
     */
    @Override
    public void run() {
        System.out.println("Runnable has started");
        WFI.checkWorkDirStatus();
        int progress;
        try {
            createFirstFileStream();
            while ((r = FINS.read(b)) != -1 && KEEPRUNNING.get()) {
                checkPauseState();
                Counter += r;
                FOUTS.write(b, 0, r);
                progress = (int) Math.round(100 * Counter / LENGTH);
                updateProgress(Math.min(progress, 100));
            }
            killFileStream();
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Runnable has exited");
    }

    /**
     *
     * @throws IOException Throws an error if can't create file streams
     */
    public void createFirstFileStream() throws IOException {
        FINS = new FileInputStream(FILEIN);
        FOUTS = new FileOutputStream(FILEOUT);
    }

    /**
     *
     * @return the status of paused.
     */
    public boolean isPaused() {
        return PAUSED.get();
    }

    /**
     * Pauses the Main copy Thread.
     */
    public void pause() {
        PAUSED.set(true);
    }

    /**
     * Resumes the thread if it is paused.
     */
    public void resume() {
        PAUSED.set(false);
        PAUSELOCK.lock();
        try {
            PAUSECONDITION.signal();
        } finally {
            PAUSELOCK.unlock();
        }
    }

    /**
     * Checks what state the thread is in as in SLEEPING or RUNNING.
     */
    protected void checkPauseState() {
        while (PAUSED.get()) {
            PAUSELOCK.lock();
            try {
                PAUSECONDITION.await();
            } catch (Exception e) {
            } finally {
                PAUSELOCK.unlock();
            }
        }
    }

    /**
     *
     * @param progress returns the progress of the progress of the file copy
     */
    protected void updateProgress(int progress) {
        if (EventQueue.isDispatchThread()) {
            TVPG.setValue(progress);
            if (KEEPRUNNING.get() == false) {
                TVPG.setValue(0);
            }
        } else {
                SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateProgress(progress);
                }
            });
        }
    }

    /**
     *
     * @throws IOException error if Streams or file doesn't exist.
     */
    public void killFileStream() throws IOException {
        FILEIN = null;
        FINS.close();
        FOUTS.flush();
        FOUTS.close();
    }

    /**
     * Stops the thread completly
     */
    public synchronized void stop() {
        KEEPRUNNING.set(false);
        resume();
    }
}
