package com.example.uilayer.milestones.betterAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uilayer.milestones.betterAdapter.holder.ViewHolder;
import com.example.uilayer.milestones.betterAdapter.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class MilestonesAdapter extends RecyclerView.Adapter<ViewHolder<ViewModel>> {
  private  List<ViewModel> viewModelsList;
    private TypeFactoryImpl typeFactory;

    public MilestonesAdapter(ArrayList<ViewModel> viewModels) {
        this.viewModelsList = viewModels;
        typeFactory = new TypeFactoryImpl();
    }

    @Override
    public void onBindViewHolder(ViewHolder<ViewModel> holder, int position) {
        holder.bind(viewModelsList.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModelsList.size();
    }

    @Override
    public ViewHolder<ViewModel> onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return (ViewHolder<ViewModel>) typeFactory.holder(viewType, view);

    }

    @Override
    public int getItemViewType(int position) {
        return viewModelsList.get(position).type(typeFactory);
    }
}
