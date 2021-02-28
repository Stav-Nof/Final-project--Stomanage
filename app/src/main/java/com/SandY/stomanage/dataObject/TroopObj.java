package com.SandY.stomanage.dataObject;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public class TroopObj implements Serializable {

    String _nsme;
    File _pic;
    HashMap<String, String> _regiments;

    

    public String get_nsme() {
        return _nsme;
    }

    public void set_nsme(String _nsme) {
        this._nsme = _nsme;
    }

    public File get_pic() {
        return _pic;
    }

    public void set_pic(File _pic) {
        this._pic = _pic;
    }

    public HashMap<String, String> get_regiments() {
        return _regiments;
    }

    public void set_regiments(HashMap<String, String> _regiments) {
        this._regiments = _regiments;
    }
}
