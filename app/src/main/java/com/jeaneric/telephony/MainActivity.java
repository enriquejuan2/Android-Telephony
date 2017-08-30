package com.jeaneric.telephony;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;

    private static final int PERMISSIONS_REQUEST_STATE_PHONE = 1;
    private boolean mCallPermissionGranted;

    private static final int PERMISSIONS_REQUEST_SMS = 2;
    public boolean mSmsPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        getDeviceName();

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            mCallPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_STATE_PHONE);
        }
        if (mCallPermissionGranted) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            //Calling the methods of TelephonyManager the returns the information
            String IMEINumber = tm.getDeviceId();
            String subscriberID = tm.getDeviceId();
            String SIMSerialNumber = tm.getSimSerialNumber();
            String networkCountryISO = tm.getNetworkCountryIso();
            String SIMCountryISO = tm.getSimCountryIso();
            String softwareVersion = tm.getDeviceSoftwareVersion();
            String voiceMailNumber = tm.getVoiceMailNumber();
            String mPhoneNumber = tm.getLine1Number();
            String carrierName = tm.getNetworkOperatorName();

            //Get the phone type
            String strphoneType = "";

            int phoneType = tm.getPhoneType();

            switch (phoneType) {
                case (TelephonyManager.PHONE_TYPE_CDMA):
                    strphoneType = "CDMA";
                    break;
                case (TelephonyManager.PHONE_TYPE_GSM):
                    strphoneType = "GSM";
                    break;
                case (TelephonyManager.PHONE_TYPE_NONE):
                    strphoneType = "NONE";
                    break;
            }

            //getting information if phone is in roaming
            boolean isRoaming = tm.isNetworkRoaming();

            String info = "Phone Details:\n";
            info += "\n IMEI Number:" + IMEINumber;
            info += "\n SubscriberID:" + subscriberID;
            info += "\n Sim Serial Number:" + SIMSerialNumber;
            info += "\n Network Country ISO:" + networkCountryISO;
            info += "\n SIM Country ISO:" + SIMCountryISO;
            info += "\n Software Version:" + softwareVersion;
            info += "\n Voice Mail Number:" + voiceMailNumber;
            info += "\n Phone Network Type:" + strphoneType;
            info += "\n In Roaming? :" + isRoaming;
            info += "\n phone number :" + mPhoneNumber;
            info += "\n Carrier name :" + carrierName;

            textView1.setText(info);//displaying the information in the textView


        }

        textView3.setText("Phone Model: " + getDeviceName());

    // public static final String INBOX = "content://sms/inbox";
// public static final String SENT = "content://sms/sent";
// public static final String DRAFT = "content://sms/draft";

        if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_SMS)==PackageManager.PERMISSION_GRANTED)

    {
        mSmsPermissionGranted = true;
    } else

    {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_SMS},
                PERMISSIONS_REQUEST_SMS);
    }
        if(mSmsPermissionGranted){

      /*  Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + " : " + cursor.getString(idx);
                }
                // use msgData
                textView2.setText(msgData);
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        } */
            StringBuilder smsBuilder = new StringBuilder();
            final String SMS_URI_INBOX = "content://sms/inbox";
            final String SMS_URI_ALL = "content://sms/";

                Uri uri = Uri.parse(SMS_URI_INBOX);
                String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
                Cursor cur = getContentResolver().query(uri, projection, "address='MPESA'", null, "date desc");
                if (cur.moveToFirst()) {
                    int index_Address = cur.getColumnIndex("address");
                    int index_Person = cur.getColumnIndex("person");
                    int index_Body = cur.getColumnIndex("body");
                    int index_Date = cur.getColumnIndex("date");
                    int index_Type = cur.getColumnIndex("type");
                    do {
                        String strAddress = cur.getString(index_Address);
                        int intPerson = cur.getInt(index_Person);
                        String strbody = cur.getString(index_Body);
                        long longDate = cur.getLong(index_Date);
                        int int_Type = cur.getInt(index_Type);

                        smsBuilder.append("[ ");
                        smsBuilder.append(strAddress + ", ");
                        smsBuilder.append(intPerson + ", ");
                        smsBuilder.append(strbody + ", ");
                        smsBuilder.append(longDate + ", ");
                        smsBuilder.append(int_Type);
                        smsBuilder.append(" ]\n\n");

                        textView2.setText(smsBuilder);
                        Log.d(TAG, "smsBuilder: "+ smsBuilder );
                    } while (cur.moveToNext());

                    if (!cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                } else {
                    smsBuilder.append("no result!");
                } // end if
            StringBuilder smsBuilderTala = new StringBuilder();
            final String SMS_URI_INBOX_TALA = "content://sms/inbox";


            Uri uritala = Uri.parse(SMS_URI_INBOX_TALA);
            String[] projectiontala = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor curtala = getContentResolver().query(uritala, projectiontala, "address='21991'", null, "date desc");
            if (curtala.moveToFirst()) {
                int index_Address = curtala.getColumnIndex("address");
                int index_Person = curtala.getColumnIndex("person");
                int index_Body = curtala.getColumnIndex("body");
                int index_Date = curtala.getColumnIndex("date");
                int index_Type = curtala.getColumnIndex("type");
                do {
                    String strAddress = curtala.getString(index_Address);
                    int intPerson = curtala.getInt(index_Person);
                    String strbody = curtala.getString(index_Body);
                    long longDate = curtala.getLong(index_Date);
                    int int_Type = curtala.getInt(index_Type);

                    smsBuilderTala.append("[ ");
                    smsBuilderTala.append(strAddress + ", ");
                    smsBuilderTala.append(intPerson + ", ");
                    smsBuilderTala.append(strbody + ", ");
                    smsBuilderTala.append(longDate + ", ");
                    smsBuilderTala.append(int_Type);
                    smsBuilderTala.append(" ]\n\n");

                    textView4.setText(smsBuilderTala);
                    Log.d(TAG, "smsBuilder: "+ smsBuilderTala );
                } while (curtala.moveToNext());

                if (!curtala.isClosed()) {
                    curtala.close();
                    curtala = null;
                }
            } else {
                smsBuilderTala.append("no result!");
            } // end if
            StringBuilder smsBuilderKcb = new StringBuilder();
            final String SMS_URI_INBOX_KCB = "content://sms/inbox";


            Uri uriKcb = Uri.parse(SMS_URI_INBOX_KCB);
            String[] projectionKcb = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor curKcb = getContentResolver().query(uriKcb, projectionKcb, "address='Kcb MPESA'", null, "date desc");
            if (curKcb.moveToFirst()) {
                int index_Address = curKcb.getColumnIndex("address");
                int index_Person = curKcb.getColumnIndex("person");
                int index_Body = curKcb.getColumnIndex("body");
                int index_Date = curKcb.getColumnIndex("date");
                int index_Type = curKcb.getColumnIndex("type");
                do {
                    String strAddress = curKcb.getString(index_Address);
                    int intPerson = curKcb.getInt(index_Person);
                    String strbody = curKcb.getString(index_Body);
                    long longDate = curKcb.getLong(index_Date);
                    int int_Type = curKcb.getInt(index_Type);

                    smsBuilderKcb.append("[ ");
                    smsBuilderKcb.append(strAddress + ", ");
                    smsBuilderKcb.append(intPerson + ", ");
                    smsBuilderKcb.append(strbody + ", ");
                    smsBuilderKcb.append(longDate + ", ");
                    smsBuilderKcb.append(int_Type);
                    smsBuilderKcb.append(" ]\n\n");

                    textView5.setText(smsBuilderKcb);
                    Log.d(TAG, "smsBuilder: "+ smsBuilderKcb );
                } while (curKcb.moveToNext());

                if (!curKcb.isClosed()) {
                    curKcb.close();
                    curKcb = null;
                }
            } else {
                smsBuilderKcb.append("no result!");
            } // end if
            StringBuilder smsBuilderBranch = new StringBuilder();
            final String SMS_URI_INBOX_BRANCH = "content://sms/inbox";


            Uri uriBr = Uri.parse(SMS_URI_INBOX_BRANCH);
            String[] projectionBr = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor curBr = getContentResolver().query(uriBr, projectionBr, "address='Branch Co'", null, "date desc");
            if (curBr.moveToFirst()) {
                int index_Address = curBr.getColumnIndex("address");
                int index_Person = curBr.getColumnIndex("person");
                int index_Body = curBr.getColumnIndex("body");
                int index_Date = curBr.getColumnIndex("date");
                int index_Type = curBr.getColumnIndex("type");
                do {
                    String strAddress = curBr.getString(index_Address);
                    int intPerson = curBr.getInt(index_Person);
                    String strbody = curBr.getString(index_Body);
                    long longDate = curBr.getLong(index_Date);
                    int int_Type = curBr.getInt(index_Type);

                    smsBuilderBranch.append("[ ");
                    smsBuilderBranch.append(strAddress + ", ");
                    smsBuilderBranch.append(intPerson + ", ");
                    smsBuilderBranch.append(strbody + ", ");
                    smsBuilderBranch.append(longDate + ", ");
                    smsBuilderBranch.append(int_Type);
                    smsBuilderBranch.append(" ]\n\n");

                    textView6.setText(smsBuilderBranch);
                    Log.d(TAG, "smsBuilder: "+ smsBuilderBranch );
                } while (curBr.moveToNext());

                if (!curBr.isClosed()) {
                    curBr.close();
                    curBr = null;
                }
            } else {
                smsBuilderBranch.append("no result!");
            } // end if
            }

        //Get the instance of TelephonyManager
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}


