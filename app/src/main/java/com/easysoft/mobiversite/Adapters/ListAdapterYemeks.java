package com.easysoft.mobiversite.Adapters;

/**
 * Created by SeymurElk on 10/27/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.easysoft.mobiversite.Activities.MainActivity;
import com.easysoft.mobiversite.R;
import com.easysoft.mobiversite.Models.Yemek;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;

import java.util.ArrayList;

public class ListAdapterYemeks extends ArrayAdapter<Yemek> {
    ICallback ic;
    private final Context context;
    private final ArrayList<Yemek> itemsArrayList;
    private int[] states;

    public ListAdapterYemeks(Context context, ArrayList<Yemek> itemsArrayList, ICallback callback) {

        super(context, R.layout.list_back, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.ic = callback;
        this.states  = new int[itemsArrayList.size()];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_back, parent, false);

        TextView yemekYear = (TextView) rowView.findViewById(R.id.tvYear);
        TextView yemekMonth = (TextView) rowView.findViewById(R.id.tvMonth);
        TextView yemekRestName = (TextView) rowView.findViewById(R.id.tvRest);
        TextView yemekIngr = (TextView) rowView.findViewById(R.id.tvYemek);
        TextView yemekState = (TextView) rowView.findViewById(R.id.tvStatus);
        TextView yemekPrice = (TextView) rowView.findViewById(R.id.tvPrice);

        TextView yemekDetail = (TextView) rowView.findViewById(R.id.tvDetail);
        TextView yemekDetailedPrice = (TextView) rowView.findViewById(R.id.tvDetailedPrice);
        final ToggleButton expandRow = (ToggleButton) rowView.findViewById(R.id.tbListExpand);








        yemekYear.setText(itemsArrayList.get(position).getYear());
        yemekMonth.setText(itemsArrayList.get(position).getMonth());
        yemekRestName.setText(itemsArrayList.get(position).getRestaurantName());
        yemekIngr.setText(itemsArrayList.get(position).getFoodIngredients());
        yemekPrice.setText(String.valueOf(itemsArrayList.get(position).getFoodPrice()) + MainActivity.currency);
        yemekDetail.setText(itemsArrayList.get(position).getSummary());
        yemekDetailedPrice.setText(String.valueOf(itemsArrayList.get(position).getSummaryPrice()) + MainActivity.currency);

        String state = itemsArrayList.get(position).getState();
        yemekState.setText(state);
       if(state.equals("Hazırlanıyor")){
            yemekState.setBackgroundTintList(context.getResources().getColorStateList(android.R.color.black));
            yemekState.setCompoundDrawableTintList(context.getResources().getColorStateList(android.R.color.black));
            yemekState.setTextColor(context.getResources().getColorStateList(android.R.color.black));
            yemekState.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable( R.drawable.ic_preparing ),null,null,null);
        }else if(state.equals("Onay Bekliyor")){
            yemekState.setBackgroundTintList(context.getResources().getColorStateList(android.R.color.holo_orange_dark));
            yemekState.setCompoundDrawableTintList(context.getResources().getColorStateList(android.R.color.holo_orange_dark));
            yemekState.setTextColor(context.getResources().getColorStateList(android.R.color.holo_orange_dark));
            yemekState.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable( R.drawable.ic_waiting ),null,null,null);
        }else{
            yemekState.setBackgroundTintList(context.getResources().getColorStateList(android.R.color.holo_green_dark));
            yemekState.setCompoundDrawableTintList(context.getResources().getColorStateList(android.R.color.holo_green_dark));
            yemekState.setTextColor(context.getResources().getColorStateList(android.R.color.holo_green_dark));
            yemekState.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable( R.drawable.ic_on_the_way ),null,null,null);
        }

        final ExpandableWeightLayout expandableLayout = (ExpandableWeightLayout) rowView.findViewById(R.id.expandableLayout);
        if(states[position] == 0)
            expandableLayout.collapse();
//        else
//            expandableLayout.expand();
        expandRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandRow.isChecked()) {
                    states[position] = 1;
                    expandableLayout.expand();
                }
                else{
                    states[position] = 0;
                    expandableLayout.collapse();
                }
            }
        });
        return rowView;
    }


    public void refreshListYemekler(ArrayList<Yemek> events) {
        this.itemsArrayList.clear();
        this.itemsArrayList.addAll(events);
        notifyDataSetChanged();
    }

    public interface ICallback {

        void onExtraEvent(int position);

    }




}
