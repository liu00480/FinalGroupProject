package com.example.finalgroupproject;

public class Ticket {

    String eventName = "";
    String startDate = "";
    String minPrice = "";
    String maxPrice = "";
    String url = "";

    public Ticket(String eventName, String startDate, String minPrice, String maxPrice, String url) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.url = url;
    }

    public String getEventName() {return eventName;}
    public String getStartDate() {return startDate;}
    public String getMinPrice() {return minPrice;}
    public String getMaxPrice() {return maxPrice;}
    public String getUrl() {return url;}
}
