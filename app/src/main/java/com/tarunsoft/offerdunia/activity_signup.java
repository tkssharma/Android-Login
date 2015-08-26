package com.tarunsoft.offerdunia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tarunsoft.offerdunia.Handler.SQLiteHandler;
import com.tarunsoft.offerdunia.Handler.SessionManager;
import com.tarunsoft.loginhandler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class activity_signup extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private SessionManager session;
    private SQLiteHandler db;
    private Context mContext;
    @InjectView(com.tarunsoft.loginhandler.R.id.input_name)
    EditText _nameText;
    @InjectView(com.tarunsoft.loginhandler.R.id.input_email) EditText _emailText;
    @InjectView(com.tarunsoft.loginhandler.R.id.input_password) EditText _passwordText;
    @InjectView(com.tarunsoft.loginhandler.R.id.btn_signup)
    Button _signupButton;
    @InjectView(com.tarunsoft.loginhandler.R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tarunsoft.loginhandler.R.layout.activity_signup);
        ButterKnife.inject(this);
        mContext = this;
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu__signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(activity_signup.this,
                com.tarunsoft.loginhandler.R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        // Session manager
        session = new SessionManager(mContext);

        // SQLite database handler
        db = new SQLiteHandler(mContext);

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(activity_signup.this,
                    ActivityLogin.class);
            startActivity(intent);
            finish();
        }
        registerUser(name, email, password);
        progressDialog.dismiss();
    }

    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
         String tag_string_req = "req_register";

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);
        params.put("tag", "login");
        HttpClient.get("", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println(response.toString());
                try {

                    boolean error = response.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = response.getString("uid");

                        JSONObject user = response.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        // Launch login activity
                        Intent intent = new Intent(
                                activity_signup.this,
                                ActivityLogin.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = response.getString("error_msg");
                        Toast.makeText(mContext,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Pull out the first event on the public timeline

              //  progressDialog.dismiss();
                // Do something with the response
                System.out.println(response.toString());


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // progress.setVisibility(ProgressBar.GONE);
              //  progressDialog.dismiss();
            }
        });


        // Adding request to request queue
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Register failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}