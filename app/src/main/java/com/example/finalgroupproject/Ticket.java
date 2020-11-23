package com.example.finalgroupproject;

public class Ticket {

    String eventName = "";
    String startDate = "";
    String priceRange = "";
    String url = "";
    //String image = "";

    public Ticket(String eventName, String  startDate, String priceRange, String url) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.priceRange = priceRange;
        this.url = url;
    }

    public String getEventName() {return eventName;}
    public String getStartDate() {return startDate;}
    public String getPriceRange() {return priceRange;}
    public String getUrl() {return url;}
}
