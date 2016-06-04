package com.icedcap.remoteserver;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/**
 * Author: doushuqi
 * Date: 16-6-3
 * Email: shuqi.dou@singuloid.com
 * LastUpdateTime:
 * LastUpdateBy:
 */
public interface IRemoteServer extends IInterface {
    public static String DESCRIPTOR = "com.icedcap.remoteserver.IRemoteServer";
    public static int TRANSACTION_ADD = IBinder.FIRST_CALL_TRANSACTION + 1;
    public static int TRANSACTION_SEARCH_USER = IBinder.FIRST_CALL_TRANSACTION + 2;

    public int add(int a, int b) throws RemoteException;

    public User searchUser(int id) throws RemoteException;
}
