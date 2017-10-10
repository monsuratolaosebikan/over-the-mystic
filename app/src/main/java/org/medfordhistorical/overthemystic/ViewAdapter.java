package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by annakasagawa on 10/5/17.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<SiteItem> itemList;
    private Context context;

    public SampleRecyclerViewAdapter(Context context,
                                     List<SiteItem> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grid_item, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bookName.setText(itemList.get(position).getName());
        holder.authorName.setText(itemList.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}



