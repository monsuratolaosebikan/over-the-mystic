package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class QueryUtils {
    private static String URL = "http://174.138.43.181";
    static RequestQueue queue;

    private QueryUtils() {}

    public static void getSitesFromServer(Context context) {
        queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + "/directus/api/1.1/tables/sites/rows" , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray rawSiteData;
                try {
                    rawSiteData = response.getJSONArray("data");
                    saveToDatabase(rawSiteData);
                } catch (JSONException e) {
                    Log.e("query", "to array error");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("query", error.toString());
            }
        });
        queue.add(request);
    }

    public static List<Site> getSitesFromDatabase() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Site.class).findAll();
    }

    public static Site getSiteFromDatabase(int id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Site.class).equalTo("id", id).findFirst();
    }

    private static void saveToDatabase(final JSONArray sites) {
        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                try {
                    for (int i = 0; i < sites.length(); i++) {
                        JSONObject s = sites.getJSONObject(i);
                        Site site = new Site();
                        site.setName(s.getString("name"));
                        site.setId(s.getInt("id"));

                        JSONObject image = null;
                        try {
                            image = s.getJSONObject("image");
                        } catch(JSONException j) {}

                        if(image != null) {
                            site.setImageUrl(URL + image.getJSONObject("data").getString("url"));
                        }

                        JSONObject audio = null;
                        try {
                            audio = s.getJSONObject("audio");
                        } catch(JSONException j) {}

                        if(audio != null) {
                            site.setAudioUrl(URL + audio.getJSONObject("data").getString("url"));
                        }

                        site.setShortDesc(s.getString("short_description"));
                        String location[] = s.getString("location").split(",");
                        site.setLatitude((Double.parseDouble(location[0])));
                        site.setLongitude((Double.parseDouble(location[1])));
                        bgRealm.copyToRealmOrUpdate(site);
                    }

                } catch(JSONException j) {
                    Log.e("save to database", j.toString());

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("realm save", "success");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("realm save", error.toString());
            }
        });
    }
}
