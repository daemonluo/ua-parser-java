package com.daemon.ua;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class UA {
    String ua;
    Boolean useFeatures = false;
    Boolean detectCamouflage = true;

    OS os;
    Engine engine;
    Device device;
    Browser browser;

    public UA(String ua){
        this.ua = ua;
        this.os = new OS("");
        this.engine = new Engine("");
        this.device = new Device("");
        this.browser = new Browser("");
    }

    protected Matcher matcher(String regex, String input, int flags){
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(input);
        return m;
    }

    protected Matcher matcher(String regex, String input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m;
    }

    protected Matcher matcher(String regex, int flags) {
        return matcher(regex, ua, flags);
    }

    protected Matcher matcher(String regex) {
        return matcher(regex, ua);
    }

    protected Boolean find(String regex, String input, int flags){
        Matcher m = matcher(regex, input, flags);
        return m.find();
    }

    protected Boolean find(String regex, String input){
        Matcher m = matcher(regex, input);
        return m.find();
    }

    protected Boolean find(String regex, int flags){
        return find(regex, ua, flags);
    }

    protected Boolean find(String regex){
        return find(regex, ua);
    }

    protected Boolean contains(String partition) {
        return ua.indexOf(partition) >= 0;
    }

    protected String cleanupModel(String s){
        if(s == null){
            s = "";
        }
        s = s.replaceAll("_TD$", "");
        s = s.replaceAll("_CMCC$", "");

        s = s.replaceAll("_", " ");
        s = s.replaceAll("^\\s+|\\s+$", "");
        s = s.replaceAll("/[^/]+$", "");
        s = s.replaceAll("[^/]+ Android.*", "");

        s = s.replaceAll("^tita on ", "");
        s = s.replaceAll("Android on ", "");
        s = s.replaceAll("Android for ", "");
        s = s.replaceAll("^ICS AOSP on ", "");
        s = s.replaceAll("^Full AOSP on ", "");
        s = s.replaceAll("^Full Android on ", "");
        s = s.replaceAll("^Full Cappuccino on ", "");
        s = s.replaceAll("^Full MIPS Android on ", "");
        s = s.replaceAll("^Full Android", "");

        s = s.replaceAll("(?i)^Acer ?", "");
        s = s.replaceAll("^Iconia ", "");
        s = s.replaceAll("^Ainol ", "");
        s = s.replaceAll("(?i)^Coolpad ?", "Coolpad ");
        s = s.replaceAll("^ALCATEL", "");
        s = s.replaceAll("^Alcatel OT-(.*)", "one touch $1");
        s = s.replaceAll("^YL-", "");
        s = s.replaceAll("^Novo7 ?", "Novo7");
        s = s.replaceAll("^GIONEE", "");
        s = s.replaceAll("^HW-", "");
        s = s.replaceAll("(?i)^Huawei[-\\s]", "Huawei ");
        s = s.replaceAll("(?i)^SAMSUNG[- ]", "");
        s = s.replaceAll("^SonyEricsson", "");
        s = s.replaceAll("^Lenovo Lenovo", "Lenovo");
        s = s.replaceAll("^LNV-Lenovo", "Lenovo");
        s = s.replaceAll("^Lenovo-", "Lenovo ");
        s = s.replaceAll("(LG)[ _/]", "LG-");
        s = s.replaceAll("(HTC.*)\\s(v|V)?[0-9.]+$", "$1");
        s = s.replaceAll("(HTC)[-/]", "HTC ");
        s = s.replaceAll("(HTC)([A-Z][0-9][0-9][0-9])", "$1 $2");
        s = s.replaceAll("(Motorola[\\s|-])", "");
        s = s.replaceAll("(Moto|MOT-)", "");

        s = s.replaceAll("(?i)-?(orange(-ls)?|vodafone|bouygues)$", "");
        s = s.replaceAll("(?i)http://.+$", "");

        s = s.replaceAll("^\\s+|\\s+$", "");

        return s;
    }

    protected void parseOSAndDevice(){
        // Unix
        if(contains("Unix")){
            this.os.name = "Unix";
        }
        if(contains("FreeBSD")){
            this.os.name = "FreeBSD";
        }
        if(contains("OpenBSD")){
            this.os.name = "OpenBSD";
        }
        if(contains("NetBSD")){
            this.os.name = "NetBSD";
        }
        if(contains("SunOS")){
            this.os.name = "Solaris";
        }

        // Linux
        if(contains("Linux")){
            this.os.name = "Linux";

            if(contains("CentOS")){
                this.os.name = "CentOS";
                Matcher m = matcher("CentOS/[0-9\\.\\-]+el([0-9_]+)");
                if(m.find()){
                    this.os.version = new Version(m.group(1).replace("_", "."));
                }
            }

            if(contains("Debian")){
                this.os.name = "Debian";
            }

            if(contains("Fedora")){
                this.os.name = "Fedora";
                Matcher m = matcher("Fedora/[0-9\\.\\-]+fc([0-9]+)");
                if(m.find()){
                    this.os.version = new Version(m.group(1));
                }
            }

            if(contains("Gentoo")){
                this.os.name = "Gentoo";
            }

            if(contains("Kubuntu")){
                this.os.name = "Kubuntu";
            }

            if(contains("Mandriva Linux")){
                this.os.name = "Mandriva";
                Matcher m = matcher("Mandriva Linux/[0-9\\.\\-]+mdv([0-9]+)");
                if(m.find()){
                    this.os.version = new Version(m.group(1));
                }
            }

            if(contains("Mageia")){
                this.os.name = "Mageia";
                Matcher m = matcher("Mageia/[0-9\\.\\-]+mga([0-9]+)");
                if(m.find()){
                    this.os.version = new Version(m.group(1));
                }
            }

            if(contains("Red Hat")){
                this.os.name = "Red Hat";
                Matcher m = matcher("Red Hat[^/]*/[0-9\\.\\-]+el([0-9_]+)");
                if(m.find()){
                    this.os.version = new Version(m.group(1).replace("_", "."));
                }
            }

            if(contains("Slackware")){
                this.os.name = "Slackware";
            }

            if(contains("SUSE")){
                this.os.name = "SUSE";
            }

            if(contains("Turbolinux")){
                this.os.name = "Turbolinux";
            }

            if(contains("Ubuntu")){
                this.os.name = "Ubuntu";
                Matcher m = matcher("Ubuntu/([0-9.]*)");
                if(m.find()){
                    this.os.version = new Version(m.group(1));
                }
            }
        }

        // IOS
        if(find("iPhone( Simulator)?;") || contains("iPad;") || contains("iPod;")){
            this.os.name = "iOS";
            this.os.version = new Version("1.0");

            Matcher m = matcher("OS (.*) like Mac OS X");
            if(m.find()){
                this.os.version = new Version(m.group(1).replace("_", "."));
            }
            
            if(contains("iPhone Simulator")){
                this.device.type = "emulator";
            }else if(contains("iPod;")){
                this.device.type = "media";
                this.device.manufacturer = "Apple";
                this.device.model = "iPod Touch";
            }else if(contains("iPhone")){
                this.device.type = "mobile";
                this.device.manufacturer = "Apple";
                this.device.model = "iPhone";
            }else{
                this.device.type = "tablet";
                this.device.manufacturer = "Apple";
                this.device.model = "iPad";
            }
            this.device.identified = true;
        }else if(contains("Mac OS X")){ // MacOS X
            this.os.name = "Mac OS X";
            Matcher m = matcher("Mac OS X (10[0-9\\._]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1).replace("_", "."));
            }
        }

        // Windows
        if(contains("Windows")){
            this.os.name = "Windows";
            Matcher match = matcher("Windows NT ([0-9]\\.[0-9])");
            if(match.find()){
                String version = match.group(1);
                version = match.group(1);
                //self.os['version'] = parseVersion(version)
                if(version.equals("6.2")){
                    this.os.version = new Version(version, "8");
                }else if(version.equals("6.1")){
                    this.os.version = new Version(version, "7");
                }else if(version.equals("6.0")){
                    this.os.version = new Version(version, "Vista");
                }else if(version.equals("5.2")){
                    this.os.version = new Version(version, "Server 2003");
                }else if(version.equals("5.1")){
                    this.os.version = new Version(version, "XP");
                }else if(version.equals("5.0")){
                    this.os.version = new Version(version, "2000");
                }else{
                    this.os.version = new Version(version, "NT " + String.valueOf(version));
                }
            }

            if(contains("Windows 95") || contains("Win95") || contains("Win 9x 4.00")){
                this.os.version = new Version("4.0", "95");
            }

            if(contains("Windows 98") || contains("Win98") || contains("Win 9x 4.10")){
                this.os.version = new Version("4.1", "98");
            }

            if(contains("Windows ME") || contains("WinME") || contains("Win 9x 4.90")){
                this.os.version = new Version("4.9", "ME");
            }

            if(contains("Windows XP") || contains("WinXP")){
                this.os.version = new Version("5.1", "XP");
            }

            if(contains("WP7")){
                this.os.name = "Windows Phone";
                this.os.version = new Version("7.0");
                this.os.version.details = 2;
                this.device.type = "mobile";
                this.browser.mode = "desktop";
            }

            if(contains("Windows CE") || contains("WinCE") || contains("WindowsCE")){
                if(contains(" IEMobile")){
                    this.os.name = "Windows Mobile";
                    if(contains(" IEMobile 8")){
                        this.os.version = new Version("6.5");
                        this.os.version.details = 2;
                    }
                    if(contains(" IEMobile 7")){
                        this.os.version = new Version("6.1");
                        this.os.version.details = 2;
                    }
                    if(contains(" IEMobile 6")){
                        this.os.version = new Version("6.0");
                        this.os.version.details = 2;
                    }
                }else{
                    this.os.name = "Windows CE";
                    Matcher m = matcher("WindowsCEOS/([0-9.]*)");
                    if(m.find()){
                        this.os.version = new Version(m.group(1));
                        this.os.version.details = 2;
                    }
                    m = matcher("Windows CE ([0-9.]*)");
                    if(m.find()){
                        this.os.version = new Version(m.group(1));
                        this.os.version.details = 2;
                    }
                    this.device.type = "mobile";
                }
            }

            if(contains("Windows Mobile")){
                this.os.name = "Windows Mobile";
                this.device.type = "mobile";
            }

            Matcher m = matcher("WindowsMobile/([0-9.]*)");
            if(m.find()){
                this.os.name = "Windows Mobile";
                this.os.version = new Version(m.group(1));
                this.os.version.details = 2;
                this.device.type = "mobile";
            }

            m = matcher("Windows Phone [0-9]");
            if(m.find()){
                this.os.name = "Windows Mobile";
                Matcher m1 = matcher("Windows Phone ([0-9.]*)");
                m1.find();
                this.os.version = new Version(m1.group(1));
                this.os.version.details = 2;
                this.device.type = "mobile";
            }

            if(contains("Windows Phone OS")){
                this.os.name = "Windows Phone";
                Matcher m1 = matcher("Windows Phone OS ([0-9.]*)");
                m1.find();
                this.os.version = new Version(m1.group(1));
                this.os.version.details = 2;
                try{
                    if(Integer.parseInt(this.os.version.value) < 7){
                        this.os.name = "Windows Mobile";
                    }
                }catch(NumberFormatException e){
                }

                m = matcher("IEMobile/[^;]+; ([^;]+); ([^;]+)[;|\\)]");
                if(m.find()){
                    this.device.manufacturer = m.group(1);
                    this.device.model = m.group(2);
                }

                this.device.type = "mobile";

                String manufacturer = this.device.manufacturer;
                String model = cleanupModel(this.device.model);

                if(MANUFACTURER.WINDOWS_PHONE_MODELS.containsKey(manufacturer)){
                    this.device.manufacturer = MANUFACTURER.WINDOWS_PHONE_MODELS.get(manufacturer).get(model)[0];
                    this.device.model = MANUFACTURER.WINDOWS_PHONE_MODELS.get(manufacturer).get(model)[1];
                    this.device.identified = true;
                }

                if(manufacturer.equals("Microsoft") && model.equals("XDeviceEmulator")){
                    this.device.manufacturer = "";
                    this.device.model = "";
                    this.device.type = "emulator";
                    this.device.identified = true;
                }
            }
        }

        // Android
        if(contains("Android")){
            this.os.name = "Android";
            this.os.version = new Version("");

            Matcher m = matcher("Android(?: )?(?:AllPhone_|CyanogenMod_)?(?:/)?v?([0-9.]+)", ua.replace("-update", "."));
            if(m.find()){
                this.os.version.value = m.group(1);
                this.os.version.details = 3;
            }

            if(contains("Android Eclair")){
                this.os.version.value = "2.0";
                this.os.version.details = 3;
            }

            this.device.type = "mobile";
            try{
                String[] arr = this.os.version.value.split(".");
                if(arr.length > 0){
                    if(Integer.parseInt(arr[0]) >= 3){
                        this.device.type = "tablet";
                    }
                    if(Integer.parseInt(arr[0]) >= 4){
                        this.device.type = "mobile";
                    }
                }
            }catch(NumberFormatException ex){
            }

            if(find("Eclair; (?:[a-zA-Z][a-zA-Z](?:[-_][a-zA-Z][a-zA-Z])?) Build/([^/]*)/")){
                m = matcher("Eclair; (?:[a-zA-Z][a-zA-Z](?:[-_][a-zA-Z][a-zA-Z])?) Build/([^/]*)/");
                m.find();
                this.device.model = m.group(1);
            }else if(find("; ([^;]*[^;\\s])\\s+Build")){
                m = matcher("; ([^;]*[^;\\s])\\s+Build");
                m.find();
                this.device.model = m.group(1);
            }else if(find("[a-zA-Z][a-zA-Z](?:[-_][a-zA-Z][a-zA-Z])?; ([^;]*[^;\\s]);\\s+Build")){
                m = matcher("[a-zA-Z][a-zA-Z](?:[-_][a-zA-Z][a-zA-Z])?; ([^;]*[^;\\s]);\\s+Build");
                m.find();
                this.device.model = m.group(1);
            }else if(find("\\(([^;]+);U;Android/[^;]+;[0-9]+\\*[0-9]+;CTC/2.0\\)")){
                m = matcher("\\(([^;]+);U;Android/[^;]+;[0-9]+\\*[0-9]+;CTC/2.0\\)");
                m.find();
                this.device.model = m.group(1);
            }else if(find(";\\s?([^;]+);\\s?[0-9]+\\*[0-9]+;\\s?CTC2.0")){
                m = matcher(";\\s?([^;]+);\\s?[0-9]+\\*[0-9]+;\\s?CTC2.0");
                m.find();
                this.device.model = m.group(1);
            }else if(find("zh-cn;\\s*(.*?)(/|build)/", Pattern.CASE_INSENSITIVE)){
                m = matcher("zh-cn;\\s*(.*?)(/|build)/", Pattern.CASE_INSENSITIVE);
                m.find();
                this.device.model = m.group(1);
            }else if(find("Android [^;]+; (?:[a-zA-Z][a-zA-Z](?:[-_][a-zA-Z][a-zA-Z])?; )?([^)]+)\\)")){
                m = matcher("Android [^;]+; (?:[a-zA-Z][a-zA-Z](?:[-_][a-zA-Z][a-zA-Z])?; )?([^)]+)\\)");
                if(!find("[a-zA-Z][a-zA-Z](?:[-_][a-zA-Z][a-zA-Z])?")){
                    m.find();
                    this.device.model = m.group(1);
                }
            }else if(find("(.+?)/\\S+", Pattern.CASE_INSENSITIVE)){
                m = matcher("(.+?)/\\S+", Pattern.CASE_INSENSITIVE);
                m.find();
                this.device.model = m.group(1);
            }
            // Sometimes we get a model name that starts with Android, in that case it is mismatch and we should ignore it
            if(this.device.model != null && this.device.model.indexOf("Android") == 0){
                this.device.model = null;
            }
            if(this.device.model != null){
                String model = cleanupModel(this.device.model);
                if(MANUFACTURER.ANDROID_MODELS.containsKey(model)){
                    this.device.manufacturer = MANUFACTURER.ANDROID_MODELS.get(model)[0];
                    this.device.model = MANUFACTURER.ANDROID_MODELS.get(model)[1];
                    if(MANUFACTURER.ANDROID_MODELS.get(model).length > 2){
                        this.device.type = MANUFACTURER.ANDROID_MODELS.get(model)[2];
                    }
                    this.device.identified = true;
                }

                if((model.equals("Emulator")) || (model.equals("x86 Emulator")) || (model.equals("x86 VirtualBox")) || (model.equals("vm"))){
                    this.device.manufacturer = null;
                    this.device.model = null;
                    this.device.type = "emulator";
                    this.device.identified = true;
                }
            }

            if(contains("HP eStation")){
                this.device.manufacturer = "HP";
                this.device.model = "eStation";
                this.device.type = "tablet";
                this.device.identified = true;
            }

            if(contains("Pre/1.0")){
                this.device.manufacturer = "Palm";
                this.device.model = "Pre";
                this.device.identified = true;
            }

            if(contains("Pre/1.1")){
                this.device.manufacturer = "Palm";
                this.device.model = "Pre Plus";
                this.device.identified = true;
            }

            if(contains("Pre/1.2")){
                this.device.manufacturer = "Palm";
                this.device.model = "Pre 2";
                this.device.identified = true;
            }

            if(contains("Pre/3.0")){
                this.device.manufacturer = "HP";
                this.device.model = "Pre 3";
                this.device.identified = true;
            }

            if(contains("Pixi/1.0")){
                this.device.manufacturer = "Palm";
                this.device.model = "Pixi";
                this.device.identified = true;
            }
            
            if(contains("Pixi/1.1")){
                this.device.manufacturer = "Palm";
                this.device.model = "Pixi Plus";
                this.device.identified = true;
            }

            if(contains("P160UN/1.0")){
                this.device.manufacturer = "HP";
                this.device.model = "Veer";
                this.device.identified = true;
            }
        }

        // Google TV
        if(contains("GoogleTV")){
            this.os.name = "Google TV";

            if(contains("Chrome/5.")){
                this.os.version = new Version("1");
            }

            if(contains("Chrome/11.")){
                this.os.version = new Version("2");
            }

            this.device.type = "television";
        }

        // WoPhone
        if(contains("WoPhone")){
            this.os.name = "WoPhone";
            Matcher m = matcher("WoPhone/([0-9\\.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
            this.device.type = "mobile";
        }

        // BlackBerry
        if(contains("BlackBerry")){
            this.os.name = "BlackBerry OS";

            if(!contains("Opera")){
                Matcher m = matcher("BlackBerry([0-9]*)/([0-9.]*)");
                if(m.find()){
                    this.device.model = m.group(1);
                    this.os.version = new Version(m.group(2));
                    this.os.version.details = 2;
                }

                m = matcher("; BlackBerry ([0-9]*)");
                if(m.find()){
                    this.device.model = m.group(1);
                }

                m = matcher("Version/([0-9.]*)");
                if(m.find()){
                    this.os.version = new Version(m.group(1));
                    this.os.version.details = 2;
                }

                if(this.device.model != null){
                    try{
                        String model = this.device.model;
                        if(MANUFACTURER.BLACKBERRY_MODELS.containsKey(model)){
                            this.device.model = "BlackBerry " + MANUFACTURER.BLACKBERRY_MODELS.get(model) + " " + model;
                        }else{
                            this.device.model = "BlackBerry " + model;
                        }
                    }catch(NumberFormatException ex){
                    }
                }
            }else{
                this.device.model = "BlackBerry";
            }

            this.device.manufacturer = "RIM";
            this.device.type = "mobile";
            this.device.identified = true;
        }

        // BlackBerry PlayBook
        if(contains("RIM Tablet OS")){
            this.os.name = "BlackBerry Tablet OS";

            Matcher m = matcher("RIM Tablet OS ([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
                this.os.version.details = 2;
            }
            this.device.manufacturer = "RIM";
            this.device.model = "BlackBerry PlayBook";
            this.device.type = "tablet";
            this.device.identified = true;
        }else if(contains("PlayBook")){
            Matcher m = matcher("Version/(10[0-9.]*)");
            if(m.find()){
                this.os.name = "BlackBerry";
                this.os.version = new Version(m.group(1));
                this.os.version.details = 2;
                this.device.manufacturer = "RIM";
                this.device.model = "BlackBerry PlayBook";
                this.device.type = "tablet";
                this.device.identified = true;
            }
        }

        // WebOS
        if(find("(?:web|hpw)OS")){
            this.os.name = "webOS";
            Matcher m = matcher("(?:web|hpw)OS/([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
            if(contains("tablet")){
                this.device.type = "tablet";
            }else{
                this.device.type = "mobile";
            }

            if(contains("hpwOS")){
                this.device.manufacturer = "HP";
            }else{
                this.device.manufacturer = "Palm";
            }

            if(contains("Pre/1.0")){
                this.device.model = "Pre";
            }
            if(contains("Pre/1.1")){
                this.device.model = "Pre Plus";
            }
            if(contains("Pre/1.2")){
                this.device.model = "Pre2";
            }
            if(contains("Pre/3.0")){
                this.device.model = "Pre3";
            }
            if(contains("Pixi/1.0")){
                this.device.model = "Pixi";
            }
            if(contains("Pixi/1.1")){
                this.device.model = "Pixi Plus";
            }
            if(find("P160UN?A?/1.0")){
                this.device.model = "Veer";
            }
            if(contains("TouchPad/1.0")){
                this.device.model = "TouchPad";
            }
            if(contains("Emulator/") && contains("Desktop/")){
                this.device.type = "emulator";
                this.device.manufacturer = null;
                this.device.model = null;
            }
            this.device.identified = true;
        }

        // S60
        if(contains("Symbian") || contains("S60") || find("Series[ ]?60")){
            this.os.name = "Series60";

            if(contains("SymbianOS/9.1") && !contains("Series60")){
                this.os.version = new Version("3.0");
            }

            Matcher m = matcher("Series60/([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }

            m = matcher("Nokia([^/;]+)[/|;]");
            if(m.find()){
                if(!m.group(1).equals("Browser")){
                    this.device.manufacturer = "Nokia";
                    this.device.model = m.group(1);
                    this.device.identified = true;
                }
            }

            m = matcher("Vertu([^/;]+)[/|;]");
            if(m.find()){
                this.device.manufacturer = "Vertu";
                this.device.model = m.group(1);
                this.device.identified = true;
            }

            m = matcher("/Symbian; U; ([;]+); [a-z][a-z]\\-[a-z][a-z]", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.device.manufacturer = "Nokia";
                this.device.model = m.group(1);
                this.device.identified = true;
            }

            m = matcher("Samsung/([^;]*);");
            if(m.find()){
                this.device.manufacturer = MANUFACTURER.STRINGS_SAMSUNG;
                this.device.model = m.group(1);
                this.device.identified = true;
            }

            this.device.type = "mobile";
        }

        // S40
        if(contains("Series40")){
            this.os.name = "Series40";
            Matcher m = matcher("Nokia([^/]+)/");
            if(m.find()){
                this.device.manufacturer = "Nokia";
                this.device.model = m.group(1);
                this.device.identified = true;
            }
            this.device.type = "mobile";
        }

        //MeeGo
        if(contains("MeeGo")){
            this.os.name = "MeeGo";
            this.device.type = "mobile";

            Matcher m = matcher("Nokia([^\\)]+)\\)");
            if(m.find()){
                this.device.manufacturer = "Nokia";
                this.device.model = m.group(1);
                this.device.identified = true;
            }
        }

        // Maemo
        if(contains("Maemo")){
            this.os.name = "Maemo";
            this.device.type = "mobile";

            Matcher m = matcher("(N[0-9]+)");
            if(m.find()){
                this.device.manufacturer = "Nokia";
                this.device.model = m.group(1);
                this.device.identified = true;
            }
        }

        // Tizen
        if(contains("Tizen")){
            this.os.name = "Tizen";
            Matcher m = matcher("Tizen[/ ]([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
            this.device.type = "Mobile";
            m = matcher("\\(([^;]+); ([^/]+)/");
            if(m.find()){
                if(!m.group(1).equals("Linux")){
                    String manufacturer = m.group(1);
                    String model = m.group(2);
                    this.device.manufacturer = manufacturer;
                    this.device.model = model;
                    if(MANUFACTURER.TIZEN_MODELS.containsKey(manufacturer) && MANUFACTURER.TIZEN_MODELS.get(manufacturer).containsKey(model)){
                        this.device.manufacturer = MANUFACTURER.TIZEN_MODELS.get(manufacturer).get(model)[0];
                        this.device.model = MANUFACTURER.TIZEN_MODELS.get(manufacturer).get(model)[1];
                        this.device.identified = true;
                    }
                }
            }
        }

        // Bada
        if(contains("Bada") || contains("bada")){
            this.os.name = "Bada";
            Matcher m = matcher("[b|B]ada/([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
            this.device.type = "mobile";
            m = matcher("\\(([^;]+); ([^/]+)/");
            if(m.find()){
                this.device.manufacturer = m.group(1);
                this.device.model = cleanupModel(m.group(2));
            }

            String manufacturer = this.device.manufacturer;
            String model = this.device.model;

            if(MANUFACTURER.BADA_MODELS.containsKey(manufacturer) && MANUFACTURER.BADA_MODELS.get(manufacturer).containsKey(model)){
                this.device.manufacturer = MANUFACTURER.BADA_MODELS.get(manufacturer).get(model)[0];
                this.device.model = MANUFACTURER.BADA_MODELS.get(manufacturer).get(model)[1];
                this.device.identified = true;
            }
        }

        // Brew
        if(ua.toLowerCase().indexOf("brew") >= 0 && contains("BMP; U")){
            this.os.name = "Brew";
            this.device.type = "mobile";

            if(find("BREW; U; ([0-9.]*)")){
                Matcher m = matcher("BREW; U; ([0-9.]*)");
                if(m.find()){
                    this.os.version = new Version(m.group(1));
                }
            }else if(find(";BREW/([0-9.]*)", Pattern.CASE_INSENSITIVE)){
                Matcher m = matcher(";BREW/([0-9.]*)", Pattern.CASE_INSENSITIVE);
                if(m.find()){
                    this.os.version = new Version(m.group(1));
                }
            }

            Matcher m = matcher("\\(([^;]+);U;REX[^;]+BREW/[^;]+;(?:.*;)?[0-9]+\\*[0-9]+;CTC/2.0\\)");
            if(m.find()){
                this.device.model = m.group(1);
            }
            if(this.device.model != null){
                String model = cleanupModel(this.device.model);
                if(MANUFACTURER.BREW_MODELS.containsKey(model)){
                    this.device.manufacturer = MANUFACTURER.BREW_MODELS.get(model)[0];
                    this.device.model = MANUFACTURER.BREW_MODELS.get(model)[1];
                    this.device.identified = true;
                }
            }
        }

        // MTK
        if(find("\\(MTK;")){
            this.os.name = "MTK";
            this.device.type = "mobile";
        }

        // CrOS
        if(contains("CrOS")){
            this.os.name = "Chrome OS";
            this.device.type = "desktop";
        }
        
        // Joli OS
        if(contains("Joli OS")){
            this.os.name = "Joli OS";
            this.device.type = "desktop";
            Matcher m = matcher("Joli OS/([0-9.]*)", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
        }

        // Haiku
        if(contains("Haiku")){
            this.os.name = "Haiku";
            this.device.type = "desktop";
        }

        // QNX
        if(contains("QNX")){
            this.os.name = "QNX";
            this.device.type = "mobile";
        }
        
        // OS/2 Warp
        if(contains("OS/2; Warp")){
            this.os.name = "OS/2 Warp";
            this.device.type = "desktop";
            Matcher m = matcher("OS/2; Warp ([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
        }

        // Grid OS
        if(contains("Grid OS")){
            this.os.name = "Grid OS";
            this.device.type = "tablet";
            Matcher m = matcher("Grid OS ([0-9.]*)", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
        }

        // AmigaOS
        if(ua.toLowerCase().indexOf("amigaos") >= 0){
            this.os.name = "AmigaOS";
            this.device.type = "desktop";
            Matcher m = matcher("AmigaOS ([0-9.]*)", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
        }
        
        // MorphOS
        if(ua.toLowerCase().indexOf("morphos") >= 0){
            this.os.name = "MorphOS";
            this.device.type = "desktop";
            Matcher m = matcher("MorphOS ([0-9.]*)", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
        }

        // Kindle
        if(contains("Kindle") && !contains("Fire")){
            this.os.name = "";
            this.device.manufacturer = "Amazon";
            this.device.model = "Kindle";
            this.device.type = "ereader";
            if(contains("Kindle/2.0")){
                this.device.model = "Kindle 2";
            }
            if(contains("Kindle/3.0")){
                this.device.model = "Kindle 3 or later";
            }
            this.device.identified = true;
        }

        // NOOK
        if(contains("nook browser")){
            this.os.name = "Android";
            this.device.manufacturer = "Barnes & Noble";
            this.device.model = "NOOK";
            this.device.type = "ereader";
            this.device.identified = true;
        }

        // Bookeen
        if(contains("bookeen/cybook")){
            this.os.name = "";
            this.device.manufacturer = "Bookeen";
            this.device.model = "Cybook";
            this.device.type = "ereader";
            if(contains("Orizon")){
                this.device.model = "Cybook Orizon";
            }
            this.device.identified = true;
        }

        // Sony Reader
        if(contains("EBRD1101")){
            this.os.name = "";
            this.device.manufacturer = "Sony";
            this.device.model = "Reader";
            this.device.type = "ereader";
            this.device.identified = true;
        }

        // iRiver
        if(contains("Iriver")){
            this.os.name = "";
            this.device.manufacturer = "iRiver";
            this.device.model = "Story";
            this.device.type = "ereader";
            if(contains("EB07")){
                this.device.model = "Story HD EB07";
            }
            this.device.identified = true;
        }

        /**
         * Nintendo
         * Opera/9.30 (Nintendo Wii; U; ; 3642; en)
         * Opera/9.30 (Nintendo Wii; U; ; 2047-7; en)
         * Opera/9.50 (Nintendo DSi; Opera/507; U; en-US)
         * Mozilla/5.0 (Nintendo 3DS; U; ; en) Version/1.7455.US
         * Mozilla/5.0 (Nintendo 3DS; U; ; en) Version/1.7455.EU
         */
        if(contains("Nintendo Wii")){
            this.os.name = "";
            this.device.manufacturer = "Nintendo";
            this.device.model = "Wii";
            this.device.type = "gaming";
            this.device.identified = true;
        }

        if(contains("Nintendo DSi")){
            this.os.name = "";
            this.device.manufacturer = "Nintendo";
            this.device.model = "DSi";
            this.device.type = "gaming";
            this.device.identified = true;
        }

        if(contains("Nintendo 3DS")){
            this.os.name = "";
            this.device.manufacturer = "Nintendo";
            this.device.model = "3DS";
            this.device.type = "gaming";
            Matcher m = matcher("Version/([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
            this.device.identified = true;
        }

        // Sony PlayStation
        if(contains("PlayStation Portable")){
            this.os.name = "";
            this.device.manufacturer = "Sony";
            this.device.model = "PlayStation Portable";
            this.device.type = "gaming";
            this.device.identified = true;
        }
        if(contains("PlayStation Vita")){
            this.os.name = "";
            Matcher m = matcher("PlayStation Vita ([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
            this.device.manufacturer = "Sony";
            this.device.model = "PlayStation Vita";
            this.device.type = "gaming";
            this.device.identified = true;
        }
        if(ua.toLowerCase().indexOf("playstation 3") >= 0){
            this.os.name = "";
            Matcher m = matcher("PLAYSTATION 3;? ([0-9.]*)");
            if(m.find()){
                this.os.version = new Version(m.group(1));
            }
            this.device.manufacturer = "Sony";
            this.device.model = "PlayStation 3";
            this.device.type = "gaming";
            this.device.identified = true;
        }

        /**
         * Panasonic Smart Viera
         *  Mozilla/5.0 (FreeBSD; U; Viera; ja-JP) AppleWebKit/535.1 (KHTML, like Gecko) Viera/1.2.4 Chrome/14.0.835.202 Safari/535.1
         */
        if(contains("Viera")){
            this.os.name = "";
            this.device.manufacturer = "Panasonic";
            this.device.model = "Smart Viera";
            this.device.type = "television";
            this.device.identified = true;
        }

        /**
         * Sharp AQUOS TV
         *  Mozilla/5.0 (DTV) AppleWebKit/531.2  (KHTML, like Gecko) AQUOSBrowser/1.0 (US00DTV;V;0001;0001)
         *  Mozilla/5.0 (DTV) AppleWebKit/531.2+ (KHTML, like Gecko) Espial/6.0.4 AQUOSBrowser/1.0 (CH00DTV;V;0001;0001)
         *  Opera/9.80 (Linux armv6l; U; en) Presto/2.8.115 Version/11.10 AQUOS-AS/1.0 LC-40LE835X
         */
        if(contains("AQUOSBrowser") || contains("AQUOS-AS")){
            this.os.name = "";
            this.device.manufacturer = MANUFACTURER.STRINGS_SHARP;
            this.device.model = "Aquos TV";
            this.device.type = "television";
            this.device.identified = true;
        }

        /**
         * Samsung Samsung TV
         *  Mozilla/5.0 (SmartHub; SMART-TV; U; Linux/SmartTV; Maple2012) AppleWebKit/534.7 (KHTML, like Gecko) SmartTV Safari/534.7
         *  Mozilla/5.0 (SmartHub; SMART-TV; U; Linux/SmartTV) AppleWebKit/531.2+ (KHTML, like Gecko) WebBrowser/1.0 SmartTV Safari/531.2+
         */
        if(contains("SMART-TV")){
            this.os.name = "";
            this.device.manufacturer = MANUFACTURER.STRINGS_SAMSUNG;
            this.device.model = "Smart TV";
            this.device.type = "television";
            this.device.identified = true;
            Matcher m = matcher("Maple([0-9]*)");
            if(m.find()){
                this.device.model = " " + m.group(1);
            }
        }

        /**
         * Sony Internet TV
         *  Opera/9.80 (Linux armv7l; U; InettvBrowser/2.2(00014A;SonyDTV115;0002;0100) KDL-46EX640; CC/USA; en) Presto/2.8.115 Version/11.10
         *  Opera/9.80 (Linux armv7l; U; InettvBrowser/2.2(00014A;SonyDTV115;0002;0100) KDL-40EX640; CC/USA; en) Presto/2.10.250 Version/11.60
         *  Opera/9.80 (Linux armv7l; U; InettvBrowser/2.2(00014A;SonyDTV115;0002;0100) N/A; CC/USA; en) Presto/2.8.115 Version/11.10
         *  Opera/9.80 (Linux mips; U; InettvBrowser/2.2 (00014A;SonyDTV115;0002;0100) ; CC/JPN; en) Presto/2.9.167 Version/11.50
         *  Opera/9.80 (Linux mips; U; InettvBrowser/2.2 (00014A;SonyDTV115;0002;0100) AZ2CVT2; CC/CAN; en) Presto/2.7.61 Version/11.00
         *  Opera/9.80 (Linux armv6l; Opera TV Store/4207; U; (SonyBDP/BDV11); en) Presto/2.9.167 Version/11.50
         *  Opera/9.80 (Linux armv6l ; U; (SonyBDP/BDV11); en) Presto/2.6.33 Version/10.60
         *  Opera/9.80 (Linux armv6l; U; (SonyBDP/BDV11); en) Presto/2.8.115 Version/11.10
         *  Sony Internet TV
         */
        if(find("SonyDTV|SonyBDP|SonyCEBrowser")){
            this.os.name = "";
            this.device.manufacturer = "Sony";
            this.device.model = "Internet TV";
            this.device.type = "television";
            this.device.identified = true;
        }

        /**
         * Philips Net TV
         *
         *  Opera/9.70 (Linux armv6l ; U; CE-HTML/1.0 NETTV/2.0.2; en) Presto/2.2.1
         *  Opera/9.80 (Linux armv6l ; U; CE-HTML/1.0 NETTV/3.0.1;; en) Presto/2.6.33 Version/10.60
         *  Opera/9.80 (Linux mips; U; CE-HTML/1.0 NETTV/3.0.1; PHILIPS-AVM-2012; en) Presto/2.9.167 Version/11.50
         *  Opera/9.80 (Linux mips ; U; HbbTV/1.1.1 (; Philips; ; ; ; ) CE-HTML/1.0 NETTV/3.1.0; en) Presto/2.6.33 Version/10.70
         *  Opera/9.80 (Linux i686; U; HbbTV/1.1.1 (; Philips; ; ; ; ) CE-HTML/1.0 NETTV/3.1.0; en) Presto/2.9.167 Version/11.50
         */
        if(contains("NETTV/")){
            this.os.name = "";
            this.device.manufacturer = "Philips";
            this.device.model = "Net TV";
            this.device.type = "television";
            this.device.identified = true;
        }

        /**
         * LG NetCast TV
         *
         *  Mozilla/5.0 (DirectFB; Linux armv7l) AppleWebKit/534.26+ (KHTML, like Gecko) Version/5.0 Safari/534.26+ LG Browser/5.00.00(+mouse+3D+SCREEN+TUNER; LGE; GLOBAL-PLAT4; 03.09.22; 0x00000001;); LG NetCast.TV-2012
         *  Mozilla/5.0 (DirectFB; Linux armv7l) AppleWebKit/534.26+ (KHTML, like Gecko) Version/5.0 Safari/534.26+ LG Browser/5.00.00(+SCREEN+TUNER; LGE; GLOBAL-PLAT4; 01.00.00; 0x00000001;); LG NetCast.TV-2012
         *  Mozilla/5.0 (DirectFB; U; Linux armv6l; en) AppleWebKit/531.2  (KHTML, like Gecko) Safari/531.2  LG Browser/4.1.4( BDP; LGE; Media/BD660; 6970; abc;); LG NetCast.Media-2011
         *  Mozilla/5.0 (DirectFB; U; Linux 7631; en) AppleWebKit/531.2  (KHTML, like Gecko) Safari/531.2  LG Browser/4.1.4( NO_NUM; LGE; Media/SP520; ST.3.97.409.F; 0x00000001;); LG NetCast.Media-2011
         *  Mozilla/5.0 (DirectFB; U; Linux 7630; en) AppleWebKit/531.2  (KHTML, like Gecko) Safari/531.2  LG Browser/4.1.4( 3D BDP NO_NUM; LGE; Media/ST600; LG NetCast.Media-2011
         *  (LGSmartTV/1.0) AppleWebKit/534.23 OBIGO-T10/2.0
         */
        if(find("LG NetCast\\.(?:TV|Media)-([0-9]*)")){
            Matcher m = matcher("LG NetCast\\.(?:TV|Media)-([0-9]*)");
            this.os.name = "";
            this.device.manufacturer = MANUFACTURER.STRINGS_LG;
            if(m.find()){
                this.device.model = "NetCast TV " + m.group(1);
            }
            this.device.type = "television";
            this.device.identified = true;
        }

        if(contains("LGSmartTV")){
            this.os.name = "";
            this.device.manufacturer = MANUFACTURER.STRINGS_LG;
            this.device.model = "Smart TV";
            this.device.type = "television";
            this.device.identified = true;
        }

        /**
         * Toshiba Smart TV
         *
         *  Mozilla/5.0 (Linux mipsel; U; HbbTV/1.1.1 (; TOSHIBA; DTV_RL953; 56.7.66.7; t12; ) ; ToshibaTP/1.3.0 (+VIDEO_MP4+VIDEO_X_MS_ASF+AUDIO_MPEG+AUDIO_MP4+DRM+NATIVELAUNCH) ; en) AppleWebKit/534.1 (KHTML, like Gecko)
         *  Mozilla/5.0 (DTV; TSBNetTV/T32013713.0203.7DD; TVwithVideoPlayer; like Gecko) NetFront/4.1 DTVNetBrowser/2.2 (000039;T32013713;0203;7DD) InettvBrowser/2.2 (000039;T32013713;0203;7DD)
         *  Mozilla/5.0 (Linux mipsel; U; HbbTV/1.1.1 (; TOSHIBA; 40PX200; 0.7.3.0.; t12; ) ; Toshiba_TP/1.3.0 (+VIDEO_MP4+AUDIO_MPEG+AUDIO_MP4+VIDEO_X_MS_ASF+OFFLINEAPP) ; en) AppleWebKit/534.1 (KHTML, like Gec
         */
        if(find("Toshiba_?TP/") || find("TSBNetTV/")){
            this.os.name = "";
            this.device.manufacturer = "Toshiba";
            this.device.model = "Smart TV";
            this.device.type = "television";
            this.device.identified = true;
        }

        // MachBlue XT
        if(find("mbxtWebKit/([0-9.]*)")){
            this.os.name = "";
            Matcher m = matcher("mbxtWebKit/([0-9.]*)");
            this.browser.name = "MachBlue XT";
            if(m.find()){
                this.browser.version = new Version(m.group(1));
                this.browser.version.details = 2;
            }
            this.device.type = "television";
        }

        // ADB
        if(find("\\(ADB; ([^\\)]+)\\)")){
            this.os.name = "";
            Matcher m = matcher("\\(ADB; ([^\\)]+)\\)");
            this.device.manufacturer = "ADB";
            if(m.find()){
                if(m.group(1).equals("Unknown")){
                    this.device.model = m.group(1).replace("ADB", "") + "IPTV receiver";
                }else{
                    this.device.model = "IPTV receiver";
                }
            }
            this.device.type = "television";
            this.device.identified = true;
        }

        // MStar
        if(contains("MStar;OWB")){
            this.os.name = "";
            this.device.manufacturer = "MStar";
            this.device.model = "PVR";
            this.device.type = "television";
            this.device.identified = true;
            this.browser.name = "Origyn Web Browser";
        }

        // TechniSat
        if(find("\\\\TechniSat ([^;]+);")){
            this.os.name = "";
            Matcher m = matcher("\\\\TechniSat ([^;]+);");
            this.device.manufacturer = "TechniSat";
            if(m.find()){
                this.device.model = m.group(1);
            }
            this.device.type = "television";
            this.device.identified = true;
        }

        // Technicolor
        if(find("\\\\Technicolor_([^;]+);")){
            this.os.name = "";
            Matcher m = matcher("\\\\Technicolor_([^;]+);");
            this.device.manufacturer = "Technicolor";
            if(m.find()){
                this.device.model = m.group(1);
            }
            this.device.type = "television";
            this.device.identified = true;
        }

        // Winbox Evo2
        if(contains("Winbox Evo2")){
            this.os.name = "";
            this.device.manufacturer = "Winbox";
            this.device.model = "Evo2";
            this.device.type = "television";
            this.device.identified = true;
        }

        // Roku
        if(find("^Roku/DVP-([0-9]+)")){
            this.device.manufacturer = "Roku";
            this.device.type = "television";
            Matcher m = matcher("^Roku/DVP-([0-9]+)");
            if(m.find()){
                String group = m.group(1);
                if(group.equals("2000")){
                    this.device.model = "HD";
                }else if(group.equals("2050")){
                    this.device.model = "XD";
                }else if(group.equals("2100")){
                    this.device.model = "XDS";
                }else if(group.equals("2400")){
                    this.device.model = "LT";
                }else if(group.equals("3000")){
                    this.device.model = "2 HD";
                }else if(group.equals("3050")){
                    this.device.model = "2 XD";
                }else if(group.equals("3100")){
                    this.device.model = "2 XS";
                }
            }
            this.device.identified = true;
        }

        if(find("HbbTV/1.1.1 \\([^;]*;\\s*([^;]*)\\s*;\\s*([^;]*)\\s*;")){
            this.device.type = "television";
            Matcher m = matcher("HbbTV/1.1.1 \\([^;]*;\\s*([^;]*)\\s*;\\s*([^;]*)\\s*;");
            if(m.find()){
                String vendorName = m.group(1).trim();
                String modelName = m.group(2).trim();
                if(this.device.manufacturer != null && vendorName != "" && !vendorName.equals("vendorName")){
                    if(vendorName.equals("LGE")){
                        this.device.manufacturer = "LG";
                    }else if(vendorName.equals("TOSHIBA")){
                        this.device.manufacturer = "Toshiba";
                    }else if(vendorName.equals("smart")){
                        this.device.manufacturer = "Smart";
                    }else if(vendorName.equals("tv2n")){
                        this.device.manufacturer = "TV2N";
                    }else{
                        this.device.manufacturer = vendorName;
                    }

                    if(this.device.model != null && modelName != "" && !modelName.equals("modelName")){
                        if(modelName.equals("GLOBAL_PLAT3")){
                            this.device.model = "NetCast TV";
                        }else if(modelName.equals("SmartTV2012")){
                            this.device.model = "Smart TV 2012";
                        }else if(modelName.equals("videoweb")){
                            this.device.model = "Videoweb";
                        }else{
                            this.device.model = modelName;
                        }

                        if(vendorName.equals("Humax")){
                            this.device.model = this.device.model.toUpperCase();
                        }
                        this.device.identified = true;
                        this.os.name = "";
                    }
                }
            }
        }

        // Detect type based on common identifiers
        if(contains("InettvBrowser")){
            this.device.type = "television";
        }
        if(contains("MIDP")){
            this.device.type = "mobile";
        }

        // Try to detect any devices based on common locations of model ids
        if(this.device.model == null && this.device.manufacturer == null){
            List<String> candidates = new ArrayList<String>();
            if(!find("^(Mozilla|Opera)")){
                Matcher m = matcher("^(?:MQQBrowser/[0-9\\.]+/)?([^\\s]+)");
                if(m.find()){
                    String group = m.group(1);
                    group = group.replaceAll("_TD$", "");
                    group = group.replaceAll("_CMCC$", "");
                    group = group.replaceAll("[_ ]Mozilla$", "");
                    group = group.replaceAll(" Linux$", "");
                    group = group.replaceAll(" Opera$", "");
                    group = group.replaceAll("/[0-9].*$", "");
                    candidates.add(group);
                }
            }

            Matcher m = matcher("[0-9]+x[0-9]+; ([^;]+)");
            if(m.find()){
                candidates.add(m.group(1));
            }

            m = matcher("[0-9]+X[0-9]+ ([^;/\\(\\)]+)");
            if(m.find()){
                candidates.add(m.group(1));
            }

            m = matcher("Windows NT 5.1; ([^;]+); Windows Phone");
            if(m.find()){
                candidates.add(m.group(1));
            }

            m = matcher("\\) PPC; (?:[0-9]+x[0-9]+; )?([^;/\\(\\)]+)");
            if(m.find()){
                candidates.add(m.group(1));
            }
            
            m = matcher("\\(([^;]+); U; Windows Mobile");
            if(m.find()){
                candidates.add(m.group(1));
            }

            m = matcher("Vodafone/1.0/([^/]+)");
            if(m.find()){
                candidates.add(m.group(1));
            }

            m = matcher("\\\\ ([^\\s]+)$");
            if(m.find()){
                candidates.add(m.group(1));
            }

            for(String candidate : candidates){
                Boolean result = false;
                if(this.device.model == null && this.device.manufacturer == null){
                    String model = cleanupModel(candidate);
                    if(this.os.name.equals("Android")){
                        if(MANUFACTURER.ANDROID_MODELS.containsKey(model)){
                            this.device.manufacturer = MANUFACTURER.ANDROID_MODELS.get(model)[0];
                            this.device.model = MANUFACTURER.ANDROID_MODELS.get(model)[1];
                            if(MANUFACTURER.ANDROID_MODELS.get(model).length > 2){
                                this.device.type = MANUFACTURER.ANDROID_MODELS.get(model)[2];
                            }
                            this.device.identified = true;
                            result = true;
                        }
                    }

                    if((this.os.name == null || this.os.name.isEmpty()) || this.os.name.equals("Windows") || this.os.name.equals("Windows Mobile") || this.os.name.equals("Windows CE")){
                        if(MANUFACTURER.WINDOWS_MOBILE_MODELS.containsKey(model)){
                            this.device.manufacturer = MANUFACTURER.WINDOWS_MOBILE_MODELS.get(model)[0];
                            this.device.model = MANUFACTURER.WINDOWS_MOBILE_MODELS.get(model)[1];
                            this.device.type = "mobile";
                            this.device.identified = true;

                            if(!this.os.name.equals("Windows Mobile")){
                                this.os.name = "Windows Mobile";
                                this.os.version = null;
                            }

                            result = true;
                        }
                    }
                }

                if(!result){
                    m = matcher("^GIONEE-([^\\s]+)", candidate);
                    if(m.find()){
                        this.device.manufacturer = "Gionee";
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";
                        this.device.identified = true;
                    }

                    m = matcher("^HTC_?([^/_]+)(?:/|_|$)", candidate);
                    if(m.find()){
                        this.device.manufacturer = MANUFACTURER.STRINGS_HTC;
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";
                        this.device.identified = true;
                    }

                    m = matcher("^HUAWEI-([^/]*)", candidate);
                    if(m.find()){
                        this.device.manufacturer = MANUFACTURER.STRINGS_HUAWEI;
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";
                        this.device.identified = true;
                    }

                    m = matcher("(?:^|\\()LGE?(?:/|-|_|\\s)([^\\s]*)", candidate);
                    if(m.find()){
                        this.device.manufacturer = MANUFACTURER.STRINGS_LG;
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";
                        this.device.identified = true;
                    }

                    m = matcher("^MOT-([^/_]+)(?:/|_|$)", candidate);
                    if(m.find()){
                        this.device.manufacturer = MANUFACTURER.STRINGS_MOTOROLA;
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";
                        this.device.identified = true;
                    }

                    m = matcher("^Motorola_([^/_]+)(?:/|_|$)", candidate);
                    if(m.find()){
                        this.device.manufacturer = MANUFACTURER.STRINGS_MOTOROLA;
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";
                        this.device.identified = true;
                    }

                    m = matcher("^Nokia([^/]+)(?:/|$)", candidate);
                    if(m.find()){
                        this.device.manufacturer = "Nokia";
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";
                        this.device.identified = true;

                        if(this.os.name == "" || this.os.name == null){
                            this.os.name = "Series40";
                        }
                    }

                    m = matcher("^SonyEricsson([^/_]+)(?:/|_|$)", candidate);
                    if(m.find()){
                        this.device.manufacturer = MANUFACTURER.STRINGS_SONY_ERICSSON;
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";
                        this.device.identified =true;
                    }

                    m = matcher("^SAMSUNG-([^/_]+)(?:/|_|$)", candidate);
                    if(m.find()){
                        this.device.manufacturer = MANUFACTURER.STRINGS_SAMSUNG;
                        this.device.model = cleanupModel(m.group(1));
                        this.device.type = "mobile";

                        if(this.os.name.equals("Bada")){
                            String manufacturer = "SAMSUNG";
                            String model = cleanupModel(this.device.model);

                            if(MANUFACTURER.BADA_MODELS.containsKey(manufacturer) && MANUFACTURER.BADA_MODELS.get(manufacturer).containsKey(model)){
                                this.device.manufacturer = MANUFACTURER.BADA_MODELS.get(manufacturer).get(model)[0];
                                this.device.model = MANUFACTURER.BADA_MODELS.get(manufacturer).get(model)[1];
                                this.device.identified = true;
                            }
                        }else if(find("Jasmine/([0-9.]*)")){
                            m = matcher("Jasmine/([0-9.]*)");
                            m.find();
                            String version = m.group(1);
                            String manufacturer = "SAMSUNG";
                            String model = cleanupModel(this.device.model);

                            if(MANUFACTURER.TOUCHWIZ_MODELS.containsKey(manufacturer) && MANUFACTURER.TOUCHWIZ_MODELS.get(manufacturer).containsKey(model)){
                                this.device.manufacturer = MANUFACTURER.TOUCHWIZ_MODELS.get(manufacturer).get(model)[0];
                                this.device.model = MANUFACTURER.TOUCHWIZ_MODELS.get(manufacturer).get(model)[1];
                                this.device.identified = true;
                                this.os.name = "Touchwiz";
                                this.os.version = new Version("2.0");
                            }
                        }else if(find("Dolfin/([0-9.]*)")){
                            m = matcher("Dolfin/([0-9.]*)");
                            m.find();
                            String version = m.group(1);
                            String manufacturer = "SAMSUNG";
                            String model = cleanupModel(this.device.model);
                            if(MANUFACTURER.BADA_MODELS.containsKey(manufacturer) && MANUFACTURER.BADA_MODELS.get(manufacturer).containsKey(model)){
                                this.device.manufacturer = MANUFACTURER.BADA_MODELS.get(manufacturer).get(model)[0];
                                this.device.model = MANUFACTURER.BADA_MODELS.get(manufacturer).get(model)[1];
                                this.device.identified = true;
                                this.os.name = "Bada";
                                if(version.equals("2.0")){
                                    this.os.version = new Version("1.0");
                                }else if(version.equals("2.2")){
                                    this.os.version = new Version("1.2");
                                }else if(version.equals("3.0")){
                                    this.os.version = new Version("2.0");
                                }
                            }
                            if(MANUFACTURER.TOUCHWIZ_MODELS.containsKey(manufacturer) && MANUFACTURER.TOUCHWIZ_MODELS.get(manufacturer).containsKey(model)){
                                this.device.manufacturer = MANUFACTURER.TOUCHWIZ_MODELS.get(manufacturer).get(model)[0];
                                this.device.model = MANUFACTURER.TOUCHWIZ_MODELS.get(manufacturer).get(model)[1];
                                this.device.identified = true;
                                this.os.name = "Touchwiz";
                                if(version.equals("1.0")){
                                    this.os.version = new Version("1.0");
                                }else if(version.equals("1.5")){
                                    this.os.version = new Version("2.0");
                                }else if(version.equals("2.0")){
                                    this.os.version = new Version("3.0");
                                }
                            }
                        }
                    }
                }
            }
        }

        if(find("\\((?:LG[-|/])(.*) (?:Browser/)?AppleWebkit")){
            Matcher m = matcher("\\((?:LG[-|/])(.*) (?:Browser/)?AppleWebkit");
            m.find();
            this.device.manufacturer = MANUFACTURER.STRINGS_LG;
            this.device.model = m.group(1);
            this.device.type = "mobile";
            this.device.identified = true;
        }
        if(find("^Mozilla/5.0 \\((?:Nokia|NOKIA)(?:\\s?)([^\\)]+)\\)UC AppleWebkit\\(like Gecko\\) Safari/530$")){
            Matcher m = matcher("^Mozilla/5.0 \\((?:Nokia|NOKIA)(?:\\s?)([^\\)]+)\\)UC AppleWebkit\\(like Gecko\\) Safari/530$");
            m.find();
            this.device.manufacturer = "Nokia";
            this.device.model = m.group(1);
            this.device.type = "mobile";
            this.device.identified = true;
            this.os.name = "Series60";
        }
    }

    protected void parseBrowser(){
        // Safari
        if(contains("Safari")){
            if(this.os.name.equals("iOS")){
                this.browser.stock = true;
                this.browser.hidden = true;
                this.browser.name = "Safari";
                this.browser.version = null;
            }

            if(this.os.name.equals("Mac OS X") || this.os.name.equals("Windows")){
                this.browser.name = "Safari";
                this.browser.stock = (this.os.name.equals("Windows"));

                Matcher m = matcher("Version/([0-9\\.]+)");
                if(m.find()){
                    this.os.version = new Version(m.group(1));
                }

                m = matcher("AppleWebKit/[0-9\\.]+\\+");
                if(m.find()){
                    this.browser.name = "WebKit Nightly Build";
                    this.browser.version = null;
                }
            }
        }

        // Internet Explorer
        if(contains("MSIE")){
            this.browser.name = "Internet Explorer";
            if(contains("IEMobile") || contains("Windows CE") || contains("Windows Phone") || contains("WP7")){
                this.browser.name = "Mobile Internet Explorer";
            }
            Matcher m = matcher("MSIE ([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // Opera
        if(ua.toLowerCase().indexOf("opera") >= 0){
            this.browser.stock = false;
            this.browser.name = "Opera";

            Matcher m = matcher("Opera[/| ]([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            m = matcher("Version/([0-9.]*)");
            if(m.find()){
                try{
                    if(Integer.parseInt(m.group(1)) >= 10){
                        this.browser.version = new Version(m.group(1));
                    }else{
                        this.browser.version = null;
                    }
                }catch(NumberFormatException ex){
                    this.browser.version = null;
                }
            }

            if(this.browser.version != null && contains("Edition Labs")){
                this.browser.version.type = "alpha";
                this.browser.channel = "Labs";
            }

            if(this.browser.version != null && contains("Edition Next")){
                this.browser.version.type = "alpha";
                this.browser.channel = "Next";
            }

            if(contains("Opera Tablet")){
                this.browser.name = "Opera Mobile";
                this.device.type = "tablet";
            }

            if(contains("Opera Mobi")){
                this.browser.name = "Opera Mobile";
                this.device.type = "mobile";
            }

            if(contains("Opera Mini;")){
                this.browser.name = "Opera Mini";
                this.browser.version = null;
                this.browser.mode = "proxy";
                this.device.type = "mobile";
            }

            m = matcher("Opera Mini/(?:att/)?([0-9.]*)");
            if(m.find()){
                this.browser.name = "Opera Mini";
                this.browser.version = new Version(m.group(1));
                this.browser.version.details = -1;
                this.browser.mode = "proxy";
                this.device.type = "mobile";
            }

            if(this.browser.name.equals("Opera") && this.device.type.equals("mobile")){
                this.browser.name = "Opera Mobile";
                if(contains("BER")){
                    this.browser.name = "Opera Mini";
                    this.browser.version = null;
                }
            }

            if(contains("InettvBrowser")){
                this.device.type = "television";
            }

            if(contains("Opera TV") || contains("Opera-TV")){
                this.browser.name = "Opera";
                this.device.type = "television";
            }

            if(contains("Linux zbov")){
                this.browser.name = "Opera Mobile";
                this.browser.mode = "desktop";
                this.device.type = "mobile";
                this.os.name = null;
                this.os.version = null;
            }

            if(contains("Linux zvav")){
                this.browser.name = "Opera Mini";
                this.browser.version = null;
                this.browser.mode = "desktop";
                this.device.type = "mobile";
                this.os.name = null;
                this.os.version = null;
            }
        }

        // Firefox
        if(contains("Firefox")){
            this.browser.stock = false;
            this.browser.name = "Firefox";

            Matcher m = matcher("Firefox/([0-9ab.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            if(this.browser.version != null && this.browser.version.type.equals("alpha")){
                this.browser.channel = "Aurora";
            }
            if(this.browser.version != null && this.browser.version.type.equals("beta")){
                this.browser.channel = "Beta";
            }

            if(contains("Fennec")){
                this.device.type = "mobile";
            }
            if(contains("Mobile; rv")){
                this.device.type = "mobile";
            }
            if(contains("Tablet; rv")){
                this.device.type = "tablet";
            }

            if(this.device.type.equals("mobile") || this.device.type.equals("tablet")){
                this.browser.name = "Firefox Mobile";
            }
        }

        if(contains("Namoroka")){
            this.browser.stock = false;
            this.browser.name = "Firefox";
            Matcher m = matcher("/Namoroka/([0-9ab.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            this.browser.channel = "Namoroka";
        }

        if(contains("Shiretoko")){
            this.browser.stock = false;
            this.browser.name = "Firefox";
            Matcher m = matcher("Shiretoko/([0-9ab.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            this.browser.channel = "Shiretoko";
        }

        if(contains("Minefield")){
            this.browser.stock = false;
            this.browser.name = "Firefox";
            Matcher m = matcher("Minefield/([0-9ab.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            this.browser.channel = "Minefield";
        }

        if(contains("Firebird")){
            this.browser.stock = false;
            this.browser.name = "Firebird";
            Matcher m = matcher("Firebird/([0-9ab.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // SeaMonkey
        if(contains("SeaMonkey")){
            this.browser.stock = false;
            this.browser.name = "SeaMonkey";
            Matcher m = matcher("SeaMonkey/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // Netscape
        if(contains("Netscape")){
            this.browser.stock = false;
            this.browser.name = "Netscape";
            Matcher m = matcher("Netscape[0-9]?/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // Konqueror
        if(ua.toLowerCase().indexOf("konqueror") >= 0){
            this.browser.name = "Konqueror";
            Matcher m = matcher("[k|K]onqueror/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // Chrome
        if(find("(?:Chrome|CrMo|CriOS)/([0-9.]*)")){
            Matcher m = matcher("(?:Chrome|CrMo|CriOS)/([0-9.]*)");
            m.find();
            this.browser.stock = false;
            this.browser.name = "Chrome";
            this.browser.version = new Version(m.group(1));

            String version_prefix = String.join(".", Arrays.copyOfRange(m.group(1).split("."), 0, 3));
            if(this.os.name.equals("Android")){
                if(version_prefix.equals("16.0.912")){
                    this.browser.channel = "Beta";
                }else if(version_prefix.equals("18.0.1025")){
                    this.browser.version.details = 1;
                }else{
                    this.browser.channel = "Nightly";
                }
            }
        }

        // Chrome Frame
        if(contains("chromeframe")){
            this.browser.stock = false;
            this.browser.name = "Chrome Frame";
            Matcher m = matcher("chromeframe/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // Chromium
        if(contains("Chromium")){
            this.browser.stock = false;
            this.browser.channel = "";
            this.browser.name = "Chromium";
            Matcher m = matcher("Chromium/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }
        // BrowserNG
        if(contains("BrowserNG")){
            this.browser.name = "Nokia Browser";
            Matcher m = matcher("BrowserNG/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
                this.browser.version.details = 3;
            }
        }

        // Nokia Browser
        if(contains("NokiaBrowser")){
            this.browser.name = "Nokia Browser";
            Matcher m = matcher("NokiaBrowser/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
                this.browser.version.details = 3;
            }
        }

        // MicroB
        if(find("Maemo[ |_]Browser")){
            this.browser.name = "MicroB";
            Matcher m = matcher("Maemo[ |_]Browser[ |_]([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
                this.browser.version.details = 3;
            }
        }

        // NetFront
        if(contains("NetFront")){
            this.browser.name = "NetFront";
            this.device.type = "mobile";
            Matcher m = matcher("NetFront/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            if(contains("InettvBrowser")){
                this.device.type = "television";
            }
        }

        // Silk
        if(contains("Silk-Accelerated") ){
            this.browser.name = "Silk";
            Matcher m = matcher("Silk/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
                this.browser.version.details = 2;
            }
            this.device.manufacturer = "Amazon";
            this.device.model = "Kindle Fire";
            this.device.type = "tablet";
            this.device.identified = true;

            if(!this.os.name.equals("Android")){
                this.os.name = "Android";
                this.os.version = null;
            }
        }

        // Dolfin
        if(contains("Dolfin")){
            this.browser.name = "Dolfin";
            Matcher m = matcher("Dolfin/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // Iris
        if(contains("Iris")){
            this.browser.name = "Iris";
            this.device.type = "mobile";
            this.device.model = null;
            this.device.manufacturer = null;
            this.os.name = "Windows Mobile";
            this.os.version = null;
            Matcher m = matcher("Iris/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            m = matcher(" WM([0-9])");
            if(m.find()){
                this.browser.version = new Version(m.group(1) + ".0");
            }else{
                this.browser.mode = "desktop";
            }
        }

        // Jasmine
        if(contains("Jasmine")){
            this.browser.name = "Jasmine";
            Matcher m = matcher("Jasmine/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // Boxee
        if(contains("Boxee")){
            this.browser.name = "Boxee";
            this.device.type = "television";
            Matcher m = matcher("Boxee/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // Espial
        if(contains("Espial")){
            this.browser.name = "Espial";
            this.os.name = "";
            this.os.version = null;
            if(!this.device.type.equals("television")){
                this.device.type = "television";
                this.device.model = null;
                this.device.manufacturer = null;
            }
            Matcher m = matcher("Espial/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
        }

        // ANT Galio
        if(find("ANTGalio/([0-9.]*)")){
            Matcher m = matcher("ANTGalio/([0-9.]*)");
            m.find();
            this.browser.name = "ANT Galio";
            this.browser.version = new Version(m.group(1));
            this.browser.version.details = 3;
            this.device.type = "television";
        }

        // NetFront NX
        if(find("NX/([0-9.]*)")){
            Matcher m = matcher("NX/([0-9.]*)");
            m.find();
            this.browser.name = "NetFront NX";
            this.browser.version = new Version(m.group(1));
            this.browser.version.details = 2;
            if(ua.toUpperCase().indexOf("DTV") >= 0){
                this.device.type = "television";
            }else if(ua.toLowerCase().indexOf("mobile") >= 0){
                this.device.type = "mobile";
            }else{
                this.device.type = "desktop";
            }
            this.os.name = null;
            this.os.version = null;
        }

        // Obigo
        if(ua.toLowerCase().indexOf("obigo") >= 0){
            this.browser.name = "Obigo";

            Matcher m = matcher("Obigo/([0-9.]*)", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            m = matcher("Obigo/([A-Z])([0-9.]*)", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.browser.name = "Obigo " + m.group(1);
                this.browser.version = new Version(m.group(2));
            }
            m = matcher("Obigo-([A-Z])([0-9.]*)/", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.browser.name = "Obigo " + m.group(1);
                this.browser.version = new Version(m.group(2));
            }
        }

        // UC Web
        if(contains("UCWEB")){
            this.browser.stock = false;
            this.browser.name = "UC Browser";

            Matcher m = matcher("UCWEB([0-9]*[.][0-9]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
                this.browser.version.details = 3;
            }

            if(this.os.name.equals("Linux")){
                this.os.name = "";
            }

            this.device.type = "mobile";

            m = matcher("^IUC \\(U;\\s?iOS ([0-9\\.]+);");
            if(m.find()){
                this.os.name = "iOS";
                this.os.version = new Version(m.group(1));
            }

            m = matcher("^JUC \\(Linux; U; ([0-9\\.]+)[^;]*; [^;]+; ([^;]*[^\\s])\\s*; [0-9]+\\*[0-9]+\\)");
            if(m.find()){
                String model = cleanupModel(m.group(2));
                this.os.name = "Android";
                this.os.version = new Version(m.group(1));

                if(MANUFACTURER.ANDROID_MODELS.containsKey(model)){
                    this.device.manufacturer = MANUFACTURER.ANDROID_MODELS.get(model)[0];
                    this.device.model = MANUFACTURER.ANDROID_MODELS.get(model)[1];
                    if(MANUFACTURER.ANDROID_MODELS.get(model).length > 2){
                        this.device.type = MANUFACTURER.ANDROID_MODELS.get(model)[2];
                    }
                    this.device.identified = true;
                }
            }

            m = matcher("\\) UC");
            if(m.find()){
                this.browser.stock = false;
                this.browser.name = "UC Browser";
            }

            m = matcher("UCBrowser/([0-9.]*)");
            if(m.find()){
                this.browser.stock = false;
                this.browser.name = "UC Browser";
                this.browser.version = new Version(m.group(1));
                this.browser.version.details = 2;
            }
        }

        // NineSky
        if(find("NineSky(?:-android-mobile(?:-cn)?)?/([0-9.]*)")){
            Matcher m = matcher("NineSky(?:-android-mobile(?:-cn)?)?/([0-9.]*)");
            m.find();
            this.browser.name = "NineSky";
            this.browser.version = new Version(m.group(1));
            if(!this.os.name.equals("Android")){
                this.os.name = "Android";
                this.os.version = null;
                this.device.manufacturer = null;
                this.device.model = null;
            }
        }

        // Skyfire
        if(find("Skyfire/([0-9.]*)")){
            Matcher m = matcher("Skyfire/([0-9.]*)");
            if(m.find()){
                this.browser.version = new Version(m.group(1));
            }
            this.browser.name = "Skyfire";
            this.device.type = "mobile";
            this.os.name = "Android";
            this.os.version = null;
        }

        // Dolphin HD
        if(find("DolphinHDCN/([0-9.]*)")){
            Matcher m = matcher("DolphinHDCN/([0-9.]*)");
            m.find();
            this.browser.name = "Dolphin";
            this.browser.version = new Version(m.group(1));
            this.device.type = "mobile";
            if(!this.os.name.equals("Android")){
                this.os.name = "Android";
                this.os.version = null;
            }
        }
        if(find("Dolphin/INT")){
            this.browser.name = "Dolphin";
            this.device.type = "mobile";
        }
        
        // QQ Browser
        if(find("(M?QQBrowser)/([0-9.]*)")){
            Matcher m = matcher("(M?QQBrowser)/([0-9.]*)");
            this.browser.name = "QQ Browser";
            m.find();
            String version = m.group(2);
            if(find("^[0-9][0-9]$", version)){
                version = version.charAt(0) + "." + version.charAt(1);
            }
            this.browser.version = new Version(version);
            this.browser.version.details = 2;
            this.browser.channel = "";
            if((this.os.name == null || this.os.name.isEmpty()) || m.group(1).equals("QQBrowser")){
                this.os.name = "Windows";
            }
        }

        // iBrowser
        if(find("(iBrowser)/([0-9.]*)")){
            Matcher m = matcher("(iBrowser)/([0-9.]*)");
            m.find();
            this.browser.name = "iBrowser";
            String version = m.group(2);
            if(find("^[0-9][0-9]$", version)){
                version = version.charAt(0) + "." + version.charAt(1);
            }
            this.browser.version = new Version(version);
            this.browser.version.details = 2;
            this.browser.channel = "";
        }

        // Puffin
        if(find("Puffin/([0-9.]*)")){
            Matcher m = matcher("Puffin/([0-9.]*)");
            m.find();
            this.browser.name = "Puffin";
            this.browser.version = new Version(m.group(1));
            this.browser.version.details = 2;
            this.device.type = "mobile";
            if(this.os.name.equals("Linux")){
                this.os.name = null;
                this.os.version = null;
            }
        }

        // 360 Extreme Explorer
        if(contains("360EE")){
            this.browser.stock = false;
            this.browser.name = "360 Extreme Explorer";
            this.browser.version = null;
        }

        // Midori
        if(find("Midori/([0-9.]*)")){
            Matcher m = matcher("Midori/([0-9.]*)");
            m.find();
            this.browser.name = "Midori";
            this.browser.version = new Version(m.group(1));
            if(!this.os.name.equals("Linux")){
                this.os.name = "Linux";
                this.os.version = null;
            }
            this.device.manufacturer = null;
            this.device.model = null;
            this.device.type = "desktop";
        }

        // Others
        for(Map<String, String> b : MANUFACTURER.OTHER_BROWSERS){
            Matcher m = null;
            if(b.containsKey("flag")){
                try{
                    m = matcher(b.get("regexp"), Integer.parseInt(b.get("flag")));
                }catch(NumberFormatException ex){}
            }else{
                m = matcher(b.get("regexp"));
            }
            if(m.find()){
                this.browser.name = b.get("name");
                this.browser.channel = "";
                this.browser.stock = false;

                if(m.groupCount() > 0){
                    this.browser.version = new Version(m.group(1));
                    if(b.containsKey("details")){
                        try{
                            this.browser.version.details = Integer.parseInt(b.get("details"));
                        }catch(NumberFormatException ex){
                        }
                    }
                }else{
                    this.browser.version = null;
                }
            }
        }
    }

    protected void parseEngine(){
        // WebKit
        if(find("WebKit/([0-9.]*)", Pattern.CASE_INSENSITIVE)){
            this.engine.name = "WebKit";
            Matcher m = matcher("WebKit/([0-9.]*)", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.engine.version = new Version(m.group(1));
            }
        }
        if(find("Browser/AppleWebKit([0-9.]*)", Pattern.CASE_INSENSITIVE)){
            this.engine.name = "WebKit";
            Matcher m = matcher("Browser/AppleWebKit([0-9.]*)", Pattern.CASE_INSENSITIVE);
            if(m.find()){
                this.engine.version = new Version(m.group(1));
            }
        }

        // KHTML
        if(find("KHTML/([0-9.]*)")){
            this.engine.name = "KHTML";
            Matcher m = matcher("KHTML/([0-9.]*)");
            if(m.find()){
                this.engine.version = new Version(m.group(1));
            }
        }

        // Gecko
        if(contains("Gecko") && ua.toLowerCase().indexOf("like gecko") < 0){
            this.engine.name = "Gecko";
            Matcher m = matcher("; rv:([^\\)]+)\\)");
            if(m.find()){
                this.engine.version = new Version(m.group(1));
            }
        }

        // Presto
        if(find("Presto/([0-9.]*)")){
            this.engine.name = "Presto";
            Matcher m = matcher("Presto/([0-9.]*)");
            if(m.find()){
                this.engine.version = new Version(m.group(1));
            }
        }

        // Trident
        if(find("Trident/([0-9.]*)")){
            Matcher m = matcher("Trident/([0-9.]*)");
            this.engine.name = "Trident";
            if(m.find()){
                this.engine.version = new Version(m.group(1));
            }
            if(this.browser.name.equals("Internet Explorer")){
                float version = Version.parseVersion(this.engine.version);
                if(version == 6.0 && Version.parseVersion(this.browser.version) < 10.0){
                    this.browser.version.value = "10.0";
                    this.browser.mode = "compat";
                }
                if(version == 5.0 && Version.parseVersion(this.browser.version) < 9.0){
                    this.browser.version.value = "9.0";
                    this.browser.mode = "compat";
                }
                if(version == 4.0 && Version.parseVersion(this.browser.version) < 8.0){
                    this.browser.version.value = "8.0";
                    this.browser.mode = "compat";
                }
            }
            if(this.os.name.equals("Windows Phone")){
                float version = Version.parseVersion(this.engine.version);
                if(version == 5.0 && Version.parseVersion(this.os.version) < 7.5){
                    this.os.version = new Version("7.5");
                }
            }
        }
    }

    protected void clearCamouflage(){
        // Corrections
        if(this.os.name.equals("Android") && this.browser.stock){
            this.browser.hidden = true;
        }
        if(this.os.name.equals("iOS") && this.browser.name.equals("Opera Mini")){
            this.os.version = null;
        }
        if(this.browser.name.equals("Midori") && this.engine.name.equals("Webkit")){
            this.engine.name = "Webkit";
            this.engine.version = null;
        }
        if(this.device.type.equals("television") && this.browser.name.equals("Opera")){
            this.browser.name = "Opera Devices";
            if(this.engine.version != null){
                if(this.engine.version.value.equals("2.10")){
                    this.engine.version = new Version("3.2");
                }else if(this.engine.version.value.equals("2.9")){
                    this.engine.version = new Version("3.1");
                }else if(this.engine.version.value.equals("2.8")){
                    this.engine.version = new Version("3.0");
                }else if(this.engine.version.value.equals("2.7")){
                    this.engine.version = new Version("2.9");
                }else if(this.engine.version.value.equals("2.6")){
                    this.engine.version = new Version("2.8");
                }else if(this.engine.version.value.equals("2.4")){
                    this.engine.version = new Version("10.3");
                }else if(this.engine.version.value.equals("2.3")){
                    this.engine.version = new Version("10");
                }else if(this.engine.version.value.equals("2.2")){
                    this.engine.version = new Version("9.7");
                }else if(this.engine.version.value.equals("2.1")){
                    this.engine.version = new Version("9.6");
                }else{
                    this.engine.version = null;
                }
            }
            this.os.name = null;
            this.os.version = null;
        }

        // Camouflage
        if(this.detectCamouflage){
            Matcher m = matcher("Mac OS X 10_6_3; ([^;]+); [a-z]{2}-(?:[a-z]{2})\\)");
            if(m.find()){
                this.browser.name = "";
                this.browser.version = null;
                this.browser.mode = "desktop";
                
                this.os.name = "Android";
                this.os.version = null;

                this.engine.name = "Webkit";
                this.engine.version = null;

                this.device.model = m.group(1);
                this.device.type = "mobile";

                String model = cleanupModel(this.device.model);
                if(MANUFACTURER.ANDROID_MODELS.containsKey(model)){
                    this.device.manufacturer = MANUFACTURER.ANDROID_MODELS.get(model)[0];
                    this.device.model = MANUFACTURER.ANDROID_MODELS.get(model)[1];
                    if(MANUFACTURER.ANDROID_MODELS.get(model).length > 2){
                        this.device.type = MANUFACTURER.ANDROID_MODELS.get(model)[2];
                    }
                    this.device.identified = true;
                }
            }

            m = matcher("Linux Ventana; [a-z]{2}-[a-z]{2}; (.+) Build");
            if(m.find()){
                this.browser.name = "";
                this.browser.version = null;
                this.browser.mode = "desktop";

                this.os.name = "Android";
                this.os.version = null;

                this.device.model = m.group(1);
                this.device.type = "mobile";

                String model = cleanupModel(this.device.model);
                if(MANUFACTURER.ANDROID_MODELS.containsKey(model)){
                    this.device.manufacturer = MANUFACTURER.ANDROID_MODELS.get(model)[0];
                    this.device.model = MANUFACTURER.ANDROID_MODELS.get(model)[1];
                    if(MANUFACTURER.ANDROID_MODELS.get(model).length > 2){
                        this.device.type = MANUFACTURER.ANDROID_MODELS.get(model)[2];
                    }
                    this.device.identified = true;
                }
            }
        }
    }

    /**
     * correct mobile device
     */
    protected void correctDevice(){
        Matcher match = null;
        Matcher tmpMatch = null;

        if(this.device.type.equals("mobile") || this.device.type.equals("tablet")){
            // get manufacturer through key words
            match = matcher("(ZTE|Samsung|Motorola|HTC|Coolpad|Huawei|Lenovo|LG|Sony Ericsson|Oppo|TCL|Vivo|Sony|Meizu|Nokia)", Pattern.CASE_INSENSITIVE);
            if(match.find()){
                this.device.manufacturer = match.group(1);
                if(this.device.model != null && this.device.model.indexOf(match.group(1)) >= 0){
                    this.device.model = this.device.model.replace(match.group(1), "");
                }
            }
            if(find("(iPod|iPad|iPhone)", Pattern.CASE_INSENSITIVE)){
                match = matcher("(iPod|iPad|iPhone)", Pattern.CASE_INSENSITIVE);
                match.find();
                // handle Apple
                // 苹果就这3种：iPod、iPad、iPhone
                this.device.manufacturer = "Apple";
                this.device.model = match.group(1);
            }else if(find("[-\\s](Galaxy[-\\s_]nexus|Galaxy[-\\s_]\\w*[-\\s_]\\w*|Galaxy[-\\s_]\\w*|SM-\\w*|GT-\\w*|s[cgp]h-\\w*|shw-\\w*|ATIV|i9070|omnia|s7568|A3000|A3009|A5000|A5009|A7000|A7009|A8000|C101|C1116|C1158|E400|E500F|E7000|E7009|G3139D|G3502|G3502i|G3508|G3508J|G3508i|G3509|G3509i|G3558|G3559|G3568V|G3586V|G3589W|G3606|G3608|G3609|G3812|G388F|G5108|G5108Q|G5109|G5306W|G5308W|G5309W|G550|G600|G7106|G7108|G7108V|G7109|G7200|G720NO|G7508Q|G7509|G8508S|G8509V|G9006V|G9006W|G9008V|G9008W|G9009D|G9009W|G9198|G9200|G9208|G9209|G9250|G9280|I535|I679|I739|I8190N|I8262|I879|I879E|I889|I9000|I9060|I9082|I9082C|I9082i|I9100|I9100G|I9108|I9128|I9128E|I9128i|I9152|I9152P|I9158|I9158P|I9158V|I9168|I9168i|I9190|I9192|I9195|I9195I|I9200|I9208|I9220|I9228|I9260|I9268|I9300|I9300i|I9305|I9308|I9308i|I939|I939D|I939i|I9500|I9502|I9505|I9507V|I9508|I9508V|I959|J100|J110|J5008|J7008|N7100|N7102|N7105|N7108|N7108D|N719|N750|N7505|N7506V|N7508V|N7509V|N900|N9002|N9005|N9006|N9008|N9008S|N9008V|N9009|N9100|N9106W|N9108V|N9109W|N9150|N916|N9200|P709|P709E|P729|S6358|S7278|S7278U|S7562C|S7562i|S7898i|b9388)[\\s\\)]", Pattern.CASE_INSENSITIVE)){
                // handle Samsung
                // 特殊型号可能以xxx-开头 或者直接空格接型号 兼容build结尾或直接)结尾
                // Galaxy nexus才是三星 nexus是google手机
                // 三星手机类型：galaxy xxx|SM-xxx|GT-xxx|SCH-xxx|SGH-xxx|SPH-xxx|SHW-xxx  若这些均未匹配到，则启用在中关村在线爬取到的机型白名单进行判断
                match = matcher("[-\\s](Galaxy[-\\s_]nexus|Galaxy[-\\s_]\\w*[-\\s_]\\w*|Galaxy[-\\s_]\\w*|SM-\\w*|GT-\\w*|s[cgp]h-\\w*|shw-\\w*|ATIV|i9070|omnia|s7568|A3000|A3009|A5000|A5009|A7000|A7009|A8000|C101|C1116|C1158|E400|E500F|E7000|E7009|G3139D|G3502|G3502i|G3508|G3508J|G3508i|G3509|G3509i|G3558|G3559|G3568V|G3586V|G3589W|G3606|G3608|G3609|G3812|G388F|G5108|G5108Q|G5109|G5306W|G5308W|G5309W|G550|G600|G7106|G7108|G7108V|G7109|G7200|G720NO|G7508Q|G7509|G8508S|G8509V|G9006V|G9006W|G9008V|G9008W|G9009D|G9009W|G9198|G9200|G9208|G9209|G9250|G9280|I535|I679|I739|I8190N|I8262|I879|I879E|I889|I9000|I9060|I9082|I9082C|I9082i|I9100|I9100G|I9108|I9128|I9128E|I9128i|I9152|I9152P|I9158|I9158P|I9158V|I9168|I9168i|I9190|I9192|I9195|I9195I|I9200|I9208|I9220|I9228|I9260|I9268|I9300|I9300i|I9305|I9308|I9308i|I939|I939D|I939i|I9500|I9502|I9505|I9507V|I9508|I9508V|I959|J100|J110|J5008|J7008|N7100|N7102|N7105|N7108|N7108D|N719|N750|N7505|N7506V|N7508V|N7509V|N900|N9002|N9005|N9006|N9008|N9008S|N9008V|N9009|N9100|N9106W|N9108V|N9109W|N9150|N916|N9200|P709|P709E|P729|S6358|S7278|S7278U|S7562C|S7562i|S7898i|b9388)[\\s\\)]", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Samsung";
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：[A-Z]+[0-9]+[A-Z]*, 例如 G9006 G9006V 其实应该是G9006 另外三星只保留3位
                String model = match.group(1).replaceAll("(?i)Galaxy S VI", "Galaxy S6");
                model = model.replaceAll("(?i)Galaxy V", "Galaxy S5");
                model = model.replaceAll("(?i)Galaxy IV", "Galaxy S4");
                model = model.replaceAll("(?i)Galaxy s III", "Galaxy S3");
                model = model.replaceAll("(?i)Galaxy S II", "Galaxy S2");
                model = model.replaceAll("(?i)Galaxy S I", "Galaxy S1");
                model = model.replaceAll("(?i)([a-z]+[0-9]{3})[0-9]?[a-z]*", "$1");
                this.device.model = model;
            }else if(this.device.manufacturer != null && this.device.manufacturer.toLowerCase().equals("samsung") && this.device.model != null){
                // 针对三星已经匹配出的数据做处理
                String model = this.device.model.replaceAll("(?i)Galaxy S VI", "Galaxy S6");
                model = model.replaceAll("(?i)Galaxy V", "Galaxy S5");
                model = model.replaceAll("(?i)Galaxy IV", "Galaxy S4");
                model = model.replaceAll("(?i)Galaxy s III", "Galaxy S3");
                model = model.replaceAll("(?i)Galaxy S II", "Galaxy S2");
                model = model.replaceAll("(?i)Galaxy S I", "Galaxy S1");
                model = model.replaceAll("(?i)([a-z]+[0-9]{3})[0-9]?[a-z]*", "$1");
                this.device.model = model;
            }else if(find("(Huawei[-\\s_](\\w*[-_]?\\w*)|\\s(7D-\\w*|ALE-\\w*|ATH-\\w*|CHE-\\w*|CHM-\\w*|Che1-\\w*|Che2-\\w*|D2-\\w*|G616-\\w*|G620S-\\w*|G621-\\w*|G660-\\w*|G750-\\w*|GRA-\\w*|Hol-\\w*|MT2-\\w*|MT7-\\w*|PE-\\w*|PLK-\\w*|SC-\\w*|SCL-\\w*|H60-\\w*|H30-\\w*)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle Huawei
                // 兼容build结尾或直接)结尾
                // 华为机型特征：Huawei[-\s_](\w*[-_]?\w*)  或者以 7D-  ALE-  CHE-等开头
                match = matcher("(Huawei[-\\s_](\\w*[-_]?\\w*)|\\s(7D-\\w*|ALE-\\w*|ATH-\\w*|CHE-\\w*|CHM-\\w*|Che1-\\w*|Che2-\\w*|D2-\\w*|G616-\\w*|G620S-\\w*|G621-\\w*|G660-\\w*|G750-\\w*|GRA-\\w*|Hol-\\w*|MT2-\\w*|MT7-\\w*|PE-\\w*|PLK-\\w*|SC-\\w*|SCL-\\w*|H60-\\w*|H30-\\w*)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Huawei";
                if(match.groupCount() >= 2 && match.group(2) != "" && match.group(2) != null){
                    this.device.model = match.group(2);
                }else if(match.groupCount() >= 3 && match.group(3) != "" && match.group(3) != null){
                    this.device.model = match.group(3);
                }
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：xxx-[A-Z][0-9]+ 例如  H30-L01  H30-L00  H30-L20  都应该是 H30-L
                // h30-l  h30-h  h30-t 都是H30
                match = matcher("(\\w*)[-\\s_]+[a-z0-9]+", this.device.model, Pattern.CASE_INSENSITIVE);
                if(match.find()){
                    this.device.model = match.group(1);
                }
            }else if(find(";\\s(mi|m1|m2|m3|m4|hm)(\\s*\\w*)[\\s\\)]", Pattern.CASE_INSENSITIVE)){
                // handle Xiaomi
                // 兼容build结尾或直接)结尾 以及特殊的HM处理方案(build/hm2013011)
                // xiaomi手机类型: mi m1 m2 m3 hm 开头
                // hongmi有特殊判断build/hm2015011
                match = matcher(";\\s(mi|m1|m2|m3|m4|hm)(\\s*\\w*)[\\s\\)]", Pattern.CASE_INSENSITIVE);
                match.find();
                tmpMatch = matcher("(meitu|MediaPad)", Pattern.CASE_INSENSITIVE);
                if(tmpMatch.find()){ // 美图手机名字冒充小米 比如 meitu m4 mizhi
                    this.device.manufacturer = tmpMatch.group(1);
                    this.device.model = "";
                }else if(match.groupCount() >= 2 && match.group(2) != null && match.group(2).length() > 0 && !find("\\s", match.group(2))){
                    // 若匹配出的 match[2]没空格 会出现很多例如 mizi mizhi miha 但也会出现mi3 minote之类 特殊处理下
                    tmpMatch = matcher("(\\d)", match.group(2), Pattern.CASE_INSENSITIVE);
                    if(tmpMatch.find()){
                        this.device.model = match.group(1) + "-" + tmpMatch.group(1);
                    }
                }else{
                    this.device.manufacturer = "Xiaomi";
                    if(match.groupCount() >= 2 && match.group(2) != null && match.group(2).length() > 0){
                        String m = match.group(2).replaceAll("\\s", "");
                        this.device.model = (match.group(1).substring(match.group(1).length() - 2, match.group(1).length()) + "-" + m).replaceAll("(\\d)-", "MI-$1");
                    }else{
                        this.device.model = match.group(1).substring(match.group(1).length() - 2, match.group(1).length()).replaceAll("(\\d)", "MI-$1");
                    }
                    // 解决移动联通等不同发行版导致的机型不同问题
                    // 特征：mi-3c,例如mi-4LTE mi-4 其实应该是 mi-4
                    if(find("(mi|hm)(-\\d)", this.device.model, Pattern.CASE_INSENSITIVE)){
                        if(find("(mi|hm)(-\\ds)", this.device.model, Pattern.CASE_INSENSITIVE)){
                            // 看看是不是 MI-3S  MI-4S....
                            match = matcher("(mi|hm)(-\\ds)", this.device.model, Pattern.CASE_INSENSITIVE);
                            if(match.find()){
                                this.device.model = match.group(1) + match.group(2);
                            }
                        }else if(find("(mi|hm)(-\\d{2})", this.device.model, Pattern.CASE_INSENSITIVE)){
                            // 防止 MI-20150XX等滥竽充数成为MI-2
                            match = matcher("(mi|hm)(-\\d{2})", this.device.model, Pattern.CASE_INSENSITIVE);
                            if(match.find()){
                                this.device.model = match.group(1);
                            }
                        }else if(find("(mi|hm)(-\\d)[A-Z]", this.device.model, Pattern.CASE_INSENSITIVE)){
                            // 将mi-3c mi-3a mi-3w等合为mi-3
                            match = matcher("(mi|hm)(-\\d)[A-Z]", this.device.model, Pattern.CASE_INSENSITIVE);
                            if(match.find()){
                                this.device.model = match.group(1) + match.group(2);
                            }
                        }
                    }
                    // 去除 mi-4g这样的东西
                    match = matcher("(mi|hm)(-\\dg)", this.device.model, Pattern.CASE_INSENSITIVE);
                    if(match.find()){
                        this.device.model = match.group(1);
                    }
                }
            }else if(find("build/HM\\d{0,7}\\)", Pattern.CASE_INSENSITIVE)){
                this.device.manufacturer = "Xiaomi";
                this.device.model = "HM";
            }else if(this.device.manufacturer != null && this.device.manufacturer.toLowerCase().equals("xiaomi") && this.device.model != null){
                // 针对通过base库判断出数据时命名风格不同。特殊处理适配如下
                if(find("mi-one", this.device.model, Pattern.CASE_INSENSITIVE)){
                    this.device.model = "MI-1";
                }else if(find("mi-two", this.device.model, Pattern.CASE_INSENSITIVE)){
                    // mi 2
                    this.device.model = "MI-2";
                }else if(find("\\d{6}", this.device.model, Pattern.CASE_INSENSITIVE)){
                    // 20150xxx2014501
                    this.device.model = "";
                }else if(find("redmi", this.device.model, Pattern.CASE_INSENSITIVE)){
                    this.device.model = this.device.model.toUpperCase().replaceAll("(?i)redmi", "HM");
                }else if(find("(m\\d)[-\\s_](s?)", this.device.model, Pattern.CASE_INSENSITIVE)){
                    // m1 m2 m3 写法不标准 另外判断是否是 m1-s
                    match = matcher("(m\\d)[-\\s_](s?)", this.device.model, Pattern.CASE_INSENSITIVE);
                    if(match.find()){
                        this.device.model = match.group(1).replace("m", "MI-");
                    }
                }else if(find("(hm|mi)[-\\s_](\\d?)[a-rt-z]", this.device.model, Pattern.CASE_INSENSITIVE)){
                    // mi-2w  mi-3w 等格式化为mi-2  mi-3
                    match = matcher("(hm|mi)[-\\s_](\\d?)[a-rt-z]", this.device.model, Pattern.CASE_INSENSITIVE);
                    tmpMatch = matcher("(mi|hm)[-\\s_](note|pad)(\\d?s?)", this.device.model, Pattern.CASE_INSENSITIVE);
                    match.find();
                    if(tmpMatch.find()){
                        this.device.model = tmpMatch.group(1) + "-" + tmpMatch.group(2) + tmpMatch.group(3);
                    }else{
                        if(match.groupCount() > 1){
                            this.device.model = match.group(1) + "-" + match.group(2);
                        }else{
                            this.device.model = match.group(1);
                        }
                    }
                }else if(find("hm", this.device.model, Pattern.CASE_INSENSITIVE)){
                    // 处理hm
                    if(find("(hm)[-\\s_](\\d{2})", this.device.model, Pattern.CASE_INSENSITIVE)){
                        // 判断是不是 hm-201xxx充数
                        this.device.model = "HM";
                    }else if(find("(hm)[-\\s_](\\ds)", this.device.model, Pattern.CASE_INSENSITIVE)){
                        // 判断是不是 hm-2s hm-1s
                        match = matcher("(hm)[-\\s_](\\ds)", this.device.model, Pattern.CASE_INSENSITIVE);
                        match.find();
                        this.device.model = "HM-" + match.group(2);
                    }else if(find("(hm)[-\\s_](\\d)[a-z]", this.device.model, Pattern.CASE_INSENSITIVE)){
                        match = matcher("(hm)[-\\s_](\\d)[a-z]", this.device.model, Pattern.CASE_INSENSITIVE);
                        match.find();
                        this.device.model = "HM-" + match.group(2);
                    }else{
                        this.device.model = "HM";
                    }
                    if(find("hm-\\dg", this.device.model)){
                        this.device.model = "HM";
                    }
                }
            }else if(find("(vivo[-\\s_](\\w*)|\\s(E1\\w?|E3\\w?|E5\\w?|V1\\w?|V2\\w?|S11\\w?|S12\\w?|S1\\w?|S3\\w?|S6\\w?|S7\\w?|S9\\w?|X1\\w?|X3\\w?|X520\\w?|X5\\w?|X5Max|X5Max+|X5Pro|X5SL|X710F|X710L|Xplay|Xshot|Xpaly3S|Y11\\w?|Y11i\\w?|Y11i\\w?|Y13\\w?|Y15\\w?|Y17\\w?|Y18\\w?|Y19\\w?|Y1\\w?|Y20\\w?|Y22\\w?|Y22i\\w?|Y23\\w?|Y27\\w?|Y28\\w?|Y29\\w?|Y33\\w?|Y37\\w?|Y3\\w?|Y613\\w?|Y622\\w?|Y627\\w?|Y913\\w?|Y923\\w?|Y927\\w?|Y928\\w?|Y929\\w?|Y937\\w?)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle Vivo
                // 兼容build结尾或直接)结尾
                // vivo机型特征: Vivo[-\s_](\w*)  或者以 E1  S11t  S7t 等开头
                match = matcher("(vivo[-\\s_](\\w*)|\\s(E1\\w?|E3\\w?|E5\\w?|V1\\w?|V2\\w?|S11\\w?|S12\\w?|S1\\w?|S3\\w?|S6\\w?|S7\\w?|S9\\w?|X1\\w?|X3\\w?|X520\\w?|X5\\w?|X5Max|X5Max+|X5Pro|X5SL|X710F|X710L|Xplay|Xshot|Xpaly3S|Y11\\w?|Y11i\\w?|Y11i\\w?|Y13\\w?|Y15\\w?|Y17\\w?|Y18\\w?|Y19\\w?|Y1\\w?|Y20\\w?|Y22\\w?|Y22i\\w?|Y23\\w?|Y27\\w?|Y28\\w?|Y29\\w?|Y33\\w?|Y37\\w?|Y3\\w?|Y613\\w?|Y622\\w?|Y627\\w?|Y913\\w?|Y923\\w?|Y927\\w?|Y928\\w?|Y929\\w?|Y937\\w?)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Vivo";
                // 首先剔除 viv-  vivo-  bbg- 等打头的内容
                this.device.model = match.group(1).replaceAll("(?i)(viv[-\\s_]|vivo[-\\s_]|bbg[-\\s_])", "");
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：[A-Z][0-9]+[A-Z] 例如  X5F X5L X5M X5iL 都应该是 X5
                match = matcher("([a-z]+[0-9]+)i?[a-z]?[-\\s_]?", this.device.model, Pattern.CASE_INSENSITIVE);
                if(match.find()){
                    this.device.model = match.group(1);
                }
            }else if(find("(Oppo[-\\s_](\\w*)|\\s(1100|1105|1107|3000|3005|3007|6607|A100|A103|A105|A105K|A109|A109K|A11|A113|A115|A115K|A121|A125|A127|A129|A201|A203|A209|A31|A31c|A31t|A31u|A51kc|A520|A613|A615|A617|E21W|Find|Mirror|N5110|N5117|N5207|N5209|R2010|R2017|R6007|R7005|R7007|R7c|R7t|R8000|R8007|R801|R805|R807|R809T|R8107|R8109|R811|R811W|R813T|R815T|R815W|R817|R819T|R8200|R8205|R8207|R821T|R823T|R827T|R830|R830S|R831S|R831T|R833T|R850|Real|T703|U2S|U521|U525|U529|U539|U701|U701T|U705T|U705W|X9000|X9007|X903|X905|X9070|X9077|X909|Z101|R829T)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle Oppo
                match = matcher("(Oppo[-\\s_](\\w*)|\\s(1100|1105|1107|3000|3005|3007|6607|A100|A103|A105|A105K|A109|A109K|A11|A113|A115|A115K|A121|A125|A127|A129|A201|A203|A209|A31|A31c|A31t|A31u|A51kc|A520|A613|A615|A617|E21W|Find|Mirror|N5110|N5117|N5207|N5209|R2010|R2017|R6007|R7005|R7007|R7c|R7t|R8000|R8007|R801|R805|R807|R809T|R8107|R8109|R811|R811W|R813T|R815T|R815W|R817|R819T|R8200|R8205|R8207|R821T|R823T|R827T|R830|R830S|R831S|R831T|R833T|R850|Real|T703|U2S|U521|U525|U529|U539|U701|U701T|U705T|U705W|X9000|X9007|X903|X905|X9070|X9077|X909|Z101|R829T)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Oppo";
                if(match.groupCount() >= 2 && match.group(2) != "" && match.group(2) != null){
                    this.device.model = match.group(2);
                }else if(match.groupCount() >= 3 && match.group(3) != "" && match.group(3) != null){
                    this.device.model = match.group(3);
                }
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：[A-Z][0-9]+[A-Z] 例如  A31c A31s 都应该是 A31
                // 对 Plus 做特殊处理
                if(find("([a-z]+[0-9]+)-?(plus)", this.device.model, Pattern.CASE_INSENSITIVE)){
                    Matcher m = matcher("([a-z]+[0-9]+)-?(plus)", this.device.model, Pattern.CASE_INSENSITIVE);
                    m.find();
                    this.device.model = m.group(1) + "-" + m.group(2);
                }else if(find("(\\w*-?[a-z]+[0-9]+)", this.device.model, Pattern.CASE_INSENSITIVE)){
                    Matcher m = matcher("(\\w*-?[a-z]+[0-9]+)", this.device.model, Pattern.CASE_INSENSITIVE);
                    m.find();
                    this.device.model = m.group(1);
                }
            }else if(this.device.manufacturer != null && this.device.manufacturer.toLowerCase().equals("oppo") && this.device.model != null){
                // 针对base库的数据做数据格式化处理
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：[A-Z][0-9]+[A-Z] 例如  A31c A31s 都应该是 A31
                // 对 Plus 做特殊处理
                if(find("([a-z]+[0-9]+)-?(plus)", this.device.model, Pattern.CASE_INSENSITIVE)){
                    match = matcher("([a-z]+[0-9]+)-?(plus)", this.device.model, Pattern.CASE_INSENSITIVE);
                    match.find();
                    this.device.model = match.group(1) + "-" + match.group(2);
                }else if(find("(\\w*-?[a-z]+[0-9]+)", this.device.model, Pattern.CASE_INSENSITIVE)){
                    match = matcher("(\\w*-?[a-z]+[0-9]+)", this.device.model, Pattern.CASE_INSENSITIVE);
                    match.find();
                    this.device.model = match.group(1);
                }
            }else if(find("(Lenovo[-\\s_](\\w*[-_]?\\w*)|\\s(A3580|A3860|A5500|A5600|A5860|A7600|A806|A800|A808T|A808T-I|A936|A938t|A788t|K30-E|K30-T|K30-W|K50-T3s|K50-T5|K80M|K910|K910e|K920|S90-e|S90-t|S90-u|S968T|X2-CU|X2-TO|Z90-3|Z90-7)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle Lenovo
                // 兼容build结尾或直接)结尾 兼容Lenovo-xxx/xxx以及Leveno xxx build等
                match = matcher("(Lenovo[-\\s_](\\w*[-_]?\\w*)|\\s(A3580|A3860|A5500|A5600|A5860|A7600|A806|A800|A808T|A808T-I|A936|A938t|A788t|K30-E|K30-T|K30-W|K50-T3s|K50-T5|K80M|K910|K910e|K920|S90-e|S90-t|S90-u|S968T|X2-CU|X2-TO|Z90-3|Z90-7)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Lenovo";
                if(match.groupCount() >= 2 && match.group(2) != "" && match.group(2) != null){
                    this.device.model = match.group(2);
                }else if(match.groupCount() >= 3 && match.group(3) != "" && match.group(3) != null){
                    this.device.model = match.group(3);
                }
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：[A-Z][0-9]+[A-Z] 例如  A360t A360 都应该是 A360
                match = matcher("([a-z]+[0-9]+)", this.device.model, Pattern.CASE_INSENSITIVE);
                if(match.find()){
                    this.device.model = match.group(1);
                }
            }else if(find("(Coolpad[-\\s_](\\w*)|\\s(7295C|7298A|7620L|8908|8085|8970L|9190L|Y80D)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle coolpad
                match = matcher("(Coolpad[-\\s_](\\w*)|\\s(7295C|7298A|7620L|8908|8085|8970L|9190L|Y80D)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Coolpad";
                if(match.groupCount() >= 2 && match.group(2) != "" && match.group(2) != null){
                    this.device.model = match.group(2);
                }else if(match.groupCount() >= 2 && match.group(3) != "" && match.group(3) != null){
                    this.device.model = match.group(3);
                }
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：[A-Z][0-9]+[A-Z] 例如  8297-t01 8297-c01 8297w 都应该是 8297
                match = matcher("([a-z]?[0-9]+)", this.device.model, Pattern.CASE_INSENSITIVE);
                if(match.find()){
                    this.device.model = match.group(1);
                }
            }else if(this.device.manufacturer != null && this.device.manufacturer.toLowerCase().equals("coolpad") && this.device.model != null){
                // base 库适配
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：[A-Z][0-9]+[A-Z] 例如  8297-t01 8297-c01 8297w 都应该是 8297
                match = matcher("([a-z]?[0-9]+)", this.device.model, Pattern.CASE_INSENSITIVE);
                if(match.find()){
                    this.device.model = match.group(1);
                }
            }else if(find("\\s(mx\\d*\\w*|mz-(\\w*))\\s(\\w*)\\s*\\w*\\s*build", Pattern.CASE_INSENSITIVE)){
                // handle meizu
                match = matcher("\\s(mx\\d*\\w*|mz-(\\w*))\\s(\\w*)\\s*\\w*\\s*build", Pattern.CASE_INSENSITIVE);
                this.device.manufacturer = "Meizu";
                match.find();
                String model = "";
                if(match.groupCount() > 1 && match.group(2) != null && !match.group(2).isEmpty()){
                    model = match.group(2);
                }else{
                    model = match.group(1);
                }
                if(match.groupCount() > 2 && match.group(3) != null && !match.group(3).isEmpty()){
                    this.device.model = model + "-" + match.group(3);
                }else{
                    this.device.model = model + "";
                }
            }else if(find("(M463C|M35)\\d", Pattern.CASE_INSENSITIVE)){
                match = matcher("(M463C|M35)\\d", Pattern.CASE_INSENSITIVE);
                this.device.manufacturer = "Meizu";
                match.find();
                this.device.model = match.group(1);
            }else if(find("(Htc[-_\\s](\\w*)|\\s(601e|606w|608t|609d|610t|6160|619d|620G|626d|626s|626t|626w|709d|801e|802d|802t|802w|809D|816d|816e|816t|816v|816w|826d|826s|826t|826w|828w|901e|919d|A310e|A50AML|A510e|A620d|A620e|A620t|A810e|A9191|Aero|C620d|C620e|C620t|D316d|D516d|D516t|D516w|D820mt|D820mu|D820t|D820ts|D820u|D820us|E9pt|E9pw|E9sw|E9t|HD7S|M8Et|M8Sd|M8St|M8Sw|M8d|M8e|M8s|M8si|M8t|M8w|M9W|M9ew|Phablet|S510b|S510e|S610d|S710d|S710e|S720e|S720t|T327t|T328d|T328t|T328w|T329d|T329t|T329w|T528d|T528t|T528w|T8698|WF5w|X315e|X710e|X715e|X720d|X920e|Z560e|Z710e|Z710t|Z715e)[\\s\\)])")){
                match = matcher("(Htc[-_\\s](\\w*)|\\s(601e|606w|608t|609d|610t|6160|619d|620G|626d|626s|626t|626w|709d|801e|802d|802t|802w|809D|816d|816e|816t|816v|816w|826d|826s|826t|826w|828w|901e|919d|A310e|A50AML|A510e|A620d|A620e|A620t|A810e|A9191|Aero|C620d|C620e|C620t|D316d|D516d|D516t|D516w|D820mt|D820mu|D820t|D820ts|D820u|D820us|E9pt|E9pw|E9sw|E9t|HD7S|M8Et|M8Sd|M8St|M8Sw|M8d|M8e|M8s|M8si|M8t|M8w|M9W|M9ew|Phablet|S510b|S510e|S610d|S710d|S710e|S720e|S720t|T327t|T328d|T328t|T328w|T329d|T329t|T329w|T528d|T528t|T528w|T8698|WF5w|X315e|X710e|X715e|X720d|X920e|Z560e|Z710e|Z710t|Z715e)[\\s\\)])");
                match.find();
                this.device.manufacturer = "Htc";
                this.device.model = match.group(1);
            }else if(find("(Gionee[-\\s_](\\w*)|\\s(GN\\d+\\w*)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle Gionee
                this.device.manufacturer = "GinDream";
                match = matcher("(Gionee[-\\s_](\\w*)|\\s(GN\\d+\\w*)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                if(match.groupCount() > 1 && match.group(2) != null && !match.group(2).isEmpty()){
                    this.device.model = match.group(2);
                }else if(match.groupCount() > 2){
                    this.device.model = match.group(3);
                }
            }else if(find("(LG[-_](\\w*)|\\s(D728|D729|D802|D855|D856|D857|D858|D859|E985T|F100L|F460|H778|H818|H819|P895|VW820)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle LG
                match = matcher("(LG[-_](\\w*)|\\s(D728|D729|D802|D855|D856|D857|D858|D859|E985T|F100L|F460|H778|H818|H819|P895|VW820)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Lg";
                if(match.groupCount() > 1){
                    this.device.model = match.group(2);
                }else if(match.groupCount() > 2){
                    this.device.model = match.group(3);
                }
            }else if(find("(Tcl[-\\s_](\\w*)|\\s(H916T|P588L|P618L|P620M|P728M)[\\s\\)])")){
                match = matcher("(Tcl[-\\s_](\\w*)|\\s(H916T|P588L|P618L|P620M|P728M)[\\s\\)])");
                match.find();
                this.device.manufacturer = "Tcl";
                this.device.model = match.group(1);
            }else if(find("(V9180|N918)", Pattern.CASE_INSENSITIVE)){
                // ZTE
                match = matcher("(V9180|N918)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Zte";
                this.device.model = match.group(1);
            }else if(this.device.manufacturer != null && this.device.manufacturer.toLowerCase().equals("zte") && this.device.model != null){
                // base 库适配
                // 解决移动联通等不同发行版导致的机型不同问题
                // 特征：[A-Z][0-9]+[A-Z] 例如  Q505T Q505u 都应该是 Q505
                match = matcher("([a-z]?[0-9]+)", this.device.model, Pattern.CASE_INSENSITIVE);
                if(match.find()){
                    this.device.model = match.group(1);
                }
            }else if(find("(UIMI\\w*|umi\\w*)[-\\s_](\\w*)\\s*\\w*\\s*build", Pattern.CASE_INSENSITIVE)){
                // UIMI
                match = matcher("(UIMI\\w*|umi\\w*)[-\\s_](\\w*)\\s*\\w*\\s*build", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Uimi";
                if(match.groupCount() > 1){
                    this.device.model = match.group(1) + "-" + match.group(2);
                }else{
                    this.device.model = match.group(1) + "";
                }
            }else if(find("eton[-\\s_](\\w*)", Pattern.CASE_INSENSITIVE)){
                // eton
                match = matcher("eton[-\\s_](\\w*)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Eton";
                this.device.model = match.group(1);
            }else if(find("(SM705|SM701|YQ601|YQ603)", Pattern.CASE_INSENSITIVE)){
                // Smartisan
                match = matcher("(SM705|SM701|YQ601|YQ603)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Smartisan";
                String model = match.group(1);
                if(model.equals("SM701") || model.equals("SM705")){
                    this.device.model = "T1";
                }else if(model.equals("YQ601") || model.equals("YQ603")){
                    this.device.model = "U1";
                }else{
                    this.device.model = model;
                }
            }else if(find("(Asus[-\\s_](\\w*)|\\s(A500CG|A500KL|A501CG|A600CG|PF400CG|PF500KL|T001|X002|X003|ZC500TG|ZE550ML|ZE551ML)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle Asus
                match = matcher("(Asus[-\\s_](\\w*)|\\s(A500CG|A500KL|A501CG|A600CG|PF400CG|PF500KL|T001|X002|X003|ZC500TG|ZE550ML|ZE551ML)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Asus";
                if(match.groupCount() > 1){
                    this.device.model = match.group(2);
                }else if(match.groupCount() > 2){
                    this.device.model = match.group(3);
                }
            }else if(find("(Nubia[-_\\s](\\w*)|(NX501|NX505J|NX506J|NX507J|NX503A|nx\\d+\\w*)[\\s\\)])", Pattern.CASE_INSENSITIVE)){
                // handle nubia
                match = matcher("(Nubia[-_\\s](\\w*)|(NX501|NX505J|NX506J|NX507J|NX503A|nx\\d+\\w*)[\\s\\)])", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Nubia";
                if(match.groupCount() > 1 && match.group(2) != null && !match.group(2).isEmpty()){
                    this.device.model = match.group(2);
                }else if(match.groupCount() > 2){
                    this.device.model = match.group(3);
                }
            }else if(find("(HT-\\w*)|Haier[-\\s_](\\w*-?\\w*)", Pattern.CASE_INSENSITIVE)){
                // handle haier
                match = matcher("(HT-\\w*)|Haier[-\\s_](\\w*-?\\w*)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Haier";
                if(match.groupCount() > 0){
                    this.device.model = match.group(1);
                }else if(match.groupCount() > 1){
                    this.device.model = match.group(2);
                }
            }else if(find("K-Touch[-\\s_](tou\\s?ch\\s?(\\d)|\\w*)", Pattern.CASE_INSENSITIVE)){
                // tianyu
                match = matcher("K-Touch[-\\s_](tou\\s?ch\\s?(\\d)|\\w*)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "K-Touch";
                if(match.groupCount() > 1 && match.group(2) != null && !match.group(2).isEmpty()){
                    this.device.model = "Ktouch" + match.group(2);
                }else{
                    this.device.model = match.group(1);
                }
            }else if(find("Doov[-\\s_](\\w*)", Pattern.CASE_INSENSITIVE)){
                // DOOV
                match = matcher("Doov[-\\s_](\\w*)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Doov";
                this.device.model = match.group(1);
            }else if(find("koobee", Pattern.CASE_INSENSITIVE)){
                // coobee
                this.device.manufacturer = "koobee";
            }else if(find("C69", Pattern.CASE_INSENSITIVE)){
                // Sony
                this.device.manufacturer = "Sony";
            }else if(find("N787|N818S", Pattern.CASE_INSENSITIVE)){
                // haojixing
                this.device.manufacturer = "Haojixing";
            }else if(find("(hs-|Hisense[-\\s_])(\\w*)", Pattern.CASE_INSENSITIVE)){
                // haojixing
                match = matcher("(hs-|Hisense[-\\s_])(\\w*)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.device.manufacturer = "Hisense";
                this.device.model = match.group(2);
            }

            // format the style of manufacturer
            if(this.device.manufacturer != null && this.device.manufacturer.length() > 0){
                String manufacturer = this.device.manufacturer;
                this.device.manufacturer = Character.toUpperCase(manufacturer.charAt(0)) + manufacturer.substring(1).toLowerCase();
            }

            // format the style of model
            if(this.device.model != null && this.device.model != ""){
                /*
                model = re.sub(r'-+|_+|\s+', ' ', uaData.device['model'].upper())
                model = re.search(r'\s*(\w*\s*\w*)', model).group(1)
                model = re.sub(r'\s+', '-', model)
                uaData.device['model'] = model
                */
                String model = this.device.model.toUpperCase().replaceAll("-+|_+|\\s+", " ");
                Matcher m = matcher("\\s*(\\w*\\s*\\w*)", model);
                if(m.find()){
                    model = m.group(1);
                }
                model = model.replaceAll("\\s+", "-");

                // 针对三星、华为做去重的特殊处理
                Map<String, String> modelDict = new HashMap<String, String>();
                if(this.device.manufacturer.equals("Samsung")){
                    modelDict.put("SCH-I95", "GT-I950");
                    modelDict.put("SCH-I93", "GT-I930");
                    modelDict.put("SCH-I86", "GT-I855");
                    modelDict.put("SCH-N71", "GT-N710");
                    modelDict.put("SCH-I73", "GT-S789");
                    modelDict.put("SCH-P70", "GT-I915");
                    if(modelDict.containsKey(model)){
                        model = modelDict.get(model);
                    }
                }else if(this.device.manufacturer.equals("Huawei")){
                    modelDict.put("CHE1", "CHE");
                    modelDict.put("CHE2", "CHE");
                    modelDict.put("G620S", "G621");
                    modelDict.put("C8817D", "G621");
                    if(modelDict.containsKey(model)){
                        model = modelDict.get(model);
                    }
                }
                this.device.model = model;
            }

            // 针对xiaomi 的部分数据没有格式化成功，格式化1次
            if(this.device.manufacturer != null && this.device.manufacturer.equals("Xiaomi")){
                if(find("(hm|mi)-(note)", this.device.model, Pattern.CASE_INSENSITIVE)){
                    match = matcher("(hm|mi)-(note)", this.device.model, Pattern.CASE_INSENSITIVE);
                    match.find();
                    this.device.model = match.group(1) + "-" + match.group(2);
                }else if(find("(hm|mi)-(\\ds?)", this.device.model, Pattern.CASE_INSENSITIVE)){
                    match = matcher("(hm|mi)-(\\ds?)", this.device.model, Pattern.CASE_INSENSITIVE);
                    match.find();
                    this.device.model = match.group(1) + "-" + match.group(2);
                }else if(find("(hm|mi)-(\\d)[a-rt-z]", this.device.model, Pattern.CASE_INSENSITIVE)){
                    match = matcher("(hm|mi)-(\\d)[a-rt-z]", this.device.model, Pattern.CASE_INSENSITIVE);
                    match.find();
                    this.device.model = match.group(1) + "-" + match.group(2);
                }
            }
        }
    }

    /**
     * handle browser
     */
    protected void correctBrowser(){
        Matcher match = null;
        Matcher tmpMatch = null;

        if(this.device.type.equals("desktop")){
            if(find("360se(?:[ /]([\\w.]+))?", Pattern.CASE_INSENSITIVE)){ // 360 security Explorer
                match = matcher("360se(?:[ /]([\\w.]+))?", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "360 security Explorer";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(find("the world(?:[ /]([\\w.]+))?", Pattern.CASE_INSENSITIVE)){ // the world
                match = matcher("the world(?:[ /]([\\w.]+))?", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "the world";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(find("tencenttraveler ([\\w.]+)", Pattern.CASE_INSENSITIVE)){ // tencenttraveler
                match = matcher("tencenttraveler ([\\w.]+)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "tencenttraveler";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }
        }else if(this.device.type.equals("mobile") || this.device.type.equals("tablet")){
            if(find("BaiduHD\\s+([\\w.]+)", Pattern.CASE_INSENSITIVE)){ // BaiduHD
                match = matcher("BaiduHD\\s+([\\w.]+)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "BaiduHD";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(find("360.s*aphone\\s*browser\\s*\\(version\\s*([\\w.]+)\\)", Pattern.CASE_INSENSITIVE)){ // 360 Browser
                match = matcher("360.s*aphone\\s*browser\\s*\\(version\\s*([\\w.]+)\\)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "360 Browser";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(find("flyflow/([\\w.]+)", Pattern.CASE_INSENSITIVE)){ // Baidu Browser
                match = matcher("flyflow/([\\w.]+)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "Baidu Browser";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(find("baiduhd ([\\w.]+)", Pattern.CASE_INSENSITIVE)){ // Baidu HD
                match = matcher("baiduhd ([\\w.]+)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "Baidu HD";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(find("LieBaoFast/([\\w.]+)", Pattern.CASE_INSENSITIVE)){ // LieBaoFast
                match = matcher("LieBaoFast/([\\w.]+)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "LieBao Fast";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(find("LieBao/([\\w.]+)", Pattern.CASE_INSENSITIVE)){ // LieBao
                match = matcher("LieBao/([\\w.]+)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "LieBao";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(this.os.name.equals("Android") && find("safari", Pattern.CASE_INSENSITIVE) && find("version/([0-9\\.]+)", Pattern.CASE_INSENSITIVE)){ // Android Google Browser
                match = matcher("version/([0-9\\.]+)", Pattern.CASE_INSENSITIVE);
                match.find();
                this.browser.name = "Google Browser";
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }else if(find("(ipad|iphone).* applewebkit/.* mobile", Pattern.CASE_INSENSITIVE)){
                // 'Mozilla/5.0 (iPad; CPU OS 5_1_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B206' belongs to Safari
                this.browser.name = "Safari";
            }
        }

        if(find("baiduboxapp", Pattern.CASE_INSENSITIVE)){
            this.browser.name = "百度框";
        }else if(find("BaiduLightAppRuntime", Pattern.CASE_INSENSITIVE)){
            this.browser.name = "轻应用runtime";
        }else if(find("Weibo", Pattern.CASE_INSENSITIVE)){
            this.browser.name = "微博";
        }else if(find("MQQ", Pattern.CASE_INSENSITIVE)){
            this.browser.name = "手机QQ";
        }else if(find("hao123", Pattern.CASE_INSENSITIVE)){
            this.browser.name = "hao123";
        }

        match = matcher("MicroMessenger/([\\w.]+)", Pattern.CASE_INSENSITIVE);
        if(match.find()){
            this.browser.name = "微信";
            String tmpVersion = match.group(1).replaceAll("_", ".");
            tmpMatch = matcher("(\\d+\\.\\d+\\.\\d+\\.\\d+)", tmpVersion);
            if(tmpMatch.find()){
                tmpVersion = tmpMatch.group(1);
            }
            this.browser.version = new Version(tmpVersion);
            this.browser.version.original = tmpVersion;
        }

        match = matcher("UCBrowser/([\\w.]+)", Pattern.CASE_INSENSITIVE);
        if(match.find()){
            this.browser.name = "UC Browser";
            this.browser.version = new Version(match.group(1));
            this.browser.version.original = match.group(1);
        }

        if(find("OPR/([\\w.]+)", Pattern.CASE_INSENSITIVE)){
            match = matcher("OPR/([\\w.]+)", Pattern.CASE_INSENSITIVE);
            match.find();
            this.browser.name = "Opera";
            this.browser.version = new Version(match.group(1));
            this.browser.version.original = match.group(1);
        }else if(find("Trident/7", Pattern.CASE_INSENSITIVE) && find("rv:11", Pattern.CASE_INSENSITIVE)){ // IE 11
            this.browser.name = "Internet Explorer";
            this.browser.version = new Version("11");
            this.browser.version.original = "11";
        }else if(find("Edge/12", Pattern.CASE_INSENSITIVE) && find("Windows Phone|Windows NT", Pattern.CASE_INSENSITIVE)){ // Microsoft Edge
            this.browser.name = "Microsoft Edge";
            this.browser.version = new Version("12");
            this.browser.version.original = "12";
        }else if(find("miuibrowser/([\\w.]+)", Pattern.CASE_INSENSITIVE)){ // miui browser
            match = matcher("miuibrowser/([\\w.]+)", Pattern.CASE_INSENSITIVE);
            match.find();
            this.browser.name = "miui browser";
            this.browser.version = new Version(match.group(1));
            this.browser.version.original = match.group(1);
        }

        if(this.browser.name == null || this.browser.name == ""){
            if(find("Safari/([\\w.]+)", Pattern.CASE_INSENSITIVE) && find("Version", Pattern.CASE_INSENSITIVE)){
                this.browser.name = "Safari";
            }
        }


        if(this.browser.name != null && this.browser.version == null){
            match = matcher("Version/([\\w.]+)", Pattern.CASE_INSENSITIVE);
            if(match.find()){
                this.browser.version = new Version(match.group(1));
                this.browser.version.original = match.group(1);
            }
        }
    }

    /**
     * handle os
     */
    protected void correctOS(){
        Matcher match = null;
        Matcher tmpMatch = null;

        if(this.os.name.equals("Windows") || find("Windows", Pattern.CASE_INSENSITIVE)){
            this.os.name = "Windows";
            if(find("NT 6.3", Pattern.CASE_INSENSITIVE)){
                this.os.version = new Version("8.1");
                this.os.version.alias = "8.1";
                this.os.version.original = "8.1";
            }else if(find("NT 6.4", Pattern.CASE_INSENSITIVE) && find("NT 10.0", Pattern.CASE_INSENSITIVE)){
                this.os.version = new Version("10");
                this.os.version.alias = "10";
                this.os.version.original = "10";
            }
        }else if(this.os.name.equals("Mac OS X")){
            match = matcher("Mac OS X[\\s\\_\\-/](\\d+[\\.\\-\\_]\\d+[\\.\\-\\_]?\\d*)", Pattern.CASE_INSENSITIVE);
            if(match.find()){
                this.os.version = new Version(match.group(1).replace("_", "."));
                this.os.version.original = match.group(1).replace("_", ".");
                this.os.version.alias = match.group(1).replace("_", ".");
            }else{
                this.os.version = new Version("");
                this.os.version.original = "";
                this.os.version.alias = "";
            }
        }else if(find("Android", this.os.name, Pattern.CASE_INSENSITIVE)){
            match = matcher("Android[\\s\\_\\-/i686]?[\\s\\_\\-/](\\d+[\\.\\-\\_]\\d+[\\.\\-\\_]?\\d*)", Pattern.CASE_INSENSITIVE);
            if(match.find()){
                this.os.version = new Version(match.group(1));
                this.os.version.alias = match.group(1);
                this.os.version.original = match.group(1);
            }
        }
    }

    protected void parse(){
        parseOSAndDevice();
        parseBrowser();
        parseEngine();
        clearCamouflage();
    }

    protected void correct(){
        correctDevice();
        correctBrowser();
        correctOS();
    }

    public void reset(){
        this.ua = "";
        this.os = new OS("");
        this.engine = new Engine("");
        this.device = new Device("");
        this.browser = new Browser("");
    }

    public UserAgent detect(){
        parse();
        correct();
        UserAgent userAgent = new UserAgent();
        userAgent.setOS(this.os);
        userAgent.setEngine(this.engine);
        userAgent.setBrowser(this.browser);
        userAgent.setDevice(this.device);
        return userAgent;
    }
}
