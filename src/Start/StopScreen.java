
package Start;

import File.SettingsFile;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;



public class StopScreen {

    private JPanel contentPanel;
    private JLabel time;

    private static JWindow window;

    private int sec;
    private boolean show;
    private Timer timer;
    private TimerTask task;
    private static int restTime;


    StopScreen() {
        if (window != null) return;

        changeTimes();
        show = false;

        timer = new Timer();
        task = new TimerTask() {
            final StringBuilder tempTime = new StringBuilder();

            @Override
            public void run() {
                if (!show) return;
                if (sec == restTime) {
                    show = false;
                    return;
                }

                sec++;
                tempTime.setLength(0);
                int temp = sec / 60;
                if (temp != 0)
                    if (temp < 10) tempTime.append(0).append(temp);
                    else tempTime.append(temp);
                else tempTime.append("00");
                tempTime.append(":");
                temp = sec % 60;
                if (temp < 10) tempTime.append(0).append(temp);
                else tempTime.append(temp);

                time.setText(tempTime.toString());
            }
        };
        timer.schedule(task, 1000, 1000);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window = new JWindow();
        window.setSize(screenSize);
        window.setLocation(900, 100);
        window.setContentPane(contentPanel);
        window.setBackground(Color.BLACK);
        window.setAlwaysOnTop(true);
        window.setType(javax.swing.JFrame.Type.UTILITY);
    }


    public static void changeTimes() {
        restTime = 20;
        switch (SettingsFile.getRule()) {
            case 0 -> { restTime = 20; }
            case 1 -> { restTime = 300; }
            case 2 -> { restTime = 600; }
            case 3 -> { restTime = 1200; }
        }
    }


    void show(boolean showIt) {
        sec = 0;
        time.setText("00:00");
        window.setVisible(showIt);
        show = showIt;
    }


    boolean isVisible() {
        return window.isVisible();
    }

}
