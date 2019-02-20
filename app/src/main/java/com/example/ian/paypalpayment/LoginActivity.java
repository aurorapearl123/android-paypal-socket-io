package com.example.ian.paypalpayment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ian.paypalpayment.Config.Config;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.id_btn_login) Button _logButton;
    @BindView(R.id.id_user_name) EditText _username;
    @BindView(R.id.id_password) EditText _password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        _logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //_username.setError("User Name not found");
                login();
            }
        });
    }

    public boolean validate()
    {
        boolean valid = true;

        String user_name = _username.getText().toString();
        String password = _password.getText().toString();

        if(user_name.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(user_name).matches()) {
            _username.setError("enter a valid email address");
            valid = false;
        }
        else {
            _username.setError(null);
        }

        if(password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _password.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        }
        else
        {
            _password.setError(null);
        }

        return valid;
    }

    public void login()
    {
        if(!validate()) {
            onLoginFailed();
            return;
        }
        _logButton.setEnabled(false);
        //add progress bar
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyGravity);
        progressDialog.setIndeterminate(true);

        progressDialog.setMessage("Authenticating..");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Log.wtf("LOGIN-ME", "Please login me");
                if(Config.getInstance(getApplication()).isOnline()) {
                    String user_name = _username.getText().toString();
                    String password = _password.getText().toString();
                    RICRest.getInstance().login(user_name, password, getApplicationContext());
                    onLoginSuccess(progressDialog);
                }
                else {
                    onLoginFailed();
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Check connection", Toast.LENGTH_LONG).show();
                }

            }
        }, 3000);
    }

    public void onLoginSuccess(ProgressDialog progressDialog) {
        _logButton.setEnabled(true);
        //startActivity(new Intent(this, HomePageActivity.class));
        progressDialog.dismiss();
        //finish();
    }
    public void onLoginFailed()
    {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _logButton.setEnabled(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
