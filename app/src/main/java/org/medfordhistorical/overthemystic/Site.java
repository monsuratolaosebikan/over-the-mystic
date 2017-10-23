package org.medfordhistorical.overthemystic;

import com.mapbox.mapboxsdk.geometry.LatLng;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * The SiteItem class holds all the information about each of the historical sites.
 * The class contains get and set functions that will return or set a variable with information
 * about the site.
 */

public class Site extends RealmObject {

    @PrimaryKey
    private int id;

    private String name;
    private String shortDesc;
    private String longDesc;
    private String imageUrl;
    private String audioUrl;
    private double latitude;
    private double longitude;

    public void setId(int id) { this.id = id; }

    public int getId() { return this.id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public LatLng getLocation() {

        return new LatLng(this.latitude, this.longitude);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
