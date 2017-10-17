package org.medfordhistorical.overthemystic;

/**
 * The SiteItem class holds all the information about each of the historical sites.
 * The class contains get and set functions that will return or set a variable with information
 * about the site.
 */

public class SiteItem {
        private String _name;
        private int _lat;
        private int _lng;
        private String _imageurl;
        private String _desc;


        public String getName()
        {
            return _name;
        }

        public void setName(String name)
        {
            this._name = name;
        }

        public int getLat(){
            return _lat;
        }

        public void setLat(int lat)
        {
            this._lat = lat;
        }

        public int getLng(){
            return _lng;
        }

        public void setLng(int lng)
        {
            this._lng = lng;
        }

        public String getImageurl(){
            return _imageurl;
        }

        public void setImageurl(String imageurl)
        {
            this._imageurl = imageurl;
        }

        public String getDesc(){
            return _desc;
        }

        public void setDesc(String desc){
            this. _desc = desc;
        }
}

