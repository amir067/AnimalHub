package com.example.animalhub.utils;

import android.Manifest;

public class Constants {

    public static int total_scores;

    public static int nature_score;
    public static int accessories_score;
    public static int pets_score;
    public static int places_score;
    public static int colors_score;
    public static int statement_score;
    public static String ADMIN_EMAIL="admin@animalhub.com";


    public static class DataConstant{
        public static String BUNDLE="bundle";
        public static String RECORDING_TYPE="type";
        public static String RECORDINGS="recordings";
        public static String IS_FROM_SIGNUP="isFromSignup";
        public static String RECORDING_PATH="path";
        public static String CIRCLE="circle";
        public static String CIRCLE_ID="circleId";
        public static String NOTIFICATION="notification";
        public static String TAB="tab";
        public static String SHOW_CIRCLE_DETAIL="show_circle_detail";
        public static String ALL_CIRCLE="allCircle";
        public static String CIRCLE_TYPE="circleType";
        public static String NAME="name";
        public static String CONTACT="contact";
        public static String CHANNEL="channel";
        public static String OTP="OTP";
        public static String PARAMS="params";
        public static String UPDATE_PROFILE_PARAM="updateProfileParam";
        public static String NUMBER_VERIFICATION_RESPONSE="numberVerificationResponse";
        public static String USER="User";
        public static String DEFAULT_REC_TIME="00:00";
        public static final String PROFILE_PICTURE_FILE_NAME = "reddot_profile.jpeg";
        public static final String DEVICE_TYPE = "android";
        public static final String TITLE = "toolbar_title";
        public static final String URL = "URL";
        public static final String OPEN_CHAT = "openChat";


        //NotificationParam keys
        public static final String NOTIFICATION_EMERGENCY_ALERT_TYPE="emergencyAlertType";
        public static final String NOTIFICATION_TYPE="notificationType";
        public static final String NOTIFICATION_USER_ID="userId";
        public static final String NOTIFICATION_ALERT_MESSAGE="alertMessage";
        public static final String NOTIFICATION_REFERRAL_CODE="referralCode";
        public static final String NOTIFICATION_USER_NAME="userName";
        public static final String NOTIFICATION_USER_EMERGENCY_TEXT="emergencyText";

        public static final String NOTIFICATION_CIRCLE_ID="circleId";
        public static final String NOTIFICATION_CIRCLE_NAME="circleName";



        public static String DEFAULT_SERVER_URL="http://reddotapp.co/protectify/api";

        public static String PRIVACY_POLICY_URL=DEFAULT_SERVER_URL+"/privacy/";
        public static String TERMS_CONDITION_URL=DEFAULT_SERVER_URL+"/tos/";
        public static String REFERRAL_CODE_DEEP_LINK ="reddot://referral/";

        public static String ANDROID ="android";

        public static final String SUICIDE_PREVENTION_NUMBER = "800-273-8255";

        public static String IS_FOR_Personal_Desc_Info="IsForPersonalDescInfo";
        public static String IS_FOR_Personal_Desc_Info_Edit="IsForPersonalDescInfoEdit";
        public static String IS_FOR_Medical_History="IsForMedicalHistory";
        public static String IS_FOR_Edit_Pin="IsForEditPin";
    }

    public static class AppPermission{
        public static String FINE_LOCATION= Manifest.permission.ACCESS_FINE_LOCATION;
        public static String COARSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
        public static String MAKE_PHONE_CALL=Manifest.permission.CALL_PHONE;
        public static String READ_EXTERNAL_STORAGE=Manifest.permission.READ_EXTERNAL_STORAGE;
        public static String WRITE_EXTERNAL_STORAGE=Manifest.permission.WRITE_EXTERNAL_STORAGE;
        public static String RECORD_AUDIO=Manifest.permission.RECORD_AUDIO;
        public static String CAMERA=Manifest.permission.CAMERA;
    }

    public static class SubscriptionSkuIDs{
        public static String SKU_UNLIMITED_CONTACTS_MONTHLY_SUB = "unlimitedcontacts";
    }

    public static class APIKey{
        public static String API_KEY_PART_1 = "AIzaSyB4AL";
        public static String API_KEY_PART_2 = "bMGLO05tsm";
        public static String API_KEY_PART_3 = "SZVvTnJyt";
        public static String API_KEY_PART_4 = "AMcLdid8G4";
    }

    public static class placesApiConstant{
        public static double NEAR_BY_LOCATION_RADIUS_DOCTOR = 7000;
        public static double NEAR_BY_LOCATION_RADIUS_HOSPITAL = 7000;
        public static double NEAR_BY_LOCATION_RADIUS_FIRE_STATION = 10000;
        public static double NEAR_BY_LOCATION_RADIUS_POLICE = 10000;

        public static String FIRE_STATION="fire_station";
        public static String HOSPITAL="hospital";
        public static String DOCTOR="doctor";
        public static String POLICE="police";

        public static String[] typeList={FIRE_STATION,DOCTOR,HOSPITAL,POLICE};
    }

    public static class Title{
        public static final String PROTECTIFY ="Protectify";
        public static final String EMERGENCY ="Emergency" ;
        public static final String PROFILE ="Profile" ;
        public static final String CIRCLES_IN_DANGER ="Circles In Danger" ;
        public static final String NOTIFICATION = "Notifications";
        public static String CIRCLE_LIST="Circles";
        //        public static String CHATS="Circles";
        public static String CHATS="Chats";
        public static String CONFIRMATION="Confirmation";
        public static String CONFIRMATION_MSG="Confirmation message";
        public static String DO_YOU_WANT_TO_CALL="Do you want to call ";
        public static String NO_CONNECTION="Connection Failed";
        public static String ERROR="Error";
        public static String CONGRATULATIONS="Congratulations!";
        public static String TERMS_CONDITIONS="Terms & Conditions";
        public static String PRIVACY_POLICY="Privacy Policy";
        public static String ALERT="Alert";
        public static String SYNCING_DATA="Syncing Data";
    }

    public static class FragmentTag{
        public static String INVITE_TO_CIRCLE="InviteToCircle";
        public static String PHONE_NUMBER_FRAGMENT="PhoneNumberDialog";
    }

    public static class Message{
        public static String SILENT_ALARM_ALREADY_SENT="You have recently sent Hospital alert.";
        public static String FIRE_ALARM_ALREADY_SENT="You recently sent a Fire alert";
        public static String POLICE_ALARM_ALREADY_SENT="You recently sent a Police alert";
        public static String EMERGENCY_ALARM_ALREADY_SENT="You recently sent a Emergency alert";
        public static String CANNOT_SEND_ALERT_WITHOUT_INTERNET="Can not sent alert without active internet connection.Check your internet connection and try again";
        public static String MARK_ME_SAFE="Mark me safe!";
        public static String SAFETY_CONFIRMATION="Are you sure you are safe?";
        public static String LOGOUT_CONFIRMATION_TITLE="Log-out";
        public static String LOGOUT_CONFIRMATION="Are you sure you want to log out of Protectify?";
        public static String REMOVE_ACCOUNT_CONFIRMATION="Are you sure you want to remove your account?";
        public static String JOIN_CIRCLE_SUCCESS="You have successfully joined the circle.";
        public static String ACCOUNT_DEACTIVATED_SUCCESS="Account Deactivated Sucessfully";
        public static String ACCOUNT_ALREADY_DEACTIVATED="Account Deactivated";
        public static String SENT="sent";
        public static String GENERIC_ERROR="Something went wrong. Please try again";
        public static String INVALID_NUMBER="Please provide valid phone number to proceed.";
        public static String SUCCESS_MESSAGE="Success";
        public static String USER_IS_IN_DANGER_MESSAGE="User is InDanger";
        public static String USER_IS_SAFE_MESSAGE="User is Safe";
        public static String SUCCESS="Success";
        public static String DONE="Done";
        public static String MARKED_SAFE_SUCCESSFULLY="You have been marked SAFE successfully";
        public static String MARKED_SAFE_SUCCESSFULLY_DIALOG="You have successfully mark yourself safe";
        public static String UNABLE_TO_LOGOUT="Unable logout!";

        //For Dialog
        public static String ENTERED_WRONG_DIGITS="You entered wrong digits";

        public static String SAVE_CHANGES_CONFIRMATION="You have some unsaved changes.Do you want to save them?";
        public static String NO_PHONE_NUMBER_TO_CALL="no phone number available to call";
        public static String NO_PHONE_NUMBER_TO_MESSAGE="no phone number available to message";

        public static String SHARE_STRING_PREFIX="Dear User,<br>";
        public static String SHARE_STRING_MID=" invited you to join the circle ";
        public static String SHARE_STRING_POSTFIX=". If you want to join please copy this code and paste it in the Protectify App ";
        public static String SHARE_STRING_DOWNLOAD_LINKS="Download Protectify App" +
                "<br>Play Store: https://bit.ly/36JaMVc" +
                "<br>App Store: https://apple.co/3iCma7P ";
        public static String SHARE_STRING_REGARDS="Regards,<br>Protectify.";
        public static String GENERIC_EMERGENCY_MESSAGE="I am in an emergency.Please help me!";
        public static String SAFE_MESSAGE="I am safe now.";
        public static String SYNCING_DATA_MESSAGE="Syncing data from server. Please try again later after a few seconds";

    }

    public static class editTextErrorMessage{
        public static String NAME_ERROR="Please enter your name to proceed.";
        public static String FIRST_NAME_ERROR="Please enter your first name to proceed.";
        public static String LAST_NAME_ERROR="Please enter your last name to proceed.";
        public static String EMAIL_ERROR="Please enter valid email address to proceed.";
        public static String PASSWORD_ERROR="Please provide password to proceed.";
        public static String PASSWORD_LENGTH_ERROR="Password must contain at least 6 characters.";
        public static String OLD_PASSWORD_ERROR="Please provide old password to proceed.";
        public static String CONFIRM_PASSWORD_ERROR="Please confirm password to proceed.";
        public static String MATCH_PASSWORD_ERROR="Password and confirm password must be same.";
        public static String PHONE_NUMBER_EMPTY_ERROR="Please provide your phone number to proceed.";
        public static String PHONE_NUMBER_IS_ALREADY_REGISTERED_ERROR="Number already registered";
        public static String VERIFY_PHONE_NUMBER_500_ERROR="500 Internal Server Error";
        public static String EMAIL_NOT_REGISTERED_401_ERROR="401 Unauthorized";
        //        public static String REFERRAL_CODE_ERROR="Please enter a valid referral to proceed";
        public static String MISSING_VALUES="Missing Values";
        public static String REFERRAL_CODE_ERROR="Please provide Invitation Code to proceed.";
        public static String WEIGHT_ERROR="Enter weight to proceed.";
        public static String HEIGHT_ERROR="Enter height to proceed.";
    }

    public static class tab{
        public static int EMERGENCY_TAB=0;
        public static int PROFILE_TAB=1;
        public static int EMERGENCY_ALERT_TAB=2;
        public static int SEE_ALL_TAB=3;

        public static int CREATED_CIRCLE=1;
        public static int JOINED_CIRCLE=2;
    }

    public static class deviceManufacturer{
        public static String SAMSUNG="samsung";
        public static String GOOGLE="Google";
    }

}
