package com.hiteshchopra.magnumhitesh.Pagination;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hiteshchopra.magnumhitesh.Models.Items;
import com.hiteshchopra.magnumhitesh.R;

import java.util.ArrayList;
import java.util.List;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<Items> mGithubUser;
    private Context context;
    public  View v2;
    private boolean isLoadingAdded = false;
    public PaginationAdapter(Context context) {
        this.context = context;
        mGithubUser = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                Log.e("LOCATION", "ViewHolder");
                v2 = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.no_results, parent, false);
                viewHolder = new NoResultVH(v3);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item, parent, false);
        viewHolder = new GithubViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Items result = mGithubUser.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final GithubViewHolder githubViewHolder = (GithubViewHolder) holder;
                githubViewHolder.mLoginNameTextView.setText(""+result.loginName);
                githubViewHolder.mLoginIdTextView.setText("ID-: " + result.loginID);
                Glide.with(context).load(mGithubUser.get(position).getImageUrl()).apply(RequestOptions.circleCropTransform()).into(((GithubViewHolder) holder).mImageView);
                break;
            case LOADING:
        }
    }

    @Override
    public int getItemCount() {
        return mGithubUser.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mGithubUser.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Items g) {
        mGithubUser.add(g);
        notifyItemInserted(mGithubUser.size() - 1);
    }

    public void addAll(List<Items> listItems) {
        for (Items result : listItems) {
            add(result);
        }
    }

    public void remove(Items r) {
        int position = mGithubUser.indexOf(r);
        if (position > -1) {
            mGithubUser.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Items());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mGithubUser.size() - 1;
        Items result = getItem(position);

        if (result != null) {
            mGithubUser.remove(position);
            notifyItemRemoved(position);
        }

    }

    public Items getItem(int position) {
        return mGithubUser.get(position);
    }

    public void removeProgress() {
        if(v2!=null)
        v2.setVisibility(View.INVISIBLE);
    }

    public void showProgress() {
        if(v2!=null)
        v2.setVisibility(View.VISIBLE);
    }
    public class GithubViewHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;
        ImageView mImageView;
        TextView mLoginNameTextView;
        TextView mLoginIdTextView;

        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.avatarImage);
            mLoginNameTextView = itemView.findViewById(R.id.loginNameTextView);
            mLoginIdTextView = itemView.findViewById(R.id.loginIdTextView);
        }
    }

    public class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
            v2.setVisibility(View.GONE);
        }
    }

    protected class NoResultVH extends RecyclerView.ViewHolder {
        public NoResultVH(View itemView) {
            super(itemView);
        }
    }

}
