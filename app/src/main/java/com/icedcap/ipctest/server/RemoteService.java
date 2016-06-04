package com.icedcap.ipctest.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.icedcap.remoteserver.RemoteServerService;
import com.icedcap.remoteserver.User;

import java.util.ArrayList;

/**
 * Author: doushuqi
 * Date: 16-6-3
 * Email: shuqi.dou@singuloid.com
 * LastUpdateTime:
 * LastUpdateBy:
 */
public class RemoteService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RemoteServerService(createTable());
    }

    private ArrayList<User> createTable() {
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User(i, "name " + i,
                    "address " + i, i + "-13833253767");
            list.add(user);
        }
        return list;
    }
}
