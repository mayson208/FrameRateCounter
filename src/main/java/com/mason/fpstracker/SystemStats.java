package com.mason.fpstracker;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class SystemStats {

    private static final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

  
    public static double getCpuUsage() {
        double load = osBean.getSystemCpuLoad();
        if (load < 0) {
            return 0;
        }
        return load * 100;
    }


    public static double getUsedMemoryMB() {
        long usedBytes =
                Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return usedBytes / (1024.0 * 1024.0);
    }
}
