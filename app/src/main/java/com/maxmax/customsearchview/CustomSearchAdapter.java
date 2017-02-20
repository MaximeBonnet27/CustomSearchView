package com.maxmax.customsearchview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mData;
    private List<CustomSearchAdapterListener> mListeners;

    private boolean mIsLoading;

    public CustomSearchAdapter() {
        mData = new ArrayList<>();
        mListeners = new ArrayList<>();
        mIsLoading = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return createLoadingViewHolder(parent);
            case 1:
                return createItemViewHolder(parent);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!mIsLoading) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mTextView.setText(mData.get(position));
        }
    }

    private LoadingViewHolder createLoadingViewHolder(ViewGroup parent) {
        ProgressBar progressBar = (ProgressBar) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_search_loading, parent, false);
        return new LoadingViewHolder(progressBar);
    }

    @NonNull
    private ItemViewHolder createItemViewHolder(ViewGroup parent) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_search, parent, false);
        return new ItemViewHolder(textView);
    }

    @Override
    public int getItemViewType(int position) {
        return mIsLoading ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return mIsLoading ? 1 : mData.size();
    }

    void generateData(String input) {
        mData.clear();
        final int size = input.length();
        if (size % 2 == 0) {
            mIsLoading = false;
            for (int i = 0; i < size; ++i) {
                mData.add(input);
            }
        } else {
            mIsLoading = true;
        }
        notifyDataSetChanged();
        notifyListeners();
    }

    void addListener(CustomSearchAdapterListener listener) {
        mListeners.add(listener);
    }

    private void notifyListeners() {
        for (CustomSearchAdapterListener listener : mListeners) {
            listener.onSizeChanged(getItemCount());
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ItemViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(ProgressBar progressBar) {
            super(progressBar);
            this.progressBar = progressBar;
        }
    }
}
