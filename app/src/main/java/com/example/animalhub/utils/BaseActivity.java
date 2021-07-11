package com.example.animalhub.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.animalhub.Interface.OnAlertDialogButtonClickListener;
import com.example.animalhub.R;


public class BaseActivity extends AppCompatActivity {

    public static void showDialogToTurnOnLocation(final Context context, final OnAlertDialogButtonClickListener onAlertDialogButtonClickListener){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final String message = "Enable either GPS or any other location"
                + " service to find current location.  Click OK to go to"
                + " location services settings to let you do so.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                onAlertDialogButtonClickListener.onPositiveButtonClick(d);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                onAlertDialogButtonClickListener.onNegativeButtonClick(d);
                            }
                        });

        final AlertDialog dialog=builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAppTextLight));
            }
        });

        dialog.show();
    }

    public boolean isGPSEnabled(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    public boolean isNetworkEnabled(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = false;

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }
        return network_enabled;
    }

    public void goToActivity(Class<?> activityClass, int flags, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(flags);
        if (bundle!=null){
            intent.putExtra(Constants.DataConstant.BUNDLE,bundle);
        }
        startActivity(intent);
    }

    public void goToActivity(Class<?> activityClass) {
        goToActivity(activityClass, 0,null);
    }

    public void goToActivity(Class<?> activityClass,int flag) {
        goToActivity(activityClass, flag,null);
    }

    public void goToActivity(Class<?> activityClass,Bundle bundle) {
        goToActivity(activityClass, 0,bundle);
    }

    public void showOkAlert(Context context,Object title, Object message, DialogInterface.OnClickListener onClick) {
        String strTitle = (title instanceof Integer) ?
                getResources().getString((Integer)title) : (String)title;

        String strMessage = (message instanceof Integer) ?
                getResources().getString((Integer)message) : (String)message;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setPositiveButton(R.string.button_ok, onClick);

        AlertDialog dialog=builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAppText));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAppTextLight));
            }
        });

        dialog.show();
    }

    public void showCancelableAlert(Context context,Object title, Object message, DialogInterface.OnClickListener onClick) {
        showCancelableAlert(context, title, message, getString(R.string.button_yes), getString(R.string.button_cancel), onClick);
    }


    public void showCancelableAlert(Context context,Object title, Object message, String positiveBtnText,
                                    String negativeBtnText, DialogInterface.OnClickListener onClick) {
        String strTitle = (title instanceof Integer) ?
                getResources().getString((Integer)title) : (String)title;

        String strMessage = (message instanceof Integer) ?
                getResources().getString((Integer)message) : (String)message;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setPositiveButton(positiveBtnText, onClick);
        builder.setNegativeButton(negativeBtnText, onClick);

        AlertDialog dialog=builder.create();
        dialog.setOnShowListener(arg0 -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAppText));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAppTextLight));
        });

        dialog.show();
    }

    public void showAlert(Context context,Object title, Object message, DialogInterface.OnClickListener onClick) {
        String strTitle = (title instanceof Integer) ?
                getResources().getString((Integer)title) : (String)title;

        String strMessage = (message instanceof Integer) ?
                getResources().getString((Integer)message) : (String)message;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setPositiveButton(R.string.button_ok, onClick);
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog=builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAppText));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAppTextLight));
            }
        });

        dialog.show();
    }






    public String getStringFromResource(int id){
        return getResources().getString(id);
    }

    public void hideSoftKeyboard(){

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow( getCurrentFocus().getWindowToken(), 0);
    }

}

