package com.koakh.androiddropsyncapi.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.koakh.omgandroid.R;
import com.koakh.androiddropsyncapi.app.Singleton;

import java.io.IOException;

/**
 * Created by mario on 19/05/2014.
 */

public class Dbx {

  //Request Codes
  private int REQUEST_LINK_TO_DBX = 0;//R.integer.REQUEST_LINK_TO_DBX;

  //Private Members
  private Activity mActivity;
  private Context mContext;
  private Singleton mApp;

  //Public Members
  private DbxAccountManager mDbxAcctMgr;
  public DbxAccountManager getDbxAcctMgr() {
    return mDbxAcctMgr;
  }

  private DbxFileSystem mDbxFs;
  public DbxFileSystem getDbxFs() {
    return mDbxFs;
  }

  public Dbx(Activity pActivity, String pAppkey, String pAppSecret) {
    //Assign Parameters
    mActivity = pActivity;
    mContext = pActivity.getApplicationContext();
    //Get Singleton
    mApp = Singleton.getInstance();
    //Get Account Manager and init FileSystem
    if (InitAccountManager(pAppkey, pAppSecret)) InitFileSystem();
  }

  //Util Methods
  private boolean InitAccountManager(String pAppkey, String pAppSecret) {
    try {
      //Linking accounts
      mDbxAcctMgr = DbxAccountManager.getInstance(
        mContext, mContext.getResources().getString(R.string.dropbox_appkey),
        mContext.getResources().getString(R.string.dropbox_appsecret)
      );

      //Returns whether this application is linked to a Dropbox account.
      if (mDbxAcctMgr.hasLinkedAccount()) {
        InitFileSystem();
      } else {
        mDbxAcctMgr.startLink(mActivity, REQUEST_LINK_TO_DBX);
      }
      return true;
    } catch (DbxAccountManager.ConfigurationMismatchException e) {
      Log.e(mApp.TAG, e.getMessage());
      return false;
    }
  }

  private void InitFileSystem() {
    if (mDbxAcctMgr.hasLinkedAccount())
    {
      try {
        mDbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
      } catch (DbxException e) {
        Log.e(mApp.TAG, e.getMessage());
      }
    }
    Log.i(mApp.TAG, "Error calling InitFileSystem: Create Linked Account First");
  }

  public boolean CreateTextFile(String pFile, String pContent) {
    try {
      DbxFile testFile = mDbxFs.create(new DbxPath(pFile));
      try {
        testFile.writeString(pContent);
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      } finally {
        testFile.close();
        return true;
      }
    } catch (DbxException e) {
      Log.e(mApp.TAG, e.getMessage());
      return false;
    }
  }
}
