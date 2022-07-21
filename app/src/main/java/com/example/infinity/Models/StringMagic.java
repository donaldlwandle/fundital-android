package com.example.infinity.Models;

public class StringMagic {
    private String stringUpdate ;

    public StringMagic(String stringUpdate) {
        this.stringUpdate = stringUpdate;
    }
    public StringMagic() {

    }

    public String getStringUpdate() {
        return stringUpdate;
    }

    public void setStringUpdate(String stringUpdate) {
        this.stringUpdate = stringUpdate;
    }

    @Override
    public String toString() {
        return "StringMagic{" +
                "stringUpdate='" + stringUpdate + '\'' +
                '}';
    }
}
