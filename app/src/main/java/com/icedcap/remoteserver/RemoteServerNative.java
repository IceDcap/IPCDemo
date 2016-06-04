package com.icedcap.remoteserver;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

/**
 * Author: doushuqi
 * Date: 16-6-3
 * Email: shuqi.dou@singuloid.com
 * LastUpdateTime:
 * LastUpdateBy:
 */
public abstract class RemoteServerNative extends Binder implements IRemoteServer {

    public RemoteServerNative() {
        attachInterface(this, DESCRIPTOR);
    }

    public static IRemoteServer asInterface(IBinder obj) {
        if (null == obj) {
            return null;
        }
        IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
        if (null != iInterface && iInterface instanceof IRemoteServer) {
            Log.e("ddd", "use the local binder");
            return (IRemoteServer) iInterface;
        }
        Log.e("ddd", "use the proxy binder");
        return new RemoteServerProxy(obj);
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        Log.e("ddd", "-------onTransact---------------");
        switch (code) {
            case TRANSACTION_ADD:
                data.enforceInterface(DESCRIPTOR);
                int a = data.readInt();
                int b = data.readInt();
                int result = this.add(a, b);
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case TRANSACTION_SEARCH_USER:
                data.enforceInterface(DESCRIPTOR);
                int id = data.readInt();
                User u = this.searchUser(id);
                reply.writeNoException();
                if (null != u) {
                    reply.writeInt(1);
                    u.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }
}

class RemoteServerProxy implements IRemoteServer {
    private IBinder mRemote;

    public RemoteServerProxy(IBinder IBinder) {
        mRemote = IBinder;
        Log.e("ddd", "mRemote >>> " + mRemote);
    }

    public IBinder asBinder() {
        return mRemote;
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(DESCRIPTOR);
        data.writeInt(a);
        data.writeInt(b);
        mRemote.transact(TRANSACTION_ADD, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        data.recycle();
        reply.recycle();
        return result;
    }

    @Override
    public User searchUser(int id) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(DESCRIPTOR);
        data.writeInt(id);
        mRemote.transact(TRANSACTION_SEARCH_USER, data, reply, 0);
        Log.e("ddd", "proxy->searchUser-----------");
        reply.readException();
        User user = null;
        if (reply.readInt() != 0) {
            user = User.CREATOR.createFromParcel(reply);
        }
        data.recycle();
        reply.recycle();
        return user;
    }
}
