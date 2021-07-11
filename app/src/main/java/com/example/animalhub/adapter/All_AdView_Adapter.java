package com.example.animalhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalhub.model.ModelAd;
import com.example.animalhub.ui.Admin.Ad_Approved_Activity;
import com.example.animalhub.ui.play.Add_View_Activity;
import com.example.animalhub.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class All_AdView_Adapter extends RecyclerView.Adapter<All_AdView_Adapter.AdViewHolder> {

    ArrayList<ModelAd> adList;
    Context context;
    public All_AdView_Adapter(ArrayList<ModelAd> adList) {
        this.adList = adList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_view_my_ads, parent, false);

        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {

        ModelAd ad=adList.get(position);
        if (ad!=null){
            Picasso.get().load(adList.get(position).getImage()).into(holder.image);
            holder.vTitle.setText(ad.getTitle());
            holder.vPrice.setText(ad.getPrice()+"/-");
            holder.vDescription.setText(ad.getDescription());
        }
       // holder.image.setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView image;
        TextView vTitle,vPrice,vDescription;
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.name);
            vPrice = itemView.findViewById(R.id.price);
            vDescription = itemView.findViewById(R.id.detail);
            image=itemView.findViewById(R.id.image);
            image.setShapeAppearanceModel(image.getShapeAppearanceModel().toBuilder()
                    .setTopRightCorner(CornerFamily.ROUNDED, 0)
                    .setTopLeftCorner(CornerFamily.ROUNDED, 10)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, 10)
                    .setBottomRightCorner(CornerFamily.ROUNDED, 0)
                    .build());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equalsIgnoreCase("admin@animalhub.com")){

                        Intent i = new Intent(context, Ad_Approved_Activity.class);
                        i.putExtra("ad",adList.get(getAdapterPosition()));
                        context.startActivity(i);
                    }
                    else {
                        Intent i = new Intent(context, Add_View_Activity.class);
                        i.putExtra("ad",adList.get(getAdapterPosition()));
                        context.startActivity(i);
                    }

//                    Intent i=new Intent(context, Add_View_Activity.class);
//                    i.putExtra("ad",adList.get(getAdapterPosition()));
//                    context.startActivity(i);
                }
            });
        }
    }
}
