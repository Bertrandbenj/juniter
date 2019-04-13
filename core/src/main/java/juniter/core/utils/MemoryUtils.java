package juniter.core.utils;

import java.text.NumberFormat;

public class MemoryUtils {
    public static String memInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = Runtime.getRuntime().maxMemory();
        long allocatedMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        sb.append("Free: ");
        sb.append(format.format(freeMemory / 1024));
        sb.append(" - ");
        sb.append("Allocated: ");
        sb.append(format.format(allocatedMemory / 1024));
        sb.append(" - ");
        sb.append("Max: ");
        sb.append(format.format(maxMemory / 1024));
        sb.append(" - ");
        sb.append("Total free memory: ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
       // sb.append(" - ");

        Runtime.getRuntime().gc();
        return sb.toString();
    }
}
