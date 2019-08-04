package juniter.core.event;

import juniter.core.event.CoreEvent;

import java.text.NumberFormat;


public class LogMemory extends CoreEvent<String> {

    public LogMemory() {
        super(memInfo(), "Current Block : ");
        name = getClass().getSimpleName();
    }

    private static String memInfo() {
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
