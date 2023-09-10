/*
 *  Copyright (C) 2016  EyesRest maintainer
 *  SPDX-License-Identifier: GPL-3.0
 */
package File;

import Start.Dialog;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;



public class SettingsFile {

    private static File file;
    private static int[] values;
    static Dialog dialog = new Dialog("Restart", "To change theme now you need to restart EyeRest\n" +
            "Do you want to restart now?!!", "Yes", "No");

    private static boolean isFileExist() {
        try {
            file = new File("src/File/settings.txt");
            if (file.exists()) {
                return true;
            }

            if (!file.createNewFile()) return false;
            saveSettings(false, "0000");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    private static void getFileStrings() {
        try {
            if (!isFileExist()) return;
            FileReader reader = new FileReader(file);
            if (values == null) values = new int[4];
            int val = reader.read() - 48;
            int i = 0;
            while (val >= 0) {
                values[i++] = val;
                val = reader.read() - 48;
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static int getTheme() {
        if (values == null) getFileStrings();
        return values[0];
    }

    public static int getLanguage() {
        if (values == null) getFileStrings();
        return values[1];
    }

    public static int getRule() {
        if (values == null) getFileStrings();
        return values[2];
    }

    public static int getAction() {
        if (values == null) getFileStrings();
        return values[3];
    }


    public static void saveSettings(boolean isThemeChanged, String data) {
        for (int i = 0; i < 4; i++) {
            values[i] = data.charAt(i) - 48;
        }
        System.out.println("value :  " + values[0]);

        try {
            FileWriter writer = new FileWriter(file);
            writer.append(data, 0, 4);
            writer.close();
            System.out.println("data :  " + data);

            if (isThemeChanged) {
                int yesOrNo = dialog.show(true, 190);
                if (yesOrNo == 1) {
                    final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
                    final File currentJar = new File(Main.Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());

                    if (!currentJar.getName().endsWith(".jar"))
                        return;

                    final ArrayList<String> command = new ArrayList<>();
                    command.add(javaBin);
                    command.add("-jar");
                    command.add(currentJar.getPath());

                    final ProcessBuilder builder = new ProcessBuilder(command);
                    builder.start();
                    System.exit(0);
                }
            }
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

}
