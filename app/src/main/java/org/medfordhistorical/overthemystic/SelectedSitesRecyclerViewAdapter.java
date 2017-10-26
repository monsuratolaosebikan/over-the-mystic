package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.content.Intent;
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
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.commons.models.Position;

import java.util.List;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class SelectedSitesRecyclerViewAdapter extends RecyclerView.Adapter<SelectedSitesRecyclerViewAdapter.ViewHolder> {
    private List<Site> sites;
    private MapboxMap map;
    private Context context;

    public SelectedSitesRecyclerViewAdapter(List<Site> sites, MapboxMap map) {
        this.sites = sites;
        this.map = map;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
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

        FloatingActionMenu navigateBtn = viewHolder.navigateBtn;
        navigateBtn.bringToFront();
        navigateBtn.setIconAnimated(false);
        navigateBtn.getMenuIconView().setImageResource(R.drawable.ic_action_navigate);

        ImageView imageView = viewHolder.imageView;

        Glide.with(viewHolder.imageView.getContext())
                .load(site.getImageUrl())
                .apply(new RequestOptions().fitCenter())
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.sites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private ImageView imageView;
        private FloatingActionMenu navigateBtn;
        private FloatingActionButton walkBtn;
        private FloatingActionButton bikeBtn;

        public ViewHolder(final View siteView) {
            super(siteView);

            nameTextView = (TextView) siteView.findViewById(R.id.site_name);
            imageView = (ImageView) siteView.findViewById(R.id.site_image);
            navigateBtn = (FloatingActionMenu) siteView.findViewById(R.id.navigate);
            walkBtn = (FloatingActionButton) siteView.findViewById(R.id.navigate_walk);
            bikeBtn = (FloatingActionButton) siteView.findViewById(R.id.navigate_bike);
            walkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewLocationDetail.class);
                    int position = getAdapterPosition();
                    intent.putExtra("siteId", sites.get(position).getId());
                    intent.putExtra("siteName", sites.get(position).getName());
                    intent.putExtra("siteDesc", sites.get(position).getShortDesc());
                    intent.putExtra("imageUrl", sites.get(position).getImageUrl());
                    intent.putExtra("audioUrl", sites.get(position).getAudioUrl());

                    context.startActivity(intent);
                }
            });
            bikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewLocationDetail.class);
                    int position = getAdapterPosition();
                    intent.putExtra("siteId", sites.get(position).getId());
                    intent.putExtra("siteName", sites.get(position).getName());
                    intent.putExtra("siteDesc", sites.get(position).getShortDesc());
                    intent.putExtra("imageUrl", sites.get(position).getImageUrl());
                    intent.putExtra("audioUrl", sites.get(position).getAudioUrl());

                    double longitude = sites.get(position).getLongitude();
                    double latitude = sites.get(position).getLatitude();

                    boolean simulateRoute = true;
                    MapActivity activity;

                    //hacky, switch to use interface
                    try {
                        activity = (MapActivity) view.getContext();
                        Position origin = Position.fromCoordinates(activity.originLocation.getLongitude(),activity.originLocation.getLatitude());
                        Position destination = Position.fromCoordinates(longitude, latitude);

                        if(origin != null) {
                            sites.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, sites.size());
                            context.startActivity(intent);

                            NavigationLauncher.startNavigation(activity, origin, destination, null, simulateRoute);

                        }
                    } catch (Exception e) {
                        Log.e("location error", e.toString());
                    }
                }
            });
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