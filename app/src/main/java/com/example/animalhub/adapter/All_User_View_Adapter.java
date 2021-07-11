package com.example.animalhub.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalhub.R;
import com.example.animalhub.model.Register_ModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class All_User_View_Adapter extends RecyclerView.Adapter<All_User_View_Adapter.AdViewHolder> {
    Register_ModelClass register_modelClass;
    ArrayList<Register_ModelClass> adList;
    Context context;
    public All_User_View_Adapter(ArrayList<Register_ModelClass> adList) {
        this.adList = adList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_view, parent, false);

        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {

        Register_ModelClass user=adList.get(position);
        if (user!=null){
            holder.vTitle.setText(user.getName());
            holder.vPrice.setText(user.getPhone());
            holder.vDescription.setText(user.getLocation());
        }
       // holder.image.setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder{
        TextView vTitle,vPrice,vDescription;
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.name);
            vPrice = itemView.findViewById(R.id.number);
            vDescription = itemView.findViewById(R.id.location);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equalsIgnoreCase("admin@animalhub.com")){
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Do You Want to Delete this User")
                                .setTitle("Delete")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String id = adList.get(getAdapterPosition()).getId();
                                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User");
                                        register_modelClass=adList.get(getAdapterPosition());
                                        register_modelClass.setDelete(true);
                                        dR.child(id).setValue(register_modelClass);

                                    }
                                }).setNegativeButton("No", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                    return true;

                }
            });
        }
    }
}
