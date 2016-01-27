package pl.edu.agh.librarian.tools;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import pl.edu.agh.librarian.Exceptions.Exception401;
import pl.edu.agh.librarian.Exceptions.Exception404;
import pl.edu.agh.librarian.Exceptions.Exception500;
import pl.edu.agh.librarian.LoginActivity;
import pl.edu.agh.librarian.R;
import pl.edu.agh.librarian.db.BorrowedBooksManager;
import pl.edu.agh.librarian.db.DBManager;

public class SyncAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SettingsManager(context);
                if (SettingsManager.getAppLauncher() == LauncherTypes.USER) {
                    if (isNetworkAvailable(context) && canReachServer()) {
                        Log.i("Sync", "sync yes");
                        new UpdateBorrowedBooksInDB(context).execute();
                    } else {
                        Log.i("Sync", "sync no");
                        sendNotification(context);
                    }
                }
            }
        }).start();
        wl.release();
    }

    private boolean canReachServer() {
        try {
            URL myUrl = new URL("http://89.77.193.66:8080");
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }

    public void SetAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SyncAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        CancelAlarm(context);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 5, pi);
    }

    private boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, SyncAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void sendNotification(Context context) {
        List<List<Object>> books;
        boolean after_time = false;
        BorrowedBooksManager borrowedBooksManager = new BorrowedBooksManager(context);
        if ((books = borrowedBooksManager.getBooks()) != null) {
            for (List<Object> book : books) {
                DateFormat format = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
                try {
                    Date date = format.parse((String) book.get(1));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 40);
                    Date future_date = cal.getTime();
                    if(date.after(future_date)) {
                        after_time = true;
                    }
                } catch (ParseException e) {
                    Log.e("SyncAlarm", "Parse error");
                }
            }
            if(after_time) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(R.drawable.common_ic_googleplayservices);

                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                builder.setContentTitle("Librarian");
                builder.setContentText("Some books will be due");
                Intent intent = new Intent(context, LoginActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
                builder.setContentIntent(pIntent);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            }
        }
    }

    public class UpdateBorrowedBooksInDB extends AsyncTask<Void, Void, Void> {

        Context context;

        UpdateBorrowedBooksInDB(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {

            getBooks();
            return null;

        }

        private void getBooks() {
            JSONArray books;
            BorrowedBooksManager borrowedBooksManager;
            try {
                List<NameValuePair> params = new LinkedList<>();
                params.add(new BasicNameValuePair("userID", SettingsManager.getUserID()));
                String response = ServerAPI.GET("/rest/getUserBooksById/", params);
                books = new JSONArray(response);
                borrowedBooksManager = new BorrowedBooksManager(context);
                for (int i = 0; i < books.length(); i++) {
                    books.getJSONObject(i);
                }

            } catch (Exception401 | Exception500 | Exception404 | ConnectTimeoutException | JSONException e) {
                Log.e("SyncAlarm", "JSON error");
                return;
            }
            try {
                borrowedBooksManager.deleteBooks();
                for (int i = 0; i < books.length(); i++) {
                    JSONObject book = books.getJSONObject(i);
                    JSONArray dates = book.getJSONArray("dates");
                    JSONObject date = dates.getJSONObject(0);
                    String date_real = date.getString("planningReturnDate").replace(",", "-").replace("[","").replace("]", "");
                    borrowedBooksManager.addBook(book.getString("title"), date_real);
                }
                sendNotification(context);
            } catch (JSONException e) {

            }
        }
    }
}