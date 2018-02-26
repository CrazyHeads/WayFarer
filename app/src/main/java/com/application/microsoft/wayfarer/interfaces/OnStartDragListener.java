package com.application.microsoft.wayfarer.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by RAJULA on 25-02-2018.
 */

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
