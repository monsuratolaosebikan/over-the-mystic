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

    private QueryUtils() {}
    static RequestQueue queue;

    public static ArrayList<Site> getSites(Context context) {
        ArrayList<Site> sites = new ArrayList<>();
        queue = Volley.newRequestQueue(context);

       JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, URL + "/directus/api/1.1/tables/sites/rows" , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray rawSiteData = new JSONArray();
                try {
                    rawSiteData = response.getJSONArray("data");
                    saveToDatabase(rawSiteData);
                } catch (JSONException e) {
                    Log.d("query", "to array error");
                }

                Log.d("query", rawSiteData.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("query", error.toString());
            }
        });
        queue.add(arrayRequest);
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
                        Site site = bgRealm.createObject(Site.class);
                        JSONObject s = sites.getJSONObject(i);
                        site.setName(s.getString("name"));
                        JSONObject image = null;
                        try {
                            image = s.getJSONObject("image");
                        } catch(JSONException j) {}

                        if(image != null) {
                            site.setImageUrl(URL + image.getJSONObject("data").getString("url"));
                        }

                        site.setShortDesc(s.getString("short_description"));
                    }

                } catch(JSONException j) {
                    Log.d("save to database", j.toString());

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
            /* success actions */
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
            /* failure actions */
            }
        });
    }
}
