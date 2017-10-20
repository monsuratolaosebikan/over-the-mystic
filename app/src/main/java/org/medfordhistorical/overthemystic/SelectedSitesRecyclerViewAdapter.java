package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

public class SelectedSitesRecyclerViewAdapter extends RecyclerView.Adapter<SelectedSitesRecyclerViewAdapter.ViewHolder> {
    private List<Site> sites;
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

        ImageView imageView = viewHolder.imageView;

        RequestOptions options = new RequestOptions();
        options.fitCenter();

        Glide.with(viewHolder.imageView.getContext())
                .load(site.getImageUrl())
                .apply(options)
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.sites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView shortDescTextView;
        public ImageView imageView;

        public ViewHolder(View siteView) {
            super(siteView);

            nameTextView = (TextView) siteView.findViewById(R.id.site_name);
            imageView = (ImageView) siteView.findViewById(R.id.site_image);
            siteView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted

                LatLng selectedLocationLatLng = sites.get(position).getLocation();
                CameraPosition newCameraPosition = new CameraPosition.Builder()
                        .target(selectedLocationLatLng)
                        .build();

                map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
                List<Marker> markers = map.getMarkers();
                map.selectMarker(markers.get(position));

            }
        }

    }
}
