package com.example.gaurav.auto_reply;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

public class Missedcallservice extends Service {

    public String profile;
    public String message;
    public String value;
    public String name;
    private static boolean ring=false;
    private static boolean callReceived=false;
    private String callerPhoneNumber;
    private Context saveContext;
    private static boolean missed=false;
    private static boolean send=false;
    ProfileDataBaseAdapter profiles;
    SettingsDataBaseAdapter settings;
    ContactsDataBaseAdapter contacts;
    String notificationTitle;
    String notificationContent;
    String tickerMessage;

    private final BroadcastReceiver receiver1 = new BroadcastReceiver() {


        @Override

        public void onReceive(Context mContext, Intent intent)
        {
            saveContext=mContext;
            // Get the current Phone State



            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);


            if(state==null){
                return;
            }

            Bundle bundle = intent.getExtras();
            String number = bundle.getString("incoming_number");

            Calendar calendar=Calendar.getInstance();
            long currentTimeStamp = calendar.getTimeInMillis();
            // If phone state "Rininging"
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                ring =true;
                // Get the Caller's Phone Number

            }



            // If incoming call is received
            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            {
                callReceived=true;
            }


            // If phone is Idle
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
            {
                // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
                if(ring==true&&callReceived==false)
                {
                    missed=true;
                    String x;
                    x = number.substring(number.length()-10,number.length());
                    name = contacts.getContact(profile,x);
                    Toast.makeText(mContext, "missed call : " + number, Toast.LENGTH_LONG).show();
                    //workingWithFunctions();
                    ring=false;

                    value = settings.getValue("sendto");


                    if (value.equals("all"))
                    {
                        send = true;
                    }

                    else if(name.equals("not exist"))
                        {
                            send = false;
                        }

                    else
                        {
                            send = true;
                        }

                }

                callReceived=false;



                if((missed==true)&&(send==true))
                {
                    Intent intent1 = new Intent(mContext, Missedcallservice.class);
                    PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent1, 0);


                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null,message, null, null);
                    notificationTitle = "Auto - Reply";
                    notificationContent = "Message sent to:" + number;
                    tickerMessage = "To" + number;

                    String value1 = settings.getValue("notification");
                    if(value1.equals("on")) {
                        Intent notificationIntent = new Intent(getApplicationContext(), NotificationView.class);
                        notificationIntent.putExtra("content", notificationContent);
                        notificationIntent.setData(Uri.parse("tel:/" + (int) System.currentTimeMillis()));
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
                        NotificationManager nManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setWhen(System.currentTimeMillis())
                                .setContentText(notificationContent)
                                .setContentTitle(notificationTitle)
                                .setSmallIcon(R.drawable.auto)
                                .setAutoCancel(true)
                                .setTicker(tickerMessage)
                                .setContentIntent(pendingIntent)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        Notification notification = notificationBuilder.build();
                        nManager.notify((int) System.currentTimeMillis(), notification);
                    }
                   // Toast.makeText(mContext, "Message Sent : "+ message,
                     //       Toast.LENGTH_LONG).show();

                    missed=false;
                    send=false;
                }
            }}

    };


    public int onStartCommand (Intent intent, int flags, int startId){

        profile = intent.getStringExtra("profile");
        message = profiles.getMessage(profile);



       return 0;
    }

    public void onCreate() {
        super.onCreate();
        profiles = new ProfileDataBaseAdapter(this);
        profiles = profiles.open();

        settings = new SettingsDataBaseAdapter(this);
        settings = settings.open();

        contacts = new ContactsDataBaseAdapter(this);
        contacts = contacts.open();
        //profile = intent.getStringExtra("profile");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(receiver1, filter);

    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


}
