package com.daemon.ua;

class Device {
    String name;
    String manufacturer = "";
    String model = "";
    String type = "";
    Boolean identified = false;

    public Device(String name) {
        this.name = name;
    }
}
