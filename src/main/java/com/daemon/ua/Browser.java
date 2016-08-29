package com.daemon.ua;

class Browser {
    String name;
    Version version = null;
    Boolean stock = true;
    Boolean hidden = false;
    String channel = "";
    String mode = "";

    public Browser(String name) {
        this.name = name;
    }
}
