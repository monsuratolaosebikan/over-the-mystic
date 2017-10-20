package org.medfordhistorical.overthemystic;
import com.mapbox.mapboxsdk.geometry.LatLng;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * The SiteItem class holds all the information about each of the historical sites.
 * The class contains get and set functions that will return or set a variable with information
 * about the site.
 */

public class Site extends RealmObject {

    private String name;
    private String shortDesc;
    private String longDesc;
    private String imageUrl;
    @Ignore
    private LatLng location;


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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
