package karman.com.gcmproject.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import karman.com.gcmproject.R;
import karman.com.gcmproject.async.Register;
import karman.com.gcmproject.util.Constants;


public class LoginActivity extends ActionBarActivity {

    EditText etName;
    EditText etMobile;
    Button btLogin;

    Activity context;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        etName = (EditText) findViewById (R.id.etName);
        etMobile= (EditText) findViewById (R.id.etMobile);
        btLogin = (Button) findViewById (R.id.btLogin);
        btLogin.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new Register (LoginActivity.this).execute (etName.getText ().toString (), etMobile.getText ().toString (), Constants.regid);
            }
        });
    }
}
