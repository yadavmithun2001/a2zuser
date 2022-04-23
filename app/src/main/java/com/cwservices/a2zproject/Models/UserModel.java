package com.cwservices.a2zproject.Models;

public class UserModel {
    String name,houseno,Floorno,Streetno,Locality,Wardno,Routeno,Pincode,Mobilenumber,access;

    public UserModel(String name, String houseno, String floorno, String streetno, String locality, String wardno, String routeno, String pincode, String mobilenumber, String access) {
        this.name = name;
        this.houseno = houseno;
        this.Floorno = floorno;
        this.Streetno = streetno;
        this.Locality = locality;
        this.Wardno = wardno;
        this.Routeno = routeno;
        this.Pincode = pincode;
        this.Mobilenumber = mobilenumber;
        this.access = access;
    }
    public UserModel(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(String houseno) {
        this.houseno = houseno;
    }

    public String getFloorno() {
        return Floorno;
    }

    public void setFloorno(String floorno) {
        Floorno = floorno;
    }

    public String getStreetno() {
        return Streetno;
    }

    public void setStreetno(String streetno) {
        Streetno = streetno;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getWardno() {
        return Wardno;
    }

    public void setWardno(String wardno) {
        Wardno = wardno;
    }

    public String getRouteno() {
        return Routeno;
    }

    public void setRouteno(String routeno) {
        Routeno = routeno;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getMobilenumber() {
        return Mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        Mobilenumber = mobilenumber;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
