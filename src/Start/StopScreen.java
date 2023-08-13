
package Start;

import javax.swing.*;
import java.awt.*;



public class StopScreen {

    private JPanel contentPanel;
    private static boolean instance = false;
    private static JFrame frame;


    StopScreen() {
        if (instance) return;
        instance = true;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame();
        frame.setSize(screenSize);
        frame.setLocation(0, 0);
        frame.setContentPane(contentPanel);
        frame.setBackground(Color.BLACK);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setType(javax.swing.JFrame.Type.UTILITY);
    }


    void show(boolean show) { // throws InterruptedException {
        frame.setVisible(show);
        /* Thread t = new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        t.join(); */
        // frame.setVisible(false);
    }

    boolean isVisible() {
        return frame.isVisible();
    }

}
