package com.mason.fpstracker;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class SystemStats {

    private static final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static double getCpuUsage() {
        double load = osBean.getCpuLoad();
        return load < 0 ? 0 : load * 100;
    }

    public static double getUsedMemoryMB() {
        long total = osBean.getTotalMemorySize();
        long free  = osBean.getFreeMemorySize();
        return (total - free) / (1024.0 * 1024.0);
    }

    public static double getTotalMemoryGB() {
        return osBean.getTotalMemorySize() / (1024.0 * 1024.0 * 1024.0);
    }
}
