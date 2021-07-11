package com.example.animalhub.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;

import com.example.animalhub.Interface.OnAlertDialogButtonClickListener;
import com.example.animalhub.R;
import com.google.android.material.snackbar.Snackbar;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MyUtils {

    public static AlertDialog getLoadingDialog(Activity context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_loading_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();
        return alertDialog;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidUrl(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.WEB_URL.matcher(target).matches();
    }

    public final static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    public static AlertDialog getLoadingDialog(Activity context){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = context.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.progress_dialog_layout, null);
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
//        AlertDialog alertDialog = dialogBuilder.create();
//        return alertDialog;
//    }

    public static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title).setMessage(message).show();
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateFormatter(String time) {
        String inputPattern = "E MMM dd HH:mm:ss z yyyy";
        String outputPattern = "hh:mm:a dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            assert date != null;
            str = outputFormat.format(date);
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    applyFont(tv, context);
                    break;
                }
            }
        }
    }

    public static void applyFont(TextView tv, Activity context) {
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/opensans.ttf"));
    }


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

    public static boolean isGPSEnabled(Activity context){
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    public static boolean isNetworkEnabled(Activity context){
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = false;

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }
        return network_enabled;
    }

}
