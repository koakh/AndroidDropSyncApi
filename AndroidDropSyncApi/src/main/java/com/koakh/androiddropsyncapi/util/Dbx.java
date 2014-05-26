package com.koakh.androiddropsyncapi.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.koakh.androiddropsyncapi.app.Singleton;
import com.koakh.androiddropsyncapi.R;

import java.io.IOException;

public class Dbx {

  //Request Codes
  private int REQUEST_LINK_TO_DBX;

  //Private Members
  private Activity mActivity;
  private Context mContext;
  private Singleton mApp;

  //Public Members
  private DbxAccountManager mAccountManager;

  public DbxAccountManager getAccountManager() {
    return mAccountManager;
  }

  private DbxFileSystem mFileSystem;

  public DbxFileSystem getFileSystem() {
    if (mFileSystem.isShutDown()) InitFileSystem();
    return mFileSystem;
  }

  //Constructor
  public Dbx(Activity pActivity) {

    //Assign Parameters
    mActivity = pActivity;
    mContext = pActivity.getApplicationContext();

    //Get Singleton
    mApp = Singleton.getInstance();

    //Get Request Codes
    REQUEST_LINK_TO_DBX = mContext.getResources().getInteger(R.integer.REQUEST_LINK_TO_DBX);

    //Get Account Manager and init FileSystem
    InitAccountManager(
      mContext.getResources().getString(R.string.dropbox_appkey),
      mContext.getResources().getString(R.string.dropbox_appsecret)
    );
  }

  //Util Methods
  private void InitAccountManager(String pAppKey, String pAppSecret) {
    try {
      //Linking accounts
      mAccountManager = DbxAccountManager.getInstance(mContext, pAppKey, pAppSecret);

      //I is linked to a DropBox account, Get FileSystem
      if (mAccountManager.hasLinkedAccount()) {
        InitFileSystem();
        //Else Request Link
      } else {
        mAccountManager.startLink(mActivity, REQUEST_LINK_TO_DBX);
      }
    } catch (DbxAccountManager.ConfigurationMismatchException e) {
      Log.e(mApp.TAG, e.getMessage());
    }
  }

  //Public Methods
  public void InitFileSystem() {
    try {
      mFileSystem = DbxFileSystem.forAccount(mAccountManager.getLinkedAccount());
    } catch (DbxException e) {
      Log.e(mApp.TAG, e.getMessage());
    }
  }

  public boolean CreateTextFile(String pFile, String pContent) {
    try {
      DbxPath testPath = new DbxPath(DbxPath.ROOT, pFile);
      if (!mFileSystem.exists(testPath)) {

        DbxFile testFile = mFileSystem.create(testPath);
        try {
          testFile.writeString(pContent);
          return true;
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          testFile.close();
        }
      } else {
        Log.i(mApp.TAG, String.format("File %s Already Exists", testPath.getName()));
      }
    } catch (DbxException e) {
      Log.e(mApp.TAG, e.getMessage());
    }
    return false;
  }
}
