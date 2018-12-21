package com.example.wws.msgclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    private Messenger mServerMessenger;

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d("test", "binderDied: ");
            if(mServerMessenger == null){
                return;
            }
            mServerMessenger.getBinder().unlinkToDeath(mDeathRecipient, 0);
            mServerMessenger = null;
        }
    };


    private ServiceConnection mMessengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            Log.d("test", "onServiceConnected: ");
            mServerMessenger = new Messenger(service);
            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            Log.d("test", "onServiceDisconnected: ");
            mServerMessenger = null;
//            Intent intent = new Intent();
//            intent.setAction("com.wws.messenger");
//            intent.setPackage("com.example.wws.msgserver");
//            startService(intent);

//            bindService(intent, mMessengerConnection, Context.BIND_AUTO_CREATE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setAction("com.wws.messenger");
        intent.setPackage("com.example.wws.msgserver");
//        bindService(intent, mMessengerConnection, Context.BIND_AUTO_CREATE);

        startService(intent);
//        startForegroundService(intent);
        
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = Message.obtain(null, 1);
                try {
                    mServerMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
