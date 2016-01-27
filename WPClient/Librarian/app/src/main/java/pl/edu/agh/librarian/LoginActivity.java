package pl.edu.agh.librarian;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.librarian.Exceptions.Exception401;
import pl.edu.agh.librarian.Exceptions.Exception404;
import pl.edu.agh.librarian.Exceptions.Exception500;
import pl.edu.agh.librarian.tools.LauncherTypes;
import pl.edu.agh.librarian.tools.ServerAPI;
import pl.edu.agh.librarian.tools.SettingsManager;
import pl.edu.agh.librarian.tools.SyncAlarm;
import pl.edu.agh.librarian.tools.ToastShow;


/**
 * A login screen that offers login via login/password.
 */
public class LoginActivity extends Activity {

    private static final String ZXING = "com.google.zxing.client.android";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkForZXing();
        new SyncAlarm().SetAlarm(this);
        Intent intent = null;
        new SettingsManager(LoginActivity.this);
        LauncherTypes lt = SettingsManager.getAppLauncher();
        if (lt == LauncherTypes.ADMIN) {
            intent = new Intent(LoginActivity.this, MainMenuActivityLibrarian.class);
        } else if (lt == LauncherTypes.USER) {
            intent = new Intent(LoginActivity.this, MainMenuActivityUser.class);
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLoginView = (EditText) findViewById(R.id.login_val);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginSignInButton = (Button) findViewById(R.id.login_sign_in_button);
        mLoginSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, android.R.anim.slide_in_left));
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid login, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(login, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void checkForZXing() {
        try {
            (LoginActivity.this).getPackageManager().getApplicationInfo(ZXING, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(LoginActivity.this, getString(R.string.no_zxing), Toast.LENGTH_LONG).show();
            Uri marketUri = Uri.parse("market://details?id=" + ZXING);
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
            finish();
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mLogin;
        private final String mPassword;
        private String token;
        private String role;
        private String userID;

        private final String roleADMIN = "ADMIN";
        private final String roleUSER = "USER";

        UserLoginTask(String login, String password) {
            mLogin = login;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return attemptLogin();

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                new SettingsManager(LoginActivity.this);
                if (role.equals(roleADMIN)) {
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivityLibrarian.class);
                    SettingsManager.setToken(token);
                    SettingsManager.setAppLauncher(LauncherTypes.ADMIN);
                    finish();
                    startActivity(intent);
                } else if (role.equals(roleUSER)) {
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivityUser.class);
                    SettingsManager.setToken(token);
                    SettingsManager.setAppLauncher(LauncherTypes.USER);
                    SettingsManager.setUserID(userID);
                    if(SettingsManager.getUserID() == null) {
                        Toast.makeText(LoginActivity.this, "Error, no userID", Toast.LENGTH_LONG).show();
                    } else {
                        finish();
                        startActivity(intent);
                    }
                }

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private boolean attemptLogin() {
            String url = "/rest/login/";
            List<NameValuePair> nameValuePairs = new ArrayList<>(5);
            nameValuePairs.add(new BasicNameValuePair("login", mLogin));
            nameValuePairs.add(new BasicNameValuePair("password", md5(mPassword)));
            try {
                JSONObject returnJSON = new JSONObject(ServerAPI.POST(url, nameValuePairs));
                token = returnJSON.getString("token");
                role = returnJSON.getString("role");
                userID = returnJSON.getString("userID");
                return true;
            } catch (JSONException e) {
                Log.e(TAG, "Blad w parsowaniu JSONa");
            } catch (Exception500 e) {
                ToastShow.showToastOnUIThread(LoginActivity.this, getString(R.string.server_error) + " " + e.getMessage(), Toast.LENGTH_LONG);
                return false;
            } catch (Exception404 | Exception401 e) { // wrong password
                return false;
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(LoginActivity.this, getString(R.string.timeout_error), Toast.LENGTH_LONG);
                return false;
            }
            return false;
        }

        public String md5(String s) {
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest)
                    hexString.append(Integer.toHexString(0xFF & aMessageDigest));
                return hexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}



