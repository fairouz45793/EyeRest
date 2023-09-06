
package Start;

import File.SettingsFile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalComboBoxButton;
import java.awt.*;
import java.awt.event.*;



public class Settings {

    private JButton cancelButton;
    private JButton saveButton;
    private JRadioButton twentyRule;
    private JRadioButton darkTheme;
    private JRadioButton whiteTheme;
    private JComboBox<String> languageComboBox;
    private JPanel contentPanel;
    private JRadioButton oneHourRule;
    private JRadioButton thirtyRule;
    private JRadioButton twoHoursRule;
    private JComboBox<String> actionComboBox;
    private JLabel info20;
    private JLabel info30;
    private JLabel info1;
    private JLabel info2;
    private JLabel otherPractices;
    private JLabel moreInfoTxt;
    private JLabel actionTxt;
    private JLabel rulesTxt;
    private JLabel languageTxt;
    private JLabel themeTxt;
    private JLabel frameTitle;
    private JLabel closeButton;
    private JPanel titleBar;

    private static JFrame frame;

    private int lastThemeIndex = -1, oldThemeIndex = -1;
    private int lastLanguageIndex = -1, oldLanguageIndex = -1;
    private int lastRuleIndex = -1, oldRuleIndex = -1;
    private int lastActionIndex = -1, oldActionIndex = -1;
    // private ImageIcon inforImage;
    // private static boolean isThereChanged = false, isThemeChanged = false;
    private static boolean isThemeDone = false;

    private JWindow infoWindow;
    private JLabel infoTitle;
    private JTextArea infoText;

    // private static final LineBorder blackBorder = new LineBorder(Color.BLACK, 1, true);
    // private static final LineBorder whiteBorder = new LineBorder(Color.WHITE, 1, true);
    private static final ImageIcon whiteInfoIcon = new ImageIcon("src/icons/white info.png");
    private static final ImageIcon blackInfoIcon = new ImageIcon("src/icons/white info.png");
    private static final ImageIcon whiteArrow = new ImageIcon("src/icons/white arrow.png");
    private static final ImageIcon blackArrow = new ImageIcon("src/icons/black arrow.png");
    private static final ImageIcon whiteClose = new ImageIcon("src/icons/white close.png");
    private static final ImageIcon blackClose = new ImageIcon("src/icons/black close.png");


    Settings() {
        if (frame != null) return;
        frame = new JFrame();

        darkTheme.addActionListener(e -> {
            // isThemeChanged = true;
            // oldTheme = 1;
            lastThemeIndex = 0;
            whiteTheme.setSelected(false);
            darkTheme.setSelected(true);
        });

        whiteTheme.addActionListener(e -> {
            // isThemeChanged = true;
            // oldTheme = 0;
            lastThemeIndex = 1;
            darkTheme.setSelected(false);
            whiteTheme.setSelected(true);
        });

        languageComboBox.addActionListener(e -> {
            int tempIndex = languageComboBox.getSelectedIndex();
            // if (lastLanguageIndex != tempIndex) isThereChanged = true;
            lastLanguageIndex = tempIndex;
        });

        twentyRule.addActionListener(e -> {
            // isThereChanged = true;
            unselectLastOne();
            lastRuleIndex = 0;
            twentyRule.setSelected(true);
        });

        thirtyRule.addActionListener(e -> {
            // isThereChanged = true;
            unselectLastOne();
            lastRuleIndex = 1;
            thirtyRule.setSelected(true);
        });

        oneHourRule.addActionListener(e -> {
            // isThereChanged = true;
            unselectLastOne();
            lastRuleIndex = 2;
            oneHourRule.setSelected(true);
        });

        twoHoursRule.addActionListener(e -> {
            // isThereChanged = true;
            unselectLastOne();
            lastRuleIndex = 3;
            twoHoursRule.setSelected(true);
        });

        actionComboBox.addActionListener(e -> {
            int tempIndex = actionComboBox.getSelectedIndex();
            // if (lastActionIndex != tempIndex) isThereChanged = true;
            lastActionIndex = tempIndex;
        });

        saveButton.addActionListener(e -> {
            if (isThereChanges()) saveData();
        });

        cancelButton.addActionListener(e -> {
            /* isThemeChanged = false;
            isThereChanged = false; */
            show(false);
        });

        closeButton.setBackground(Color.RED);
        closeButton.setOpaque(false);
        Dialog dialog = new Dialog("Warning", "There is changes not saved\nDo you want to save changes?!",
                lastThemeIndex + "" + lastLanguageIndex + "" + lastRuleIndex + "" + lastActionIndex);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                System.out.println("isThereChanged :   " + isThereChanges());
                if (isThereChanges()) {
                    closeButton.setOpaque(false);
                    closeButton.repaint();
                    int yesOrNo = dialog.show(true, 150);
                    System.out.println("yesOrNo :   " + yesOrNo);
                    if (yesOrNo == 1) saveData();
                    else reload();
                    /* SettingsFile.saveSettings(themeIndex + "" + languageComboBox.getSelectedIndex() + "" +
                            ruleIndex + "" + actionComboBox.getSelectedIndex()); */
                }
                // isThereChanged = false;
                frame.setVisible(false);
            }

            @Override public void mouseEntered(MouseEvent e) { closeButton.setOpaque(true); closeButton.repaint(); }

            @Override public void mouseExited(MouseEvent e) {
                /* if (themeIndex == 0) closeButton.setBackground(Color.BLACK);
                else closeButton.setBackground(Color.LIGHT_GRAY); */
                closeButton.setOpaque(false); closeButton.repaint();
            }
        });

        info20.setBackground(new Color(0, 0, 0, 0));
        info30.setBackground(new Color(0, 0, 0, 0));
        info1.setBackground(new Color(0, 0, 0, 0));
        info2.setBackground(new Color(0, 0, 0, 0));
        otherPractices.setBackground(new Color(0, 0, 0, 0));
        setInfoBtns();
        reload();

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent) {
                if (event.getID() == MouseEvent.MOUSE_CLICKED) {
                    Object source = event.getSource();
                    if (source != info20 && source != info30 && source != info1 && source != info2) {
                        infoWindow.setVisible(false);
                    }
                }
            }
        }, AWTEvent.WINDOW_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);

        ((MetalComboBoxButton) languageComboBox.getComponent(0)).setBorder(new LineBorder(Color.darkGray, 1, true));
        ((MetalComboBoxButton) actionComboBox.getComponent(0)).setBorder(new LineBorder(Color.darkGray, 1, true));

        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { infoWindow.setVisible(false); }
        });
        frame.setSize(450, 420);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(contentPanel);
        frame.setUndecorated(true);
        frame.setType(javax.swing.JFrame.Type.UTILITY);
    }


    void reload() {
        unselectLastOne();

        // int oldThemeIndex = themeIndex;
        // try {
        oldThemeIndex = SettingsFile.getTheme();
        lastThemeIndex = oldThemeIndex;
        if (lastThemeIndex == 0) {
            darkTheme.setSelected(true);
            whiteTheme.setSelected(false);
        } else {
            darkTheme.setSelected(false);
            whiteTheme.setSelected(true);
        }
        oldLanguageIndex = SettingsFile.getLanguage();
        lastLanguageIndex = oldLanguageIndex;
        languageComboBox.setSelectedIndex(oldLanguageIndex);
        oldRuleIndex = SettingsFile.getRule();
        lastRuleIndex = SettingsFile.getRule();
        oldActionIndex = SettingsFile.getAction();
        lastActionIndex = SettingsFile.getAction();
            actionComboBox.setSelectedIndex(oldActionIndex);
        /* } catch (IOException ex) {
            ex.printStackTrace();
            /* themeIndex = 0;
            languageComboBox.setSelectedIndex(0);
            ruleIndex = 0;
            actionComboBox.setSelectedIndex(0); */
        // }
        // System.out.println("data from file :   " + themeIndex + ",  " + languageIndex + ",  " + ruleIndex + ",  " + actionIndex);

        switch (oldRuleIndex) {
            case 0 -> twentyRule.setSelected(true);
            case 1 -> thirtyRule.setSelected(true);
            case 2 -> oneHourRule.setSelected(true);
            case 3 -> twoHoursRule.setSelected(true);
        }
        // isThereChanged = false;

        if (isThemeDone) return;
        isThemeDone = true;
        // if (oldThemeIndex != themeIndex) {
        /* private static final Color grayColor = Color.DARK_GRAY;
    private static final Color blackColor = new Color(51, 51, 51);
    private static final Color whiteColor = new Color(230, 230, 230); */
        Color backgroundColor;
        Color foregroundColor;
        if (oldThemeIndex == 0) {
                backgroundColor = Color.darkGray;
                foregroundColor = Color.WHITE;
                ((MetalComboBoxButton) languageComboBox.getComponent(0)).setComboIcon(whiteArrow);
                ((MetalComboBoxButton) actionComboBox.getComponent(0)).setComboIcon(whiteArrow);
                info20.setIcon(whiteInfoIcon);
                info30.setIcon(whiteInfoIcon);
                info1.setIcon(whiteInfoIcon);
                info2.setIcon(whiteInfoIcon);
                otherPractices.setIcon(whiteInfoIcon);
                titleBar.setBackground(Color.BLACK);
                // closeButton.setBackground(Color.BLACK);
                closeButton.setIcon(whiteClose);
            } else {
                backgroundColor = Color.WHITE;
                foregroundColor = Color.DARK_GRAY;
                ((MetalComboBoxButton) languageComboBox.getComponent(0)).setComboIcon(blackArrow);
                ((MetalComboBoxButton) actionComboBox.getComponent(0)).setComboIcon(blackArrow);
                info20.setIcon(blackInfoIcon);
                info30.setIcon(blackInfoIcon);
                info1.setIcon(blackInfoIcon);
                info2.setIcon(blackInfoIcon);
                otherPractices.setIcon(blackInfoIcon);
                titleBar.setBackground(Color.LIGHT_GRAY);
                // closeButton.setBackground(Color.LIGHT_GRAY);
                closeButton.setIcon(blackClose);
            }
            contentPanel.setBackground(backgroundColor);
            themeTxt.setForeground(foregroundColor);
            darkTheme.setForeground(foregroundColor);
            whiteTheme.setForeground(foregroundColor);
            languageTxt.setForeground(foregroundColor);
            languageComboBox.setForeground(foregroundColor);
            languageComboBox.setBackground(backgroundColor);
            rulesTxt.setForeground(foregroundColor);
            twentyRule.setForeground(foregroundColor);
            thirtyRule.setForeground(foregroundColor);
            oneHourRule.setForeground(foregroundColor);
            twoHoursRule.setForeground(foregroundColor);
            infoWindow.setBackground(backgroundColor);
            infoTitle.setForeground(foregroundColor);
            infoText.setForeground(foregroundColor);
            moreInfoTxt.setForeground(foregroundColor);
            actionTxt.setForeground(foregroundColor);
            actionComboBox.setForeground(foregroundColor);
            actionComboBox.setBackground(backgroundColor);
            saveButton.setForeground(foregroundColor);
            saveButton.setBackground(backgroundColor);
            cancelButton.setForeground(foregroundColor);
            cancelButton.setBackground(backgroundColor);
            frameTitle.setForeground(foregroundColor);
        // }
    }


    private void unselectLastOne() {
        switch (lastRuleIndex) {
            case 0 -> twentyRule.setSelected(false);
            case 1 -> thirtyRule.setSelected(false);
            case 2 -> oneHourRule.setSelected(false);
            case 3 -> twoHoursRule.setSelected(false);
        }
    }


    private void setInfoBtns() {
        infoWindow = new JWindow();
        JPanel infoPanel = new JPanel();
        Dimension titleDimension = new Dimension(160, 20);
        Dimension textDimension = new Dimension(160, 50);

        infoTitle = new JLabel();
        infoTitle.setFont(new Font(Font.SERIF, Font.BOLD, 12));
        infoPanel.add(infoTitle, BorderLayout.NORTH);
        infoTitle.setMinimumSize(titleDimension);
        infoTitle.setPreferredSize(titleDimension);

        infoText = new JTextArea();
        infoText.setBackground(null);
        infoText.setFont(new Font(Font.SERIF, Font.PLAIN, 11));
        infoText.setWrapStyleWord(true);
        infoText.setLineWrap(true);
        infoPanel.add(infoText, BorderLayout.SOUTH);
        infoText.setMinimumSize(textDimension);
        infoText.setPreferredSize(textDimension);

        infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        infoWindow.setContentPane(infoPanel);
        infoWindow.setSize(180, 86);

        /* inforImage = new ImageIcon("src/icons/info.png");
        info20.setIcon(inforImage); */
        info20.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                infoTitle.setText("20 - 20 - 20 Rule :");
                infoText.setText("  Gaze at a distant object (at least 20 feet away = 6 meter) for at least 20 seconds.");
                Point p = info20.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });

        // info30.setIcon(inforImage);
        info30.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                infoTitle.setText("30 min - 5 min Rule :");
                infoText.setText("  Gaze at a distant object (look from window for example) for at least 5 minutes.");
                Point p = info30.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });

        // info1.setIcon(inforImage);
        info1.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                infoTitle.setText("1 hour - 10 min Rule :");
                infoText.setText("  Gaze at a distant object (look from window for example) for at least 10 minutes.");
                Point p = info1.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });

        // info2.setIcon(inforImage);
        info2.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                infoTitle.setText("2 hours - 20 min Rule :");
                infoText.setText("  Gaze at a distant object (look from window for example) for at least 20 minutes.");
                Point p = info2.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });

        // otherPractices.setIcon(inforImage);
        otherPractices.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                infoTitle.setText("recommended Practices :");
                infoText.setText("  Gaze at a distant object (at least 20 feet away = 6 meter) for at least 20 seconds." +
                        "\n  look far away at an object for 10-15 seconds, then gaze at something up close for 10-15 seconds." +
                        "\n  Then look back at the distant object. Do this 10 times.");
                Point p = otherPractices.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });
    }


    void show(boolean show) {
        frame.setVisible(show);
    }


    static Point getLocation() {
        if (frame != null && frame.isShowing()) return frame.getLocationOnScreen();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point((int) (d.getWidth() / 2) + 225, (int) (d.getHeight() / 2) + 90);
    }


    private void saveData() {
        // try {
        SettingsFile.saveSettings(oldThemeIndex != lastThemeIndex, lastThemeIndex + "" + languageComboBox.getSelectedIndex() + "" +
                lastRuleIndex + "" + actionComboBox.getSelectedIndex());
            /* } catch (IOException ex) {
                throw new RuntimeException(ex);
            } */
        Start.changeTimes();
        StopScreen.changeTimes();
        show(false);
        /* isThereChanged = false;
        isThemeChanged = false; */
    }


    private boolean isThereChanges() {
        return ((oldThemeIndex != lastThemeIndex) || (oldLanguageIndex != lastLanguageIndex) ||
                (oldRuleIndex != lastRuleIndex) || (oldActionIndex != lastActionIndex));
    }

}
