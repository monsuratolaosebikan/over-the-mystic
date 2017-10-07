package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

public class SelectedSitesRecyclerViewAdapter extends RecyclerView.Adapter<SelectedSitesRecyclerViewAdapter.ViewHolder> {
    private List<Site> sites;
    private Context context;
    public MapboxMap map;

    public SelectedSitesRecyclerViewAdapter(List<Site> sites, MapboxMap map) {
        this.sites = sites;
        this.map = map;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View siteView = inflater.inflate(R.layout.rv_site_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(siteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Site site = this.sites.get(position);

        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(site.getName());

        TextView shortDescTextView = viewHolder.shortDescTextView;
        shortDescTextView.setText(site.getShortDesc());
    }

    @Override
    public int getItemCount() {
        return this.sites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView shortDescTextView;

        public ViewHolder(View siteView) {
            super(siteView);

            nameTextView = (TextView) siteView.findViewById(R.id.site_name);
            shortDescTextView = (TextView) siteView.findViewById(R.id.site_shortDesc);
        }

    }
}
