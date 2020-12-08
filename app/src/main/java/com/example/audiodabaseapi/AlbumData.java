package com.example.audiodabaseapi;

/**
 * AlbumData - a class where we save all album data for user search
 * it is like a calendar containing all data information
 * the browser
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class AlbumData {

    String AlbumID;
    String ArtistID;
    String AlbumName;
    String AlbumURL;
    String ArtistName;
    String intYear;
    String Description;

    /**
     *public no arg constructor
     */
    public AlbumData()
    {

    }

    /**
     * @param albumID
     * @param artistID
     * @param albumName
     * @param albumURL
     */
    public AlbumData(String albumID, String artistID, String albumName, String albumURL, String artistName, String intYear, String description) {
        AlbumID = albumID;
        ArtistID = artistID;
        AlbumName = albumName;
        AlbumURL = albumURL;
        ArtistName = artistName;
        this.intYear = intYear;
        Description = description;
    }

    /**
     * @return  AlbumID
     */
    public String getAlbumID() {
        return AlbumID;
    }

    /**
     * @param albumID
     */
    public void setAlbumID(String albumID) {
        AlbumID = albumID;
    }

    /**
     * @return ArtistID
     */
    public String getArtistID() {
        return ArtistID;
    }

    /**
     * @param artistID
     */
    public void setArtistID(String artistID) {
        ArtistID = artistID;
    }

    /**
     * @return AlbumName
     */
    public String getAlbumName() {
        return AlbumName;
    }

    /**
     * @param albumName
     */
    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    /**
     * @return AlbumURL
     */
    public String getAlbumURL() {
        return AlbumURL;
    }

    /**
     * @param albumURL
     */
    public void setAlbumURL(String albumURL) {
        AlbumURL = albumURL;
    }
    public String getArtistName() {
        return ArtistName;
    }

    /**
     * @param artistName
     */
    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    /**
     * @return intYear
     */
    public String getIntYear() {
        return intYear;
    }

    /**
     * @param intYear
     */
    public void setIntYear(String intYear) {
        this.intYear = intYear;
    }

    /**
     * @return Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        Description = description;
    }
}
