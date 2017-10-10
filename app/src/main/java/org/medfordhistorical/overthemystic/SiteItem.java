package org.medfordhistorical.overthemystic;

/**
 * Created by annakasagawa on 10/5/17.
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

        public int getLat(){
            return _lat;
        }

        public int getLng(){
            return _lng;
        }

        public String getImageurl(){
            return _imageurl;
        }

        public String getDesc(){
            return _desc;
        }

        


    public void setName(String name)
        {
            this._name = name;
        }

        public String getAuthor()
        {
            return _author;
        }

        public void setAuthor(String auth)
        {
            this._author = auth;
        }
}
