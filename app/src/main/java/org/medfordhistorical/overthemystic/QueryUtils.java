package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

public class QueryUtils {
    private static String URL = "http://174.138.43.181/directus/api/1.1/tables/sites/rows";

    private QueryUtils() {}
    static RequestQueue queue;

    public static ArrayList<Site> getSites(Context context) {
        ArrayList<Site> sites = new ArrayList<>();
        queue = Volley.newRequestQueue(context);

//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.deleteAll();
//        Site site = realm.createObject(Site.class);
//        site.setName("Tufts University");
//        site.setShortDesc("Learn some stuff");
//        site.setImageUrl("http://174.138.43.181/directus/storage/uploads/00000000022.jpg");
//        realm.commitTransaction();
//        realm.close();

       JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
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
                try {
                    for (int i = 0; i < sites.length(); i++) {
                        Site site = bgRealm.createObject(Site.class);
                        JSONObject s = sites.getJSONObject(i);
                        site.setName(s.getString("name"));
                        site.setImageUrl(s.getString("image"));
                        site.setShortDesc(s.getString("short_description"));
                    }
                    Realm realm = Realm.getDefaultInstance();

                    RealmResults<Site> sites = realm.where(Site.class)
                            .findAll();

                    Log.d("main", sites.get(1).toString());
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
