/*
 *  SPDX-License-Identifier: LGPL-2.1-or-later
 */
package Start;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;



public class STimer {

    private static int sec = 0;
    private static int min = 0;


    public STimer() {
    }


    String getTime() {
        return min + ":" + sec;
    }

}
