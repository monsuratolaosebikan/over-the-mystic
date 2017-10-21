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
import io.realm.Realm;

public class QueryUtils {
    private static String URL = "http://174.138.43.181";
    static RequestQueue queue;

    private QueryUtils() {}

    public static ArrayList<Site> getSites(Context context) {
        ArrayList<Site> sites = new ArrayList<>();
        queue = Volley.newRequestQueue(context);

       JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + "/directus/api/1.1/tables/sites/rows" , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray rawSiteData = new JSONArray();
                try {
                    rawSiteData = response.getJSONArray("data");
                    saveToDatabase(rawSiteData);
                } catch (JSONException e) {
                    Log.e("query", "to array error");
                }

                Log.d("query", rawSiteData.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("query", error.toString());
            }
        });
        queue.add(request);
        return sites;
    }

    private static void saveToDatabase(final JSONArray sites) {
        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.deleteAll();
                try {
                    for (int i = 0; i < sites.length(); i++) {
                        JSONObject s = sites.getJSONObject(i);
                        Site site = bgRealm.createObject(Site.class, s.getInt("id"));
                        site.setName(s.getString("name"));
                        JSONObject image = null;
                        try {
                            image = s.getJSONObject("image");
                        } catch(JSONException j) {}

                        if(image != null) {
                            site.setImageUrl(URL + image.getJSONObject("data").getString("url"));
                        }

                        site.setShortDesc(s.getString("short_description"));
                        String location[] = s.getString("location").split(",");
                        site.setLatitude((Double.parseDouble(location[0])));
                        site.setLongitude((Double.parseDouble(location[1])));
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
