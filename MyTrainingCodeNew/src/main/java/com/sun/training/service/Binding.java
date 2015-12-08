package com.sun.training.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.training.R;
import com.sun.training.service.aidl.IRemoteService;

public class Binding extends Activity {
    /**
     * The primary interface we will be calling on the service.
     */
    IRemoteService mService = null;

    Button mKillButton;
    TextView mCallbackText;

    private boolean mIsBound;
    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IRemoteService.Stub.asInterface(service);
            mKillButton.setEnabled(true);
            mCallbackText.setText("Attached.");

            // As part of the sample, tell the user what happened.
            Toast.makeText(Binding.this, "remote service connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mKillButton.setEnabled(false);
            mCallbackText.setText("Disconnected.");

            Toast.makeText(Binding.this, "remote service disconnected.", Toast.LENGTH_SHORT).show();
        }
    };
    private View.OnClickListener mBindListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Establish a couple connections with the service,

            Intent intent = new Intent(Binding.this, RemoteService.class);
            intent.setAction(IRemoteService.class.getName());
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

            mIsBound = true;
            mCallbackText.setText("Binding.");
        }
    };
    private View.OnClickListener mUnbindListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mIsBound) {
                unbindService(mConnection);
                mKillButton.setEnabled(false);
                mIsBound = false;
                mCallbackText.setText("Unbinding.");
            }
        }
    };
    private View.OnClickListener mKillListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // To kill the process hosting our service, we need to know its
            // PID. Conveniently our service has a call that will return
            // to us that information.
            if (mService != null) {
                try {
                    int pid = mService.getPid();
                    // Note that, though this API allows us to request to
                    // kill any process based on its PID, the kernel will
                    // still impose standard restrictions on which PIDs you
                    // are actually able to kill. Typically this means only
                    // the process running your application and any additional
                    // processes created by that app as shown here; packages
                    // sharing a common UID will also be able to kill each
                    // other's processes.
                    android.os.Process.killProcess(pid);
                    mCallbackText.setText("Killed service process.");
                } catch (RemoteException e) {
                    // Recover gracefully from the process hosting the
                    // service dying.
                    // Just for purposes of the sample, put up a notification.
                    Toast.makeText(Binding.this, "remote call failed", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_remote);

        Button button = (Button) findViewById(R.id.bind);
        button.setOnClickListener(mBindListener);
        button = (Button) findViewById(R.id.unbind);
        button.setOnClickListener(mUnbindListener);
        mKillButton = (Button) findViewById(R.id.kill);
        mKillButton.setOnClickListener(mKillListener);
        mKillButton.setEnabled(false);

        mCallbackText = (TextView) findViewById(R.id.callback);
        mCallbackText.setText("Not attached.");
    }
}
