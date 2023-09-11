/*
 *  SPDX-License-Identifier: LGPL-2.1-or-later
 */
package Start;

import File.SettingsFile;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
    private JPanel subPanel;
    private JLabel timeTxt;
    private JLabel resumeTxt;
    private JLabel resumeMinTxt;
    private JLabel timeMinTxt;

    private JLabel closeBtn;
    private JLabel restartBtn;
    private JLabel pauseBtn;
    private JLabel settingsBtn;

    private static JFrame frame;
    private static boolean isPaused = false;
    private static boolean isPop1 = true;

    private static final int x = Toolkit.getDefaultToolkit().getScreenSize().width - 15;
    private static int stopTime = 0;
    private static int min = 0, sec = 0;
    private static int timeToTakeRest, restTime;

    private static final ImageIcon popIcon1 = new ImageIcon("src/icons/pop_demi.png");
    private static final ImageIcon popIcon2 = new ImageIcon("src/icons/pop_comp.png");
    private static final ImageIcon pauseIcon = new ImageIcon("src/icons/pause.png");
    private static final ImageIcon playIcon = new ImageIcon("src/icons/play.png");

    private static final Color blackColor = new Color(51, 51, 51);
    private static final Color whiteColor = new Color(230, 230, 230);
    private static final LineBorder blackBorder = new LineBorder(Color.BLACK, 1, true);
    private static final LineBorder whiteBorder = new LineBorder(Color.WHITE, 1, true);


    public Start() {
        if (frame != null) return;
        frame = new JFrame();

        changeTimes();
        resumeTime.setCaretColor(new Color(0, 0, 0, 0));

        JWindow menu = new JWindow();
        pop.setIcon(popIcon1);
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
                        int xIndx = x - 276;
                        for (int i = x; i > xIndx; i -= 2) {
                            frame.setLocation(i, frame.getLocationOnScreen().y);
                            Thread.sleep(1);
                        }
                    } else {
                        pop.setIcon(popIcon1);
                        for (int i = x - 276; i < x + 1; i += 2) {
                            frame.setLocation(i, frame.getLocationOnScreen().y);
                            Thread.sleep(1);
                        }
                    }
                    menu.setVisible(false);
                    isPop1 = !isPop1;
                } catch (InterruptedException ignored) {

                }
            }
        });

        closeBtn = new JLabel();
        restartBtn = new JLabel();
        pauseBtn = new JLabel();
        settingsBtn = new JLabel();
        closeBtn.setOpaque(false);
        restartBtn.setOpaque(false);
        pauseBtn.setOpaque(false);
        settingsBtn.setOpaque(false);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 2));
        contentPane.add(closeBtn, BorderLayout.NORTH);
        contentPane.add(pauseBtn, BorderLayout.CENTER);
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout(0, 2));
        tempPanel.setOpaque(false);
        tempPanel.add(restartBtn, BorderLayout.NORTH);
        tempPanel.add(settingsBtn, BorderLayout.SOUTH);
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

        Settings settings = new Settings();
        settingsBtn.setIcon(new ImageIcon("src/icons/settings.png"));
        settingsBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                settings.reload();
                settings.show(true);
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent) {
                    if (event.getID() == MouseEvent.MOUSE_ENTERED) {
                        Object source = event.getSource();
                        if (source == pauseBtn || source == restartBtn || source == closeBtn || source == settingsBtn) return;
                        if (source == pop) {
                            if (isPop1) menu.setLocation(pop.getLocationOnScreen().x - 7, pop.getLocationOnScreen().y + 30);
                            else menu.setLocation(pop.getLocationOnScreen().x, pop.getLocationOnScreen().y + 32);
                            menu.setVisible(true);
                        } else menu.setVisible(false);
                    }
            }
        }, AWTEvent.WINDOW_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);


        java.util.Timer timer = new Timer();
        StopScreen stopScreen = new StopScreen();
        TimerTask task = new TimerTask() {
            final StringBuilder tempTime = new StringBuilder();

            @Override public void run() {
                if (stopTime > -1) {
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
                        sec = -1;
                    }
                }

                if (isPaused) return;
                if (min == timeToTakeRest) {
                    stopScreen.show(true);
                    stopTime = restTime;
                    return;
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
            if (resumeTime.getText().isEmpty())
                return;

            if (resumeAfter.isSelected()) {
                pauseBtn.setIcon(playIcon);
                stopTime = Integer.parseInt(resumeTime.getText()) * 60;
            } else {
                pauseBtn.setIcon(pauseIcon);
            }
        });

        ((PlainDocument) resumeTime.getDocument()).setDocumentFilter(new NumbersOnly());
        setTheme();

        frame.setUndecorated(true);
        frame.setContentPane(mainPanel);
        frame.setLocation(x, 130);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(320, 150);
        frame.setBackground(new Color(0, 0, 0, 0));
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setType(javax.swing.JFrame.Type.UTILITY);
        frame.setVisible(true);
    }


    public static void changeTimes() {
        restTime = 20;
        timeToTakeRest = 20;
        switch (SettingsFile.getRule()) {
            case 0 -> { restTime = 20;   timeToTakeRest = 20; }
            case 1 -> { restTime = 300;  timeToTakeRest = 30; }
            case 2 -> { restTime = 600;  timeToTakeRest = 60; }
            case 3 -> { restTime = 1200; timeToTakeRest = 120; }
        }
    }


    public void setTheme() {
        if (SettingsFile.getTheme() == 0) {
            subPanel.setBackground(blackColor);
            timeTxt.setForeground(whiteColor);
            timeMinTxt.setForeground(whiteColor);
            time.setForeground(whiteColor);
            resumeTxt.setForeground(whiteColor);
            resumeMinTxt.setForeground(whiteColor);
            resumeTime.setForeground(whiteColor);
            resumeTime.setBackground(blackColor);
            resumeTime.setBorder(whiteBorder);
            resumeAfter.setBackground(blackColor);
        } else {
            subPanel.setBackground(whiteColor);
            timeTxt.setForeground(blackColor);
            timeMinTxt.setForeground(blackColor);
            time.setForeground(blackColor);
            resumeTxt.setForeground(blackColor);
            resumeMinTxt.setForeground(blackColor);
            resumeTime.setForeground(blackColor);
            resumeTime.setBackground(whiteColor);
            resumeTime.setBorder(blackBorder);
            resumeAfter.setBackground(whiteColor);
        }
    }

}
