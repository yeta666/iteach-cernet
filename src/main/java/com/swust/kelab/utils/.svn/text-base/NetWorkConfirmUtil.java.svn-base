package com.swust.kelab.utils;

import java.util.HashMap;

import java.util.Map;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

/**
 * 添加服务器时网段测算
 * 
 * @author lancer
 * 
 */
public class NetWorkConfirmUtil {

    /**
     * 传入子网掩码，如“255.255.255.0”，计算掩码位数，如24
     * 
     * @author lancer
     * @param mask 子网掩码
     * @return count 掩码位数
     */
    public static int asNetMask(String mask) {
        String[] str = mask.split("\\.");
        int count = 0;
        for (int i = str.length - 1; i >= 0; i--) {

            int res = Integer.parseInt(str[i]);

            for (int j = 1; j <= 128; j = j * 2) {
                if ((res & j) != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 计算最大可用IP，最小可用IP
     * 
     * @author lancer
     * @param subnet 网络地址：如“10.10.4.1/24”
     * @return map
     */
    public static Map<String, String> asNetWork(String subnet) {
        Map<String, String> map = new HashMap<String, String>();
        SubnetUtils utils = new SubnetUtils(subnet);
        SubnetInfo info = utils.getInfo();
        map.put("lowAddress", info.getLowAddress());
        map.put("highAddress", info.getHighAddress());
        return map;
    }
    // public static void main(String[] args) {
    // String subnet = "192.168.0.1/10";
    // SubnetUtils utils = new SubnetUtils(subnet);
    // SubnetInfo info = utils.getInfo();
    //
    // System.out.printf("Subnet Information for %s:\n", subnet);
    // System.out.println("--------------------------------------");
    // System.out.printf("IP Address:\t\t\t%s\t[%s]\n", info.getAddress(),
    // Integer.toBinaryString(info.asInteger(info.getAddress())));
    // System.out.printf("Netmask:\t\t\t%s\t[%s]\n", info.getNetmask(),
    // Integer.toBinaryString(info.asInteger(info.getNetmask())));
    // System.out.printf("CIDR Representation:\t\t%s\n\n", info.getCidrSignature());
    //
    // System.out.printf("Supplied IP Address:\t\t%s\n\n", info.getAddress());
    //
    // System.out.printf("Network Address:\t\t%s\t[%s]\n", info.getNetworkAddress(),
    // Integer.toBinaryString(info.asInteger(info.getNetworkAddress())));
    // System.out.printf("Broadcast Address:\t\t%s\t[%s]\n", info.getBroadcastAddress(),
    // Integer.toBinaryString(info.asInteger(info.getBroadcastAddress())));
    // System.out.printf("First Usable Address:\t\t%s\t[%s]\n", info.getLowAddress(),
    // Integer.toBinaryString(info.asInteger(info.getLowAddress())));
    // System.out.printf("Last Usable Address:\t\t%s\t[%s]\n", info.getHighAddress(),
    // Integer.toBinaryString(info.asInteger(info.getHighAddress())));
    //
    // System.out.printf("Total usable addresses: \t%d\n", info.getAddressCount());
    // System.out.printf("Address List: %s\n\n", Arrays.toString(info.getAllAddresses()));
    //
    // final String prompt = "Enter an IP address (e.g. 192.168.0.10):";
    // System.out.println(prompt);
    // Scanner scanner = new Scanner(System.in);
    // while (scanner.hasNextLine()) {
    // String address = scanner.nextLine();
    // System.out.println("The IP address [" + address + "] is " + (info.isInRange(address) ? "" : "not ")
    // + "within the subnet [" + subnet + "]");
    // System.out.println(prompt);
    // }
    //
    // }

}
