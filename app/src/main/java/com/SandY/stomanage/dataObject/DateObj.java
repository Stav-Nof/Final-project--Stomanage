package com.SandY.stomanage.dataObject;

import java.io.Serializable;

public class DateObj implements Serializable {
    int _day;
    int _month;
    int _year;

    public DateObj() {
    }

    public DateObj(int _day, int _month, int _year) {
        this._day = _day;
        this._month = _month;
        this._year = _year;
    }

    @Override
    public String toString() {
        return _day + "-" + _month + "-" + _year;
    }

    public int get_day() {
        return _day;
    }

    public void set_day(int _day) {
        this._day = _day;
    }

    public int get_month() {
        return _month;
    }

    public void set_month(int _month) {
        this._month = _month;
    }

    public int get_year() {
        return _year;
    }

    public void set_year(int _year) {
        this._year = _year;
    }
}
