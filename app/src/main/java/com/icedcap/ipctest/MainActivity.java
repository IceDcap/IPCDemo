package com.icedcap.ipctest;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.icedcap.ipctest.server.RemoteService;
import com.icedcap.remoteserver.IRemoteServer;
import com.icedcap.remoteserver.RemoteServerService;
import com.icedcap.remoteserver.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ServiceConnection mConnection;
    private IRemoteServer mIRemoteServer;
    private EditText mA, mB, mInput;
    private TextView mResult, mSearchResult;
    private int a, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        init();
    }

    @Override
    protected void onResume() {
        bindService();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    private void init() {
        mResult = (TextView) findViewById(R.id.add_result);
        mSearchResult = (TextView) findViewById(R.id.search_result);
        mA = (EditText) findViewById(R.id.a);
        mB = (EditText) findViewById(R.id.b);
        mInput = (EditText) findViewById(R.id.key);
        findViewById(R.id.btn_search).setOnClickListener(this);

        mA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                a = "".equals(s.toString()) ? 0 : Integer.parseInt(s.toString());
                performAdd();
            }
        });
        mB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                b = "".equals(s.toString()) ? 0 : Integer.parseInt(s.toString());
                performAdd();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void bindService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIRemoteServer = RemoteServerService.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIRemoteServer = null;
            }
        };

        if (null == mIRemoteServer) {
//            startService(new Intent(this, RemoteService.class));
            Intent service = new Intent("com.icedcap.ipctest.START_SERVICE");
            service.setPackage("com.icedcap.ipctest");
            bindService(service, mConnection, Service.BIND_AUTO_CREATE);

        }
    }

    @Override
    public void onClick(View v) {
        String input = mInput.getText().toString();
        if (v.getId() == R.id.btn_search && !"".equals(input)) {
            int id = Integer.parseInt(input);
            try {
                User user = mIRemoteServer.searchUser(id);
                mSearchResult.setText(user == null ? "null" : user.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void performAdd() {
        try {
            mResult.setText(String.valueOf(mIRemoteServer.add(a, b)));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
