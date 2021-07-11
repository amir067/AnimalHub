package com.example.animalhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.animalhub.ui.play.Add_View_Activity;
import com.example.animalhub.R;
import com.example.animalhub.model.ModelAd;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdView_Adapter extends ArrayAdapter implements Filterable {


    List<ModelAd> adList;
    List<ModelAd> originalList;
    Context context;

    public AdView_Adapter(@NonNull Context context,List<ModelAd> adList ) {
        super(context, android.R.layout.simple_selectable_list_item);
        this.adList=adList;
        this.originalList=adList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return adList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ShapeableImageView image;
        TextView vTitle,vPrice,vDescription;
        if (view==null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.activityviewad, parent, false);
        }
        vTitle = view.findViewById(R.id.name);
        vPrice = view.findViewById(R.id.price);
        vDescription = view.findViewById(R.id.detail);
        image=view.findViewById(R.id.image);
        image.setShapeAppearanceModel(image.getShapeAppearanceModel().toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, 15)
                .setTopLeftCorner(CornerFamily.ROUNDED, 15)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 0)
                .setBottomRightCorner(CornerFamily.ROUNDED, 0)
                .build());

        ModelAd ad=adList.get(position);
        if (ad!=null){
            Picasso.get().load(adList.get(position).getImage()).into(image);
            vTitle.setText(ad.getTitle());
            vPrice.setText("pkr "+ad.getPrice()+"/-");
            vDescription.setText(ad.getDescription());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, Add_View_Activity.class);
                i.putExtra("ad",ad);
                context.startActivity(i);
            }
        });
        return view;
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adList = (List<ModelAd>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ModelAd> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }
    protected List<ModelAd> getFilteredResults(String constraint) {
        List<ModelAd> results = new ArrayList<>();

        for (ModelAd item : originalList) {
            if (item.getTitle().toLowerCase().contains(constraint) || item.getLocation().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

}
