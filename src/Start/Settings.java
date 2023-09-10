
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
    private static boolean isThemeDone = false;

    private JWindow infoWindow;
    private JLabel infoTitle;
    private JTextArea infoText;

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
            lastThemeIndex = 0;
            whiteTheme.setSelected(false);
            darkTheme.setSelected(true);
        });

        whiteTheme.addActionListener(e -> {
            lastThemeIndex = 1;
            darkTheme.setSelected(false);
            whiteTheme.setSelected(true);
        });

        languageComboBox.addActionListener(e -> {
            lastLanguageIndex = languageComboBox.getSelectedIndex();
        });

        twentyRule.addActionListener(e -> {
            unselectLastOne();
            lastRuleIndex = 0;
            twentyRule.setSelected(true);
        });

        thirtyRule.addActionListener(e -> {
            unselectLastOne();
            lastRuleIndex = 1;
            thirtyRule.setSelected(true);
        });

        oneHourRule.addActionListener(e -> {
            unselectLastOne();
            lastRuleIndex = 2;
            oneHourRule.setSelected(true);
        });

        twoHoursRule.addActionListener(e -> {
            unselectLastOne();
            lastRuleIndex = 3;
            twoHoursRule.setSelected(true);
        });

        actionComboBox.addActionListener(e -> lastActionIndex = actionComboBox.getSelectedIndex() );
        saveButton.    addActionListener(e -> { if (isThereChanges()) saveData(); });
        cancelButton.  addActionListener(e -> show(false) );

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
                }
                frame.setVisible(false);
            }

            @Override public void mouseEntered(MouseEvent e) { closeButton.setOpaque(true); closeButton.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { closeButton.setOpaque(false); closeButton.repaint(); }
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

        switch (oldRuleIndex) {
            case 0 -> twentyRule.setSelected(true);
            case 1 -> thirtyRule.setSelected(true);
            case 2 -> oneHourRule.setSelected(true);
            case 3 -> twoHoursRule.setSelected(true);
        }

        if (isThemeDone) return;
        isThemeDone = true;
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
        Dimension textDimension1 = new Dimension(160, 50);
        Dimension textDimension2 = new Dimension(240, 140);
        final boolean[] isDim1 = {true};

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

        infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        infoWindow.setContentPane(infoPanel);

        info20.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (!isDim1[0]) {
                    infoText.setMinimumSize(textDimension1);
                    infoText.setPreferredSize(textDimension1);
                    infoWindow.setSize(180, 86);
                    isDim1[0] = true;
                }
                infoTitle.setText("20 - 20 - 20 Rule :");
                infoText.setText("  Gaze at a distant object (at least 20 feet away = 6 meter) for at least 20 seconds.");
                Point p = info20.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });

        info30.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (!isDim1[0]) {
                    infoText.setMinimumSize(textDimension1);
                    infoText.setPreferredSize(textDimension1);
                    infoWindow.setSize(180, 86);
                    isDim1[0] = true;
                }
                infoTitle.setText("30 min - 5 min Rule :");
                infoText.setText("  Gaze at a distant object (look from window for example) for at least 5 minutes.");
                Point p = info30.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });

        info1.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (!isDim1[0]) {
                    infoText.setMinimumSize(textDimension1);
                    infoText.setPreferredSize(textDimension1);
                    infoWindow.setSize(180, 86);
                    isDim1[0] = true;
                }
                infoTitle.setText("1 hour - 10 min Rule :");
                infoText.setText("  Gaze at a distant object (look from window for example) for at least 10 minutes.");
                Point p = info1.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });

        info2.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (!isDim1[0]) {
                    infoText.setMinimumSize(textDimension1);
                    infoText.setPreferredSize(textDimension1);
                    infoWindow.setSize(180, 86);
                    isDim1[0] = true;
                }
                infoTitle.setText("2 hours - 20 min Rule :");
                infoText.setText("  Gaze at a distant object (look from window for example) for at least 20 minutes.");
                Point p = info2.getLocationOnScreen();
                infoWindow.setLocation(p.x + 20, p.y);
                infoWindow.setVisible(true);
            }
        });

        otherPractices.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (isDim1[0]) {
                    infoText.setMinimumSize(textDimension2);
                    infoText.setPreferredSize(textDimension2);
                    infoWindow.setSize(260, 176);
                    isDim1[0] = false;
                }
                infoTitle.setText("Recommended Practices :");
                infoText.setText("  Make your food healthy, do some physical exercises to keep you healthy, go out and " +
                        "walk without watching on your smartphone and stare far away for 30 min per day, and do not " +
                        "forget to wear sunglasses. \n\n  Do not take these information are 100% correct, please contact " +
                        "your Ophthalmologist to get more information and advice according to your case.");
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
        SettingsFile.saveSettings(oldThemeIndex != lastThemeIndex, lastThemeIndex + "" + languageComboBox.getSelectedIndex() + "" +
                lastRuleIndex + "" + actionComboBox.getSelectedIndex());
        Start.changeTimes();
        StopScreen.changeTimes();
        show(false);
    }


    private boolean isThereChanges() {
        return ((oldThemeIndex != lastThemeIndex) || (oldLanguageIndex != lastLanguageIndex) ||
                (oldRuleIndex != lastRuleIndex) || (oldActionIndex != lastActionIndex));
    }

}
