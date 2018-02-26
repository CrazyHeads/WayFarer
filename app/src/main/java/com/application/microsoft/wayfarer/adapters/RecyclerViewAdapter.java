package com.application.microsoft.wayfarer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.interfaces.ItemTouchHelperAdapter;
import com.application.microsoft.wayfarer.interfaces.ItemTouchHelperViewHolder;
import com.application.microsoft.wayfarer.interfaces.OnStartDragListener;
import com.application.microsoft.wayfarer.models.Place;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  implements ItemTouchHelperAdapter {
    private ArrayList<Place> list;
    private final OnStartDragListener mDragStartListener;
    public RecyclerViewAdapter(ArrayList<Place> list, Context context, OnStartDragListener dragStartListener) {
        this.list = list;
        mDragStartListener = dragStartListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText((CharSequence) list.get(position));
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        TextView textView;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.text);
//            imageView=(ImageView)itemView.findViewById(R.id.handle);
        }
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }
        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
