package com.thepalladiumgroup.iqm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thepalladiumgroup.iqm.core.model.PatientStats;
import com.thepalladiumgroup.iqm.core.model.RecordStats;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.presentation.presenter.IMainPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.MainPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IMainView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity implements IMainView {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(MainActivity.class);
    private IMainPresenter presenter;
    private User user;
    private PatientStats patientStats;
    private RecordStats recordStats;
    private Button mProfile, mExit, mSetting;
    private TextView mLoggedInUser, mPatientCount, mMaleCount, mFemaleCount;
    private TextView mRecordCount, mCompleteCount, mPendingCount;

    @Override
    public void setPresenter(IMainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setCurrentUser(User user) {
        setCurrentView(user);
    }

    @Override
    public void setPatientStats(PatientStats patientStats) {
        setCurrentPatientStatsView(patientStats);
    }

    @Override
    public void setRecordStats(RecordStats recordStats) {
        setCurrentRecordStatsView(recordStats);
    }

    @Override
    public void initialize() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(myToolbar);
        setTitle(getString(R.string.title_dashboard));
        myToolbar.setLogo(R.mipmap.ic_launcher);

        mProfile = (Button) findViewById(R.id.buttonProfile);
        mExit = (Button) findViewById(R.id.buttonExit);
        mSetting = (Button) findViewById(R.id.buttonDevice);
        mLoggedInUser = (TextView) findViewById(R.id.textViewMainUser);
        mPatientCount = (TextView) findViewById(R.id.textViewPatientCount);
        mMaleCount = (TextView) findViewById(R.id.textViewStatM);
        mFemaleCount = (TextView) findViewById(R.id.textViewStatF);

        mRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        mCompleteCount = (TextView) findViewById(R.id.textViewRecordComplete);
        mPendingCount = (TextView) findViewById(R.id.textViewRecordPending);

        presenter = new MainPresenter(this, (IQMobileApplication) getApplication());
        presenter.cleanUp();
        presenter.loadCurrentUser();
        presenter.loadPatientStats();
        presenter.loadRecordStats();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_setting:
                mSetting.performClick();
                break;

            case R.id.action_exit:
                mExit.performClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit IQMobile")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.finish();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onClickManage(View view) {
        SLF_LOGGER.debug("Open Find/Add patients");
        Intent intent = new Intent(this, FindAddPatientActivity.class);
        startActivity(intent);
    }
    public void onClickNew(View view) {
        SLF_LOGGER.debug("Open New Patient");
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickDeviceInfo(View view) {
        SLF_LOGGER.debug("Open Device Settings");
        Intent intent = new Intent(this, DeviceActivity.class);
        startActivity(intent);
    }

    public void onClickSync(View view) {
        SLF_LOGGER.debug("Open Sync");
        Intent intent = new Intent(this, SyncActivity.class);
        startActivity(intent);
    }

    public void onClickLogOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void onClickExit(View view) {
        onBackPressed();
    }

    private void setCurrentView(User user) {
        this.user = user;
        mLoggedInUser.setText("welcome, [" + this.user.getUsername() + "]");
        mProfile.setText(this.user.getUsername());

    }

    private void setCurrentPatientStatsView(PatientStats patientStats) {
        this.patientStats = patientStats;
        mPatientCount.setText(String.valueOf(this.patientStats.getTotalCount()));
        mMaleCount.setText(this.patientStats.getMaleCountInfo());
        mFemaleCount.setText(this.patientStats.getFemaleCountInfo());
    }

    private void setCurrentRecordStatsView(RecordStats recordStats) {
        this.recordStats = recordStats;
        mRecordCount.setText(String.valueOf(this.recordStats.getTotalCount()));
        mCompleteCount.setText(String.valueOf(this.recordStats.getCompleteCount()));
        mPendingCount.setText(String.valueOf(this.recordStats.getPendingCount()));
    }
}
