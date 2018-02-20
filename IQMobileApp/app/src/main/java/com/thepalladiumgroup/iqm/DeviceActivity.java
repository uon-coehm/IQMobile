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
import android.widget.TextView;

import com.thepalladiumgroup.iqm.core.model.Device;
import com.thepalladiumgroup.iqm.core.services.IDeviceService;
import com.thepalladiumgroup.iqm.presentation.presenter.IDevicePresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.DevicePresenter;
import com.thepalladiumgroup.iqm.presentation.view.IDeviceView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceActivity extends AppCompatActivity implements IDeviceView {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(DeviceActivity.class);
    EditText mSerial;
    EditText mCode;
    EditText mFacilitiCode;
    EditText mFacilityName;
    TextView mId;
    Button mEdit;
    Button mSave;
    private IDeviceService deviceService;
    private IDevicePresenter presenter;
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
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
    public IDevicePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(IDevicePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Device getDevice() {
        return getCurrentView();
    }

    @Override
    public void setDevice(Device device) {
        setCurrentView(device);
    }

    @Override
    public void initialize() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.device_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_device));

        mId = (TextView) findViewById(R.id.textViewDeviceId);
        mSerial = (EditText) findViewById(R.id.editTextDeviceSerial);
        mCode = (EditText) findViewById(R.id.editTextDeviceCode);
        mFacilitiCode = (EditText) findViewById(R.id.editTextDeviceFacilitiCode);
        mFacilityName = (EditText) findViewById(R.id.editTextDeviceFacilityName);
        mEdit = (Button) findViewById(R.id.buttonDeviceEdit);
        mSave = (Button) findViewById(R.id.buttonDeviceSave);

        presenter = new DevicePresenter(this, (IQMobileApplication) getApplication());
        presenter.loadDeviceInfo();
        allowedit(true);

    }

    @Override
    public void allowedit(boolean state) {
        mCode.setEnabled(!state);
        mEdit.setEnabled(state);
        mSave.setEnabled(!state);
    }

    private Device getCurrentView() {
        device = new Device(
                mSerial.getText().toString(),
                mCode.getText().toString(),
                Integer.parseInt(mFacilitiCode.getText().toString()),
                mFacilityName.getText().toString()
        );
        device.setId(Integer.parseInt(mId.getText().toString()));
        return device;
    }

    private void setCurrentView(Device device) {
        this.device = device;
        mId.setText(String.valueOf(device.getId()));
        mSerial.setText(device.getSerial());
        mCode.setText(device.getCode());
        mFacilitiCode.setText(String.valueOf(device.getFacilitycode()));
        mFacilityName.setText(device.getFacility());
    }

    public void onEdit(View view) {
        allowedit(false);
    }

    public void onSave(View view) {
        presenter.saveDeviceInfo();
    }
}
