package com.icedcap.remoteserver;

import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;

/**
 * Author: doushuqi
 * Date: 16-6-3
 * Email: shuqi.dou@singuloid.com
 * LastUpdateTime:
 * LastUpdateBy:
 */
public class RemoteServerService extends RemoteServerNative {
    private ArrayList<User> mUsers;

    public RemoteServerService(ArrayList<User> users) {
        mUsers = users;
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

    @Override
    public User searchUser(int id) throws RemoteException {
        if (mUsers != null && mUsers.size() > 0 && id < mUsers.size()) {
            Log.e("ddd", "------search-----------");
            return mUsers.get(id);
        }
        return null;
    }
}
