package com.example.animalhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalhub.R;
import com.example.animalhub.model.Model_FeedBack;

import java.util.ArrayList;


public class FeedBack_Adapter extends RecyclerView.Adapter<FeedBack_Adapter.AdViewHolder> {

    ArrayList<Model_FeedBack> adList;
    Context context;
    public FeedBack_Adapter(ArrayList<Model_FeedBack> adList) {
        this.adList = adList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.feedback_view, parent, false);

        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {

        Model_FeedBack ad=adList.get(position);
        if (ad!=null){
            holder.vName.setText(ad.getUName());
            holder.vFeedBack.setText(ad.getFeedBack());
        }
       // holder.image.setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder{

        TextView vName,vFeedBack,vDate,vTime;
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            vName = itemView.findViewById(R.id.name);
            vFeedBack = itemView.findViewById(R.id.feedBack);
        }
    }
}
