package micdoodle8.mods.miccore;

public class IntCache {
    /**
     * This method overrides the broken Galacticraft version.
     * It simply allocates a new array, bypassing the caching logic causing the crash.
     */
    public static int[] getIntCache(int size) {
        return new int[size];
    }

    /**
     * Required to prevent "Method not found" errors during world gen.
     */
    public static void resetIntCache() {
        // Do nothing.
    }
}