package com.daemon.ua;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class UATest extends TestCase {
    public UATest(String ua){
        super(ua);
    }

    static public Test suite(){
        return new TestSuite(UATest.class);
    }

    public void test() throws Exception{
        long stime = System.currentTimeMillis();
        //single();
        batch();
        long etime = System.currentTimeMillis();
        System.out.println("total " + (etime - stime) + "ms");
    }

    protected UserAgent parseLine(String line){
        UserAgent userAgent = new UserAgent();

        String[] sections = line.split(";");

        String[] osItems = sections[0].split(",");
        OS os = new OS(osItems[0].substring(osItems[0].indexOf("=") + 1, osItems[0].length()));
        os.version = new Version(osItems[1].substring(osItems[1].indexOf("=") + 1, osItems[1].length()));
        userAgent.setOS(os);

        String[] engineItems = sections[1].split(",");
        Engine engine = new Engine(engineItems[0].substring(engineItems[0].indexOf("=") + 1, engineItems[0].length()));
        engine.version = new Version(engineItems[1].substring(engineItems[1].indexOf("=") + 1, engineItems[1].length()));
        userAgent.setEngine(engine);

        String[] browserItems = sections[2].split(",");
        Browser browser = new Browser(browserItems[0].substring(browserItems[0].indexOf("=") + 1, browserItems[0].length()));
        browser.version = new Version(browserItems[1].substring(browserItems[1].indexOf("=") + 1, browserItems[1].length()));
        browser.mode = browserItems[2].substring(browserItems[2].indexOf("=") + 1, browserItems[2].length());
        userAgent.setBrowser(browser);

        String[] deviceItems = sections[3].split(",");
        Device device = new Device("");
        device.model = deviceItems[0].substring(deviceItems[0].indexOf("=") + 1, deviceItems[0].length());
        device.type = deviceItems[1].substring(deviceItems[1].indexOf("=") + 1, deviceItems[1].length());
        device.manufacturer = deviceItems[2].substring(deviceItems[2].indexOf("=") + 1, deviceItems[2].length());
        userAgent.setDevice(device);

        return userAgent;
    }

    public void batch() throws Exception{
        UA ua = null;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("ua_test_input").getFile());
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        UserAgent ua1 = null;
        UserAgent ua2 = null;
        int index = 1;
        while ((line = br.readLine()) != null) {
            if((index % 2) == 1){
                if(ua == null){
                    ua = new UA(line);
                }else{
                    ua.ua = line;
                }
                ua1 = ua.detect();
                //System.out.println(ua1.toObject());
            }else{
                ua2 = parseLine(line);
                //System.out.println(ua2.toObject());
                assertTrue(ua1.equals(ua2));

                ua1 = null;
                ua2 = null;
                ua.reset();
            }
            index += 1;
            //System.out.println(index);
        }
    }

    public void single(){
        String input = "Mozilla/5.0 (Linux; U; Android 2.3.6; zh-CN; GT-I9070 Build/GINGERBREAD) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.7.0.634 U3/0.8.0 Mobile Safari/534.30";
        //String input = "inTV/2.0.6 (iPhone; iOS 9.3.2; Scale/2.00)";
        UA ua = new UA(input);
        UserAgent userAgent = ua.detect();

        /*
        System.out.println(input);
        String output = "";
        output = userAgent.prettyPrint();
        System.out.println("\033[1;31m" + output + "\033[0m");
        output = userAgent.compactPrint();
        System.out.println("\033[1;31m" + output + "\033[0m");
        */
        System.out.println(userAgent.toObject());

        assertTrue(true);
    }
}
