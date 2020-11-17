package io.edurt.grp.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetWorksUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NetWorksUtils.class);
    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final String DEFAULT_ADDRESS = "127.0.0.1";

    private NetWorksUtils()
    {}

    /**
     * 获取系统主机名
     * <p>1. 当获取主机名称出现异常时，默认使用localhost</p>
     * <p>2. 当主机名称=bogon时，默认使用localhost</p>
     *
     * @param useDefault 是否使用默认主机名称
     * @return 系统主机名
     */
    public static String getHostName(Boolean useDefault)
    {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String host = inetAddress.getHostName();
            if (host.equalsIgnoreCase("bogon")) {
                if (useDefault) {
                    return DEFAULT_HOSTNAME;
                }
            }
            return host;
        }
        catch (UnknownHostException e) {
            LOGGER.debug("获取本地主机名出现异常，将使用默认主机名，异常信息 {}", e);
            if (useDefault) {
                return DEFAULT_HOSTNAME;
            }
        }
        return null;
    }

    /**
     * 获取系统主机名
     * <p>1. 当获取主机名称出现异常时，默认使用localhost</p>
     * <p>2. 当主机名称=bogon时，默认使用localhost</p>
     *
     * @return 系统主机名
     */
    public static String getHostName()
    {
        return getHostName(Boolean.TRUE);
    }

    /**
     * 获取主机IP地址
     * <p>1. 当获取主机IP地址出现异常时，默认使用127.0.0.1</p>
     * <p>2. 当主机地址无法获取时，默认使用127.0.0.1</p>
     *
     * @param useDefault 是否使用默认IP地址
     * @return 主机IP地址
     */
    public static String getAddress(Boolean useDefault)
    {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            if (ObjectUtils.isNotEmpty(inetAddress.getHostAddress())) {
                return inetAddress.getHostAddress();
            }
            if (useDefault) {
                return DEFAULT_ADDRESS;
            }
        }
        catch (UnknownHostException e) {
            LOGGER.debug("获取本地主机IP地址出现异常，将使用默认主机IP，异常信息 {}", e);
            if (useDefault) {
                return DEFAULT_ADDRESS;
            }
        }
        return null;
    }

    /**
     * 获取主机IP地址
     * <p>1. 当获取主机IP地址出现异常时，默认使用127.0.0.1</p>
     * <p>2. 当主机地址无法获取时，默认使用127.0.0.1</p>
     *
     * @return 主机IP地址
     */
    public static String getAddress()
    {
        return getAddress(Boolean.TRUE);
    }
}
