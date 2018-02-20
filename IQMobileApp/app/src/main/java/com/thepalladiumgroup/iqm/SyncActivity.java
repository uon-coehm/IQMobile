package com.thepalladiumgroup.iqm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thepalladiumgroup.iqm.core.model.Server;
import com.thepalladiumgroup.iqm.presentation.presenter.ISyncPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.SyncPresenter;
import com.thepalladiumgroup.iqm.presentation.view.ISyncView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncActivity extends AppCompatActivity implements ISyncView, View.OnClickListener {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(SyncActivity.class);
    private ISyncPresenter presenter;
    private Server server;

    private TextView mTextViewServer, mTextViewAppStatus, mTextViewAppError, mTextViewDataStatus, mTextViewDataError;
    private EditText mEditTextServer;
    private ProgressBar mProgressApp, mProgressData;
    private Button mButtonSyncEdit, mButtonSyncSave, mButtonSyncApp, mButtonSyncData;



    @Override
    public void setPresenter(ISyncPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Server getServer() {
        return getCurrentView();
    }



    @Override
    public void setServer(Server server) {
        setCurrentView(server);
    }

    @Override
    public void showAppSettingsProgress(boolean show) {
        mProgressApp.setVisibility(View.GONE);
        if (show) {
            mProgressApp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showAppDataProgress(boolean show) {
        mProgressData.setVisibility(View.GONE);
        if (show) {
            mProgressData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setAppSettingsProgress(int percentage) {
        mProgressApp.setProgress(percentage);
    }

    @Override
    public void setAppDataProgress(int percentage) {
        mProgressApp.setProgress(percentage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        initialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setBusy(boolean isbusy) {

        mButtonSyncApp.setEnabled(!isbusy);
        mButtonSyncData.setEnabled(!isbusy);
    }

    @Override
    public void initialize() {

        /*
          private EditText mEditTextServer;

    private Button mButtonSyncEdit,mButtonSyncSave,mButtonSyncTest, mButtonSyncApp;

        */
        Toolbar myToolbar = (Toolbar) findViewById(R.id.sync_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_sync));

        mTextViewServer = (TextView) findViewById(R.id.textViewServerName);
        mEditTextServer= (EditText) findViewById(R.id.editTextServer);
        mEditTextServer.setEnabled(false);
        mTextViewAppStatus = (TextView) findViewById(R.id.textViewAppSyncStatus);
        mTextViewDataStatus = (TextView) findViewById(R.id.textViewDataSyncStatus);
        mProgressApp = (ProgressBar) findViewById(R.id.progressBarAppSync);
        mProgressData = (ProgressBar) findViewById(R.id.progressBarDataSync);
        mTextViewAppError = (TextView) findViewById(R.id.textViewAppSettingError);
        mTextViewDataError = (TextView) findViewById(R.id.textViewAppDataError);
        mButtonSyncEdit = (Button) findViewById(R.id.buttonSyncEdit);
        mButtonSyncEdit.setOnClickListener(this);
        mButtonSyncSave = (Button) findViewById(R.id.buttonSyncSave);
        mButtonSyncSave.setOnClickListener(this);
        mButtonSyncSave.setEnabled(false);

        mButtonSyncApp = (Button) findViewById(R.id.buttonAppSync);
        mButtonSyncApp.setOnClickListener(this);
        mButtonSyncData = (Button) findViewById(R.id.buttonDataSync);
        mButtonSyncData.setOnClickListener(this);


        presenter = new SyncPresenter(this, (IQMobileApplication) getApplication());
        presenter.loadSettings();


    }
    private Server getCurrentView() {
        this.server.setUrl(mEditTextServer.getText().toString());
        return this.server;
    }

    private void setCurrentView(Server server) {
        this.server = server;
        mTextViewServer.setText("Server: "+this.server.getName());
        mEditTextServer.setText(this.server.getUrl());
    }

    @Override
    public void setViewAppSettingsStatus(String status) {
        mTextViewAppStatus.setText(status);
    }

    @Override
    public void setViewAppSettingsErrors(String error) {
        mTextViewAppError.setText(error);
    }

    @Override
    public void setViewAppDataStatus(String status) {
        mTextViewDataStatus.setText(status);
    }


    @Override
    public void setViewAppDataErrors(String error) {
        mTextViewDataError.setText(error);
    }

    @Override
    public void onClick(View v) {


        if (v == mButtonSyncEdit) {
            mEditTextServer.setEnabled(true);
            mButtonSyncSave.setEnabled(true);
            mButtonSyncEdit.setEnabled(false);
            mButtonSyncApp.setEnabled(false);
            mButtonSyncData.setEnabled(false);
        }

        if (v == mButtonSyncSave) {
            presenter.saveServer();
            mEditTextServer.setEnabled(false);
            mButtonSyncSave.setEnabled(false);
            mButtonSyncEdit.setEnabled(true);
            mButtonSyncApp.setEnabled(true);
            mButtonSyncData.setEnabled(true);
        }

        if (v == mButtonSyncApp) {
            presenter.syncAppSettings();
        }
        if (v == mButtonSyncData) {
            presenter.syncAppData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SLF_LOGGER.debug("Resumed");
//        SLF_LOGGER.debug(presenter.getUserTask().getStatus().toString());
//        SLF_LOGGER.debug(presenter.getLookupTask().getStatus().toString());
//        SLF_LOGGER.debug(presenter.getModuleTask().getStatus().toString());
//        SLF_LOGGER.debug(presenter.getConceptTask().getStatus().toString());
        SLF_LOGGER.debug("Resumed");
    }
}
