/*
 *  SPDX-License-Identifier: LGPL-2.1-or-later
 */
package Start;

import File.SettingsFile;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JDialog;
import java.awt.Color;
import java.awt.Point;



public class Dialog {

    private JButton okButton;
    private JButton cancelButton;
    private JPanel titleBar;
    private JLabel titleLabel;
    private JTextArea messageArea;
    private JPanel parentPanel;
    private JPanel subPanel;
    private JPanel t2;
    private JPanel t3;

    protected JDialog myOptionPane;
    private boolean isThemeDone;
    private String data;
    private int yesOrNo;


    private Dialog(int demiWidth) {
        myOptionPane = new JDialog();
        isThemeDone = false;
        yesOrNo = -1;
        okButton.addActionListener(e -> {
            yesOrNo = 1;
            myOptionPane.dispose();
        });

        cancelButton.addActionListener(e -> {
            show(false, demiWidth);
            yesOrNo = 0;
            myOptionPane.dispose();
        });

        t2.setBackground(new Color(33, 32, 32, 130));
        t3.setBackground(new Color(32, 32, 32, 130));

        myOptionPane.setContentPane(parentPanel);
        myOptionPane.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        myOptionPane.setUndecorated(true);
        myOptionPane.setBackground(new Color(0, 126, 168, 0));
        myOptionPane.setModal(true);
    }

    public Dialog(String title, String message, String dataToSave) {
        this(150);
        titleLabel.setText(title);
        messageArea.setText(message);
        data = dataToSave;
        myOptionPane.setSize(320, 180);
        setTheme();
    }

    public Dialog(String title, String message, String leftButton, String rightButton) {
        this(190);
        titleLabel.setText(title);
        messageArea.setText(message);
        okButton.setText(leftButton);
        cancelButton.setText(rightButton);
        myOptionPane.setSize(400, 180);
        setTheme();
    }


    private void setTheme() {
        if (isThemeDone) return;

        isThemeDone = true;
        Color backgroundColor;
        Color foregroundColor;
        if (SettingsFile.getTheme() == 0) {
            backgroundColor = Color.DARK_GRAY;
            foregroundColor = Color.WHITE;
            titleBar.setBackground(Color.BLACK);
        } else {
            backgroundColor = Color.WHITE;
            foregroundColor = Color.DARK_GRAY;
            titleBar.setBackground(new Color(255, 255, 255));
        }

        titleLabel.setForeground(foregroundColor);
        messageArea.setForeground(foregroundColor);
        okButton.setForeground(foregroundColor);
        cancelButton.setForeground(foregroundColor);
        subPanel.setBackground(backgroundColor);
        okButton.setBackground(backgroundColor);
        cancelButton.setBackground(backgroundColor);
    }


    public int show(boolean visible, int demiWidth) {
        Point point = Settings.getLocation();
        point.x = point.x + 225 - demiWidth;
        point.y = point.y + 120;
        myOptionPane.setLocation(point);
        myOptionPane.repaint();
        myOptionPane.setVisible(visible);
        return yesOrNo;
    }

}
