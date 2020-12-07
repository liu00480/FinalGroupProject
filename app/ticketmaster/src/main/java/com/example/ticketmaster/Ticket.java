package com.example.ticketmaster;

/**
 * Class to store ticket
 */
public class Ticket {

    long id;
    String eventName;
    String startDate;
    String minPrice ;
    String maxPrice;
    String url;
    String imgUrl;

    /**
     * Constructor create instance of ticket
     * @param eventName String: event name
     * @param startDate String: event start date
     * @param minPrice String: event min price
     * @param maxPrice String: event max price
     * @param url String: event url
     * @param imgUrl String: event image url
     */
    public Ticket(String eventName, String startDate, String minPrice, String maxPrice, String url, String imgUrl) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.url = url;
        this.imgUrl = imgUrl;
    }

    /**
     * Constructor create instance of ticket with ID
     * @param id event id
     * @param eventName event name
     * @param startDate event start date
     * @param minPrice event min price
     * @param maxPrice event max price
     * @param url event url
     * @param imgUrl event image url
     */
    public Ticket(long id, String eventName, String startDate, String minPrice, String maxPrice, String url, String imgUrl) {
        this.id = id;
        this.eventName = eventName;
        this.startDate = startDate;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.url = url;
        this.imgUrl = imgUrl;
    }

    /**
     * Get event name
     * @return event name
     */
    public String getEventName() {return eventName;}

    /**
     * Get event start date
     * @return event start date
     */
    public String getStartDate() {return startDate;}

    /**
     * Get event min price
     * @return event min price
     */
    public String getMinPrice() {return minPrice;}

    /**
     * Get event max price
     * @return event max price
     */
    public String getMaxPrice() {return maxPrice;}

    /**
     * Get event url
     * @return event url
     */
    public String getUrl() {return url;}

    /**
     * Get event image url (4:3 radio)
     * @return event image url (4:3 radio)
     */
    public String getImgUrl() {return imgUrl;}

    /**
     * Get event id
     * @return event id
     */
    public long getId() { return id; }

    /**
     * Override the toString() method
     * @return
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", minPrice='" + minPrice + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                ", url='" + url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}