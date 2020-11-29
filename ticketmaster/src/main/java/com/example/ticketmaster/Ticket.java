package com.example.ticketmaster;

public class Ticket {

    long id;
    String eventName = "";
    String startDate = "";
    String minPrice = "";
    String maxPrice = "";
    String url = "";
    String imgUrl="";

    public Ticket(String eventName, String startDate, String minPrice, String maxPrice, String url, String imgUrl) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.url = url;
        this.imgUrl = imgUrl;
    }

    public String getEventName() {return eventName;}
    public String getStartDate() {return startDate;}
    public String getMinPrice() {return minPrice;}
    public String getMaxPrice() {return maxPrice;}
    public String getUrl() {return url;}
    public String getImgUrl() {return imgUrl;}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
