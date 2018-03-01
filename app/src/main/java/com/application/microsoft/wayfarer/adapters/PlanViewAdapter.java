package com.application.microsoft.wayfarer.adapters;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.application.microsoft.wayfarer.R;
import com.application.microsoft.wayfarer.models.Plan;

public class PlanViewAdapter extends ArrayAdapter<Plan> {
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }

    private Context context;
    private ArrayList<Plan> data = new ArrayList<>();

    public PlanViewAdapter(Context context, ArrayList<Plan> data) {
        super(context , R.layout.plan_layout, data);

        this.data = data;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.plan_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.planTo);
            viewHolder.madeOn = (TextView)convertView.findViewById(R.id.madeOn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(("Trip to " + data.get(position).getPlanedPlaces().get(0).getCity()));
        viewHolder.madeOn.setText("Made On " + data.get(position).getMadeOn().toString());
        return convertView;
    }

    public class ViewHolder {
        TextView text;
        TextView madeOn;
        Button button;
    }
}
