package pl.edu.agh.librarian.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.librarian.Exceptions.Exception401;
import pl.edu.agh.librarian.Exceptions.Exception404;
import pl.edu.agh.librarian.Exceptions.Exception500;
import pl.edu.agh.librarian.LoginActivity;
import pl.edu.agh.librarian.R;

public class Logout {

    Activity a;

    public Logout(Activity a){
        this.a = a;
    }

    public void logout() {
        new LogoutTask().execute();

    }

    public class LogoutTask extends AsyncTask<Void, Void, Void> {

        LogoutTask() {
            new SettingsManager(a);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(signoff()) {
                SettingsManager.setAppLauncher(LauncherTypes.LOGIN);
                SettingsManager.setToken(null);
                Intent intent = new Intent(a, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.startActivity(intent);
                a.finish();
            }
            return null;
        }

        private boolean signoff() {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair("token", SettingsManager.getToken()));
            try {
                ServerAPI.POST("/rest/logout/", params);
                return true;
            } catch (Exception500 | Exception404 e) {
                ToastShow.showToastOnUIThread(a, a.getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
                return false;
            } catch (ConnectTimeoutException | Exception401 e) {
                return true;
            }
        }
    }
}
