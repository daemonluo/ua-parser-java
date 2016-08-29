package com.daemon.ua;

import java.util.Arrays;

class Version {
    String value;
    String original = "";
    String alias = "";
    String type = "";
    int details;

    public Version(String value) {
        this(value, value);
    }

    public Version(String value, String alias){
        this.value = value;
        this.alias = alias;
    }

    static protected float parseVersion(String version){
        String[] components = version.split(".");
        if(components.length == 0){
            return 0.0f;
        }
        String major = components[0];
        if(components.length == 1){
            return Float.valueOf(major);
        }
        String pecision = String.join("", Arrays.copyOfRange(components, 1, components.length));
        version = major + "." + pecision;
        return Float.valueOf(version);
    }

    static protected float parseVersion(Version version){
        if(version == null){
            return 0.0f;
        }
        return parseVersion(version.value);
    }
}
