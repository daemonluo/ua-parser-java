package com.daemon.ua;

class OS {
    String name;
    Version version = null;

    public OS(String name) {
        this.name = name;
        //this.version = new Version("");
    }

    @Override
    public String toString(){
        return "(" + this.name + ", " + this.version.value + ")";
    }
}
