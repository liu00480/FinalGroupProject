package com.example.covid19;

public class Province {

    private long id;
    private String country;
    private String province;
    private String casenumber;
    private String date;

    public Province() {
    }
    public Province(String province, String casenumber){
        this.province=province;
        this.casenumber=casenumber;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCase() { return casenumber; }

    public void setCase(String cases) { this.casenumber = cases; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

