package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SitesAdapter extends BaseAdapter {
    private Context context;
    private List<Site> siteList;
    private ArrayList<Integer> sitesSelected;

    public SitesAdapter(Context context, List<Site> sites) {
        this.context = context;
        this.siteList = sites;
        sitesSelected = new ArrayList<Integer>();
    }

    @Override
    public int getCount() {
        return siteList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        final Site site = siteList.get(position);

        if (itemView == null) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            itemView = inflater.inflate(R.layout.grid_item, null);
        }

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
        final TextView textView = (TextView) itemView.findViewById(R.id.name);
        final ImageView overlay =  (ImageView)itemView.findViewById(R.id.overlayImg);

        Glide.with(imageView.getContext())
                .load(siteList.get(position).getImageUrl())
                .into(imageView);
        textView.setText(siteList.get(position).getName());

        if(site.isSelected()) {
            overlay.setVisibility(View.VISIBLE);
            Integer id = siteList.get(position).getId();

            if(!sitesSelected.contains(id)) {
                Log.d("changing to visible", id.toString());
                sitesSelected.add(id);
            }
        }
        else {
            overlay.setVisibility(View.INVISIBLE);
            Integer id = siteList.get(position).getId();
            sitesSelected.remove(Integer.valueOf(id));
        }
        notifyDataSetChanged();
        return itemView;
    }

    public static int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] intArray = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < intArray.length; i++)
        {
            intArray[i] = iterator.next().intValue();
        }
        return intArray;
    }

    public int[] getSitesSelected(){
        return convertIntegers(sitesSelected);
    }

}





