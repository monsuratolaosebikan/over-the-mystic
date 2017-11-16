package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class SelectedSitesRecyclerViewAdapter extends RecyclerView.Adapter<SelectedSitesRecyclerViewAdapter.ViewHolder> {
    private List<Site> sites;
    private MapboxMap map;
    private Context context;
    private ClickListener clickListener;

    public SelectedSitesRecyclerViewAdapter(List<Site> sites, MapboxMap map, Context context, ClickListener cardClickListener) {
        this.sites = sites;
        this.map = map;
        this.context = context;
        this.clickListener = cardClickListener;
    }

    public interface ClickListener {
        void onItemClick(int position);
        void onNavButtonClick(int position, String type);
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

  public class ViewHolder extends RecyclerView.ViewHolder {
      private TextView nameTextView;
      private ImageView imageView;
      private CardView cardView;
      private FloatingActionMenu navigateBtn;
      private FloatingActionButton walkBtn;
      private FloatingActionButton bikeBtn;

      public ViewHolder(final View siteView) {
          super(siteView);

          nameTextView = siteView.findViewById(R.id.site_name);
          imageView = siteView.findViewById(R.id.site_image);
          cardView = siteView.findViewById(R.id.card_view);
          navigateBtn = siteView.findViewById(R.id.navigate);
          walkBtn = siteView.findViewById(R.id.navigate_walk);
          bikeBtn = siteView.findViewById(R.id.navigate_bike);

          cardView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  clickListener.onItemClick(getAdapterPosition());
              }
          });

          bikeBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  int position = getAdapterPosition();
                  clickListener.onNavButtonClick(position, "bike");
              }
          });

          walkBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  int position = getAdapterPosition();
                  clickListener.onNavButtonClick(getAdapterPosition(), "walk");
              }
          });

      }
  }
}