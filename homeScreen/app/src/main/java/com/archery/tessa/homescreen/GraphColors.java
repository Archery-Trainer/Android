package com.archery.tessa.homescreen;

import android.graphics.Color;

/**
 * The colors for the graph plots are defined here. It would be cleaner if they were defined
 * in colors.xml, but those hexadecimal values didn't work in the graph view for some reason.
 *
 * Also, I didn't find a way to change the checkbox tint colors programmatically, so their colors
 * are defined in colors.xml. Change those also when changing these values.
 */

public class GraphColors {

    public final static int[] colors = {
            Color.RED,
            Color.MAGENTA,
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.YELLOW
    };

    /**
     * Get a sensor color
     * @param ix    Index of the sensor whose color to get
     * @return      Int value of the color
     */
    public static int getSensorColor(int ix) {
        if(ix < 0 || ix > 5)
            return 0;
        else
            return colors[ix];
    }

}
