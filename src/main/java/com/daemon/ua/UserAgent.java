package com.daemon.ua;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class UserAgent {
    Map<String, String> os = new HashMap<String, String>();
    Map<String, String> engine = new HashMap<String, String>();
    Map<String, String> browser = new HashMap<String, String>();
    Map<String, String> device = new HashMap<String, String>();
    String net = "<net>";

    public UserAgent(){
        os.put("name", "");
        os.put("version", "");

        engine.put("name", "");
        engine.put("version", "");

        browser.put("name", "");
        browser.put("version", "");
        browser.put("mode", "");

        device.put("type", "");
        device.put("model", "");
        device.put("manufacturer", "");
    }
    
    protected String strip(String input, String suffix){
        input = input.trim();
        while(input.endsWith(suffix)){
            input = input.substring(0, input.length() - 1).trim();
        }
        return input;
    }

    void setOS(OS os){
        if(os.name != null){
            this.os.put("name", os.name);
        }
        if(os.version != null){
            this.os.put("version", os.version.value);
        }
    }

    void setEngine(Engine engine){
        if(engine.name != null){
            this.engine.put("name", engine.name);
        }
        if(engine.version != null){
            this.engine.put("version", engine.version.value);
        }
    }

    void setBrowser(Browser browser){
        if(browser.name != null){
            this.browser.put("name", browser.name);
        }
        if(browser.version != null){
            this.browser.put("version", browser.version.value);
        }
        if(browser.mode != null){
            this.browser.put("mode", browser.mode);
        }
    }

    void setDevice(Device device){
        if(device.type != null){
            this.device.put("type", device.type);
        }
        if(device.model != null){
            this.device.put("model", strip(device.model, "-"));
        }
        if(device.manufacturer != null){
            this.device.put("manufacturer", device.manufacturer);
        }
    }

    void setNet(String net){
        this.net = net;
    }

    public String prettyPrint(){
        String output = "{\n";
        String indent = "    ";

        output += indent + "\"" + "os" + "\" : {\n";
        output += indent + indent + "\"name\" : \"" + this.os.get("name") + "\",\n";
        output += indent + indent + "\"version\" : \"" + this.os.get("version") + "\"\n";
        output += indent + "},\n";

        output += indent + "\"" + "engine" + "\" : {\n";
        output += indent + indent + "\"name\" : \"" + this.engine.get("name") + "\",\n";
        output += indent + indent + "\"version\" : \"" + this.engine.get("version") + "\"\n";
        output += indent + "},\n";

        output += indent + "\"" + "browser" + "\" : {\n";
        output += indent + indent + "\"name\" : \"" + this.browser.get("name") + "\",\n";
        output += indent + indent + "\"version\" : \"" + this.browser.get("version") + "\",\n";
        output += indent + indent + "\"mode\" : \"" + this.browser.get("mode") + "\"\n";
        output += indent + "},\n";

        output += indent + "\"" + "device" + "\" : {\n";
        output += indent + indent + "\"type\" : \"" + this.device.get("type") + "\",\n";
        output += indent + indent + "\"model\" : \"" + this.device.get("model") + "\",\n";
        output += indent + indent + "\"manufacturer\" : \"" + this.device.get("manufacturer") + "\"\n";
        output += indent + "},\n";

        output += indent + "\"" + "nettype" + "\" : " + this.net + "\n";

        output += "}";
        return output;
    }

    public String compactPrint(){
        String output = "";

        output += "name=" + this.os.get("name") + ",version=" + this.os.get("version") + ";";
        output += "name=" + this.engine.get("name") + ",version=" + this.engine.get("version") + ";";
        output += "name=" + this.browser.get("name") + ",version=" + this.browser.get("version") + ",mode=" + this.browser.get("mode") + ";";
        output += "model=" + this.device.get("model") + ",type=" + this.device.get("type") + ",manufacturer=" + this.device.get("manufacturer") + ";";
        output += "net=" + this.net;

        return output;
    }

    public Map<String, Map<String, String>> toObject(){
        Map<String, Map<String, String>> obj = new HashMap<String, Map<String, String>>(){
            {
                put("os", os);
                put("engine", engine);
                put("browser", browser);
                put("device", device);
            }
        };

        return obj;
    }

    public Boolean equals(UserAgent ua){
        return this.os.get("name").equals(ua.os.get("name"))
            && this.os.get("version").equals(ua.os.get("version"))
            && this.engine.get("name").equals(ua.engine.get("name"))
            && this.engine.get("version").equals(ua.engine.get("version"))
            && this.browser.get("name").equals(ua.browser.get("name"))
            && this.browser.get("version").equals(ua.browser.get("version"))
            && this.browser.get("mode").equals(ua.browser.get("mode"))
            && this.device.get("type").equals(ua.device.get("type"))
            && this.device.get("manufacturer").equals(ua.device.get("manufacturer"))
            && this.device.get("model").equals(ua.device.get("model"));
    }
}
