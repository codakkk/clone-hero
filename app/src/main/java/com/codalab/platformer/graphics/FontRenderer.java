package com.codalab.platformer.graphics;

public class FontRenderer {
    private static String chars = "" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " +
            "0123456789.,!?'\"-+=/\\%()<>:;     " +
            "";

    public static void draw(MyGraphics g, String msg, int x, int y) {
        msg = msg.toUpperCase();

        for (int i = 0; i < msg.length(); i++) {
            int ix = chars.indexOf(msg.charAt(i));
            if (ix < 0) {
                continue;
            }

            g.draw(4 + x + i * 8, 4+y, (ix + 6 * 32));
        }
    }

    // This method with color, creates a little more garbage than the other one
    // this is caused by the Color Filter applied in the AndroidGraphics
    // that's why we're cloning the method and only changing [color]
    // This is mainly used only by TextEntity
    public static void draw(MyGraphics g, String msg, int x, int y, int color) {
        msg = msg.toUpperCase();

        for (int i = 0; i < msg.length(); i++) {
            int ix = chars.indexOf(msg.charAt(i));
            if (ix < 0) {
                continue;
            }

            g.draw(4 + x + i * 8, 4+y, (ix + 6 * 32), color);
        }
    }
}
