package com.koakh.androiddropsyncapi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFileSystem;

import com.koakh.androiddropsyncapi.R;
import com.koakh.androiddropsyncapi.app.Singleton;
import com.koakh.androiddropsyncapi.util.Dbx;

//Links
//Using the Sync API with Android Studio
//https://www.dropbox.com/developers/blog/57/using-the-sync-api-with-android-studio

public class MainActivity extends ActionBarActivity {

  //Request Codes
  private int REQUEST_LINK_TO_DBX = 0;//R.integer.REQUEST_LINK_TO_DBX;

  private Singleton mApp;

  //Class Members
  private Menu mMenu;
  private DbxAccountManager mDbxAcctMgr;
  private DbxFileSystem mDbxFs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Get Singleton
    mApp = Singleton.getInstance();
    //Init DropBox
    mApp.setDbx(new Dbx(this, getResources().getString(R.string.dropbox_appkey), getResources().getString(R.string.dropbox_appsecret)));
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_LINK_TO_DBX) {
      if (resultCode == Activity.RESULT_OK) {
        Log.i(mApp.TAG, "Start using Dropbox files");
        mApp.getDbx().getDbxFs();
      } else {
        Log.i(mApp.TAG, "Link failed or was cancelled by the user");
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);

    //get reference for menu
    this.mMenu = menu;
    //Enable menus if has DropBox Linked Account
    MenuItem menuItemCreateFile = mMenu.findItem(R.id.action_menu_create_file);
    if (mApp.getDbx() != null) menuItemCreateFile.setEnabled(mApp.getDbx().getDbxAcctMgr().hasLinkedAccount());

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id) {
      case R.id.action_menu_link:
        Log.i(mApp.TAG, "link");
        //mApp.getDbx().getDbxAcctMgr().startLink();
        break;
      case R.id.action_menu_unlink:
        Log.i(mApp.TAG, "unlink");
        mApp.getDbx().getDbxAcctMgr().unlink();
        break;
      case R.id.action_menu_create_file:
        Log.i(mApp.TAG, "create_file");
        mApp.getDbx().CreateTextFile("hello.txt", "Hello Dropbox!");
        break;
      case R.id.action_menu_download:
        Log.i(mApp.TAG, "download");
        break;
      case R.id.action_menu_upload:
        Log.i(mApp.TAG, "upload");
        break;
      case R.id.action_menu_settings:
        Log.i(mApp.TAG, "settings");
        break;
      case R.id.action_menu_about:
        Log.i(mApp.TAG, "about");
        break;
      case R.id.action_menu_quit:
        finish();
        break;
      default:
        Log.i(mApp.TAG, "default");
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
