package com.koakh.androiddropsyncapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.koakh.androiddropsyncapi.app.Singleton;
import com.koakh.androiddropsyncapi.util.Dbx;

public class MainActivity extends ActionBarActivity {

  //get App Singleton
  private Singleton mApp;
  //Store Menu Reference
  private Menu mMenu;
  //Store SubMenu References
  private MenuItem mMenuItemLink;
  private MenuItem mMenuItemUnlink;
  private MenuItem mMenuItemCreateFile;
  //Request Codes
  private int REQUEST_LINK_TO_DBX;

  //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  //LiveCycle

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Get Request Codes
    REQUEST_LINK_TO_DBX = getResources().getInteger(R.integer.REQUEST_LINK_TO_DBX);

    //Get Singleton
    mApp = Singleton.getInstance();

    //Init DropBox
    if (mApp.getDbx() == null) {
      Toast.makeText(this, "Initialized Singleton Dropbox.", Toast.LENGTH_SHORT).show();
      mApp.setDbx(new Dbx(this));
    }
    else {
      Toast.makeText(this, "Singleton Dropbox already Initialized.", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  //SaveState

  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    //savedInstanceState.putString("dbx_userid", mApp.getDbx().getAccountManager().getLinkedAccount().getUserId());
    Toast.makeText(this, "onSaveInstanceState", Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    //String dbx_userid = savedInstanceState.getString("dbx_userid");
    Toast.makeText(this, "onRestoreInstanceState", Toast.LENGTH_SHORT).show();
  }

  //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  //ActivityResult

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_LINK_TO_DBX) {
      if (resultCode == Activity.RESULT_OK) {
        Log.i(mApp.TAG, "Start using Dropbox files");
        mApp.getDbx().getFileSystem();
      } else {
        Log.i(mApp.TAG, "Link failed or was cancelled by the user");
      }
      UpdateMenu();
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  //Menu

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);

    //get reference for menu
    this.mMenu = menu;
    //Get References
    this.mMenuItemLink = mMenu.findItem(R.id.action_menu_link);
    this.mMenuItemUnlink = mMenu.findItem(R.id.action_menu_unlink);
    this.mMenuItemCreateFile = mMenu.findItem(R.id.action_menu_create_file);

    UpdateMenu();

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
        mApp.getDbx().getAccountManager().startLink(this, REQUEST_LINK_TO_DBX);
        UpdateMenu();
        break;
      case R.id.action_menu_unlink:
        Log.i(mApp.TAG, "unlink");
        mApp.getDbx().getAccountManager().unlink();
        UpdateMenu();
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

  //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  //Helper Methods

  public void UpdateMenu() {
    if (mApp.getDbx() != null && mApp.getDbx().getAccountManager().hasLinkedAccount()) {
      mMenuItemLink.setEnabled(false);
      mMenuItemUnlink.setEnabled(true);
      mMenuItemCreateFile.setEnabled(true);
    } else {
      mMenuItemLink.setEnabled(true);
      mMenuItemUnlink.setEnabled(false);
      mMenuItemCreateFile.setEnabled(false);
    }
  }

  //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
}
