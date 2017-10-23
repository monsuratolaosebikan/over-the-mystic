package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//recycle view adapter that will use layout for each item
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Site> siteList;
    private ArrayList<Site> sitesSelected = new ArrayList<Site>();

    public ViewAdapter(Context context, ArrayList<Site> sites) {
        this.context = context;
        this.siteList = sites;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layoutView = inflater.inflate(R.layout.grid_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
            .load(siteList.get(position).getImageUrl())
            .into(holder.imageView);

        holder.textView.setText(siteList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return siteList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);
            textView = (TextView) itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        //Store which sites were selected, and change the image effects to show it
        private int count = 0;
        @Override
        public void onClick(View view){
            count++;
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String name = siteList.get(position).getName();
                sitesSelected.add(siteList.get(position));
                Toast.makeText(context, "added "+ name, Toast.LENGTH_SHORT).show();
                getSelections();
            }

        }

        public ArrayList<Site> getSelections(){
            Log.d("getSelection: ", sitesSelected.size()+"");
            return sitesSelected;
        }


    }

}





