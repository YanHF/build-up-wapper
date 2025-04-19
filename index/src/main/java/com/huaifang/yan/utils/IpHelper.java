package com.huaifang.yan.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IpHelper {
    public static String getLocalIp() {
        try {
            List<String> list = new ArrayList<>();
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null)
                return "Unknown";
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if ("docker0".equals(ni.getDisplayName()) || "docker0".equals(ni.getName()))
                    continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (inetAddress instanceof Inet4Address) {
                        if (!inetAddress.isSiteLocalAddress())
                            continue;
                        list.add(inetAddress.getHostAddress());
                    }
                }
            }

            long offlineCount = list.stream().filter(p -> p.startsWith("10.10")).count();
            if (offlineCount >= 1) {
                Set<String> collect = list.stream().filter(p -> p.startsWith("10.10")).collect(Collectors.toSet());
                return StringUtils.join(collect.iterator(), ",");
            }
            long onlineCount = list.stream().filter(p -> p.startsWith("172")).count();
            if (onlineCount >= 1) {
                Set<String> collect = list.stream().filter(p -> p.startsWith("172")).collect(Collectors.toSet());
                return StringUtils.join(collect.iterator(), ",");
            }
            if (list.size() != 0)
                return StringUtils.join(list.iterator(), ",");
            return "Unknown";

        } catch (Exception ex) {
            return "Unknown";
        }
    }
}
