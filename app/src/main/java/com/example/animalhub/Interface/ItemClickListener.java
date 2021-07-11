package com.example.animalhub.Interface;

import android.view.View;

public interface ItemClickListener {
    void OnClick(View view, int position, boolean isLongClick);

    void onLongClick(View view, int position,boolean isLongClick);


}
