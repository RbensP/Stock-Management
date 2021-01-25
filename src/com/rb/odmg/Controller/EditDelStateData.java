package com.rb.odmg.Controller;

import java.sql.Date;

public class EditDelStateData {
    private String state;
    private java.sql.Date date;

    public EditDelStateData(String state, Date date) {
        this.state = state;
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
