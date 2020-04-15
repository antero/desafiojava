package com.concrete.desafiojava.model;

import javax.persistence.Entity;

@Entity
public class PhoneNumber {
    private String number;
    private String ddd;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
}
