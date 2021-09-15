package com.bnkc.sourcemodule.util;

public class UtilsActivity {

    private static boolean isCreated;

    public static boolean isCreated() {
        return isCreated;
    }

    public static void isCreated(boolean isCreated) {
        UtilsActivity.isCreated = isCreated;
    }
}
