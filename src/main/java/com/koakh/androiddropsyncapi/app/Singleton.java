package com.koakh.androiddropsyncapi.app;

import com.koakh.androiddropsyncapi.util.Dbx;

import java.util.HashMap;
import java.util.Map;

public class Singleton {

  //Singleton
  private static Singleton ourInstance = new Singleton();

  public static Singleton getInstance() {
    return ourInstance;
  }

  //Final
  public static final String TAG = "OMG";

  //Request Codes
  Map<String, String> mRequestCode = new HashMap<String, String>();

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
