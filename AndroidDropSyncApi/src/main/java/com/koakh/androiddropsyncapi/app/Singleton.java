package com.koakh.androiddropsyncapi.app;

import com.koakh.androiddropsyncapi.util.Dbx;

public class Singleton {

  //Singleton
  private static Singleton ourInstance = new Singleton();

  public static Singleton getInstance() {
    return ourInstance;
  }

  //Final
  public final String TAG = "AndroidDropSyncApi";

  //Request Codes
  //Map<String, String> mRequestCodes = new HashMap<String, String>();

  //Public Members
  private Dbx mDbx;

  public Dbx getDbx() {
    return mDbx;
  }

  public void setDbx(Dbx mDbx) {
    this.mDbx = mDbx;
  }

  private Singleton() {
  }
}
