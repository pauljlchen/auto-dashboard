package org.xbot.core.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by paulc on 2/17/2017.
 */
public class GenericController {
    final public String Project_View = "project";
    final public String Test_View = "test";
    final public String Record_View ="record";
    final public String Dashboard_View ="dashboard";
    final public String Team_Confidence_View ="team_confidence";

    final public String Dashboard_Controller ="dashboards";

    protected Logger log = LogManager.getLogger("dashboard");

    String MESSAGE="MSG";
    String RESULT="RESULT";

    protected String getIpAddr(HttpServletRequest request) {
        if (request == null){
            return "IP not available";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }

        return ip;
    }
}
