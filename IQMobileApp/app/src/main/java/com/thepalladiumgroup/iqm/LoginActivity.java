package com.thepalladiumgroup.iqm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.presentation.presenter.ILoginPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.LoginPresenter;
import com.thepalladiumgroup.iqm.presentation.view.ILoginView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener {

    //TODO: remove set DEV_MODE to false
    //region fields
    private static final boolean DEV_MODE = false;
    private static final Logger LOG = LoggerFactory.getLogger(LoginActivity.class);
    //region controls
    Spinner spinnerUserList;
    @NotEmpty(messageId = com.thepalladiumgroup.iqm.R.string.validation_password, order = 1)
    EditText editTextPassword;
    TextView textViewError;
    Button buttonLogin;
    private ILoginPresenter presenter;
    Spinner spinnerTestingPoint;
    private User user;
    private List<User> users;
    private List<Lookup> lookups;
    private ArrayAdapter<User> userArrayAdapter;
    private ArrayAdapter<Lookup> lookupArrayAdapter;
    //endregion
    //endregion

    @Override
    public void setPresenter(ILoginPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setUsersList(List<User> Users) {
        setCurrentView(Users);
    }

    @Override
    public User getUser() {
        return getCurrentView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.thepalladiumgroup.iqm.R.layout.activity_login);
        initialize();
    }

    @Override
    public void initialize() {

        LOG.debug("initializing...");

        Toolbar myToolbar = (Toolbar) findViewById(com.thepalladiumgroup.iqm.R.id.login_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            setTitle(getString(com.thepalladiumgroup.iqm.R.string.title_login));
            myToolbar.setLogo(com.thepalladiumgroup.iqm.R.drawable.ic_lock_outline_white_24dp);
        }

        spinnerUserList = (Spinner) findViewById(com.thepalladiumgroup.iqm.R.id.spinnerUsers);
        editTextPassword = (EditText) findViewById(com.thepalladiumgroup.iqm.R.id.editTextPassword);
        textViewError = (TextView) findViewById(com.thepalladiumgroup.iqm.R.id.textViewError);

        spinnerTestingPoint = (Spinner) findViewById(R.id.spinnerTestingPoint);

        buttonLogin = (Button) findViewById(com.thepalladiumgroup.iqm.R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        presenter = new LoginPresenter(this, (IQMobileApplication) getApplication());
        presenter.loadUsers();
        presenter.loadLookups();

        if (DEV_MODE) {
            editTextPassword.setText("admin1");
            buttonLogin.performClick();
        }

        LOG.debug("initialized !");
    }

    @Override
    public boolean viewIsValid() {

        boolean valid = false;
        if (this != null) {
            valid = FormValidator.validate(this, new SimpleErrorPopupCallback(this));
        }
        return valid;
    }

    @Override
    public void showError(String error) {
        textViewError.setText(error);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            onLogin();
        }
    }

    private User getCurrentView() {
        User loginUser = null;
        if (spinnerUserList.getSelectedItem() != null) {
            this.user = (User) spinnerUserList.getSelectedItem();
            loginUser = this.user.getLoginUser(editTextPassword.getText().toString());

            //Get strategy
            if (spinnerTestingPoint.getSelectedItem() != null) {
                String itemName = spinnerTestingPoint.getSelectedItem().toString();

                for (int i=0; i<this.lookups.size();i++) {
                    if(lookups.get(i).getName()==itemName) {
                        loginUser.setStrategy(lookups.get(i).getIqcareid());
                    }
                }
            }
        }

        return loginUser;
    }

    private void setCurrentView(List<User> users) {
        this.users = users;
        userArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.users);
        userArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerUserList.setAdapter(userArrayAdapter);
    }

    public void onLogin() {
        LOG.debug("authenticating...");
        if (viewIsValid() && presenter.login()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        LOG.debug("authentication failed");
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit IQMobile")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void setLookups(List<Lookup> lookups) {
        SetLookupsList(lookups);
    }

    private void SetLookupsList(List<Lookup> lookups) {
        this.lookups = lookups;
        lookupArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lookups);
        lookupArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerTestingPoint.setAdapter(lookupArrayAdapter);
    }
}
