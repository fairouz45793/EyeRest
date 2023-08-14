
package Start;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;



public class Start {

    private JPanel mainPanel;
    private JCheckBox resumeAfter;
    private JLabel time;
    private JLabel pop;
    private JTextField resumeTime;

    private JLabel closeBtn;
    private JLabel restartBtn;
    private JLabel pauseBtn;
    private JLabel settings;

    private static boolean instance = false;
    private static boolean isPaused = false;
    private static boolean isPop1 = true;

    private static final int x = Toolkit.getDefaultToolkit().getScreenSize().width - 15;
    private static int stopTime = 0;
    private static int min = 0, sec = 0;

    private static final ImageIcon popIcon1 = new ImageIcon("src/icons/pop_demi.png");
    private static final ImageIcon popIcon2 = new ImageIcon("src/icons/pop_comp.png");
    private static final ImageIcon pauseIcon = new ImageIcon("src/icons/pause.png");
    private static final ImageIcon playIcon = new ImageIcon("src/icons/play.png");


    public Start() {
        if (instance) return;
        instance = true;
        JFrame frame = new JFrame();

        JWindow menu = new JWindow();
        pop.setIcon(popIcon1);
        // make pop button draggable at the right side
        pop.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                if (menu.isVisible()) menu.setVisible(false);
                frame.setLocation(x, MouseInfo.getPointerInfo().getLocation().y);
            }
        });

        pop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (isPop1) {
                        pop.setIcon(popIcon2);
                        int xIndx = x - 257;
                        for (int i = x; i > xIndx; i -= 2) {
                            frame.setLocation(i, frame.getLocationOnScreen().y);
                            Thread.sleep(1);
                        }
                        // frame.setLocation(x - 256, frame.getLocationOnScreen().y);
                    } else {
                        pop.setIcon(popIcon1);
                        // frame.setLocation(x, frame.getLocationOnScreen().y);
                        for (int i = x - 257; i < x + 1; i += 2) {
                            frame.setLocation(i, frame.getLocationOnScreen().y);
                            Thread.sleep(1);
                        }
                    }
                    menu.setVisible(false);
                    isPop1 = !isPop1;
                } catch (InterruptedException ex) {

                }
            }
        });

        closeBtn = new JLabel();
        restartBtn = new JLabel();
        pauseBtn = new JLabel();
        settings = new JLabel();
        closeBtn.setOpaque(false);
        restartBtn.setOpaque(false);
        pauseBtn.setOpaque(false);
        settings.setOpaque(false);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 2));
        contentPane.add(closeBtn, BorderLayout.NORTH);
        contentPane.add(pauseBtn, BorderLayout.CENTER);
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout(0, 2));
        tempPanel.setOpaque(false);
        tempPanel.add(restartBtn, BorderLayout.NORTH);
        tempPanel.add(settings, BorderLayout.SOUTH);
        contentPane.add(tempPanel, BorderLayout.SOUTH);
        menu.setContentPane(contentPane);
        menu.setBackground(new Color(0, 0, 0, 0));
        menu.setSize(30, 108);
        menu.setOpacity(0.5f);
        menu.setVisible(false);

        closeBtn.setIcon(new ImageIcon("src/icons/close.png"));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        pauseBtn.setIcon(pauseIcon);
        pauseBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPaused) {
                    pauseBtn.setIcon(pauseIcon);
                    if (resumeAfter.isSelected()) resumeAfter.setSelected(false);
                } else {
                    pauseBtn.setIcon(playIcon);
                }
                isPaused = !isPaused;
            }
        });

        restartBtn.setIcon(new ImageIcon("src/icons/restart.png"));
        restartBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                min = 0;
                sec = 0;
                time.setText("00:00");
            }
        });

        settings.setIcon(new ImageIcon("src/icons/settings.png"));
        settings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent) {
                    if (event.getID() == MouseEvent.MOUSE_ENTERED) {
                        Object source = event.getSource();
                        if (source == pauseBtn || source == restartBtn || source == closeBtn || source == settings) return;
                        if (source == pop) {
                            if (isPop1) menu.setLocation(pop.getLocationOnScreen().x - 7, pop.getLocationOnScreen().y + 30);
                            else menu.setLocation(pop.getLocationOnScreen().x, pop.getLocationOnScreen().y + 32);
                            menu.setVisible(true);
                        } else menu.setVisible(false);
                    }
            }
            if (event instanceof WindowEvent) {
                if (event.getID() == WindowEvent.WINDOW_DEACTIVATED || event.getID() == WindowEvent.WINDOW_STATE_CHANGED) {
                    if (menu.isVisible()) menu.setVisible(false);
                }
            }
        }, AWTEvent.WINDOW_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);


        java.util.Timer timer = new Timer();
        StopScreen stopScreen = new StopScreen();
        TimerTask task = new TimerTask() {
            final StringBuilder tempTime = new StringBuilder();

            @Override public void run() {
                if (stopTime != 0) {
                    stopTime--;
                    return;
                } else {
                    if (resumeAfter.isSelected()) {
                        pauseBtn.setIcon(pauseIcon);
                        resumeAfter.setSelected(false);
                    }
                    if (stopScreen.isVisible()) {
                        stopScreen.show(false);
                        min = 0;
                        sec = 0;
                    }
                }
                if (isPaused) return;
                if (min == 20) {
                    stopScreen.show(true);
                    stopTime = 20;
                }

                if (sec == 59) { sec = 0; min++; }
                else sec++;

                tempTime.setLength(0);
                if (min < 10) tempTime.append(0).append(min).append(':');
                else tempTime.append(min).append(":");
                if (sec < 10) tempTime.append(0).append(sec);
                else tempTime.append(sec);

                time.setText(tempTime.toString());
            }
        };
        Calendar calendar = Calendar.getInstance();
        timer.scheduleAtFixedRate(task, calendar.getTime(), 1000);

        resumeAfter.addActionListener(e -> {
            if (resumeAfter.isSelected()) {
                pauseBtn.setIcon(playIcon);
                stopTime = Integer.parseInt(resumeTime.getText()) * 60;
            } else {
                pauseBtn.setIcon(pauseIcon);
            }
        });

        ((PlainDocument) resumeTime.getDocument()).setDocumentFilter(new NumbersOnly());

        frame.setUndecorated(true);
        frame.setContentPane(mainPanel);
        frame.setLocation(x, 130);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setBackground(new Color(0, 0, 0, 0));
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setType(javax.swing.JFrame.Type.UTILITY);
        frame.setVisible(true);
    }

}
