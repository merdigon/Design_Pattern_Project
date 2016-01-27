package pl.edu.agh.librarian;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.librarian.Exceptions.Exception401;
import pl.edu.agh.librarian.Exceptions.Exception404;
import pl.edu.agh.librarian.Exceptions.Exception500;
import pl.edu.agh.librarian.tools.Logout;
import pl.edu.agh.librarian.tools.ServerAPI;
import pl.edu.agh.librarian.tools.SettingsManager;
import pl.edu.agh.librarian.tools.ToastShow;


public class MainMenuActivityLibrarian extends Activity {

    //REQUEST CODES
    private static final int REQUEST_BORROW = 0;
    private static final int REQUEST_RETURN = 1;
    private static final int REQUEST_CHANGE_STATUS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu_librarian);

        initButtons();
    }


    private void initButtons() {
        Button scan = (Button) findViewById(R.id.borrowLibrarianButton);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan(REQUEST_BORROW);
            }
        });

        Button bookReturn = (Button) findViewById(R.id.returnLibrarianButton);
        bookReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan(REQUEST_RETURN);
            }
        });

        Button changeCondition = (Button) findViewById(R.id.changeStatusLibrarianButton);
        changeCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan(REQUEST_CHANGE_STATUS);
            }
        });

        final Button logout = (Button) findViewById(R.id.logoutLibrarianButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Logout(MainMenuActivityLibrarian.this).logout();
            }
        });

        Button stocktake = (Button) findViewById(R.id.stocktakeLibrarianButton);
        stocktake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivityLibrarian.this, StocktakeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startScan(int intent_code) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, intent_code);
    }

    private void showConditionDialog(final String uuid) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        AlertDialog.Builder b = new AlertDialog.Builder(MainMenuActivityLibrarian.this);
        b.setTitle("Change condition");
        b.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });
        final String[] types = {"Available", "Reserved", "Borrowed", "Missing", "Damaged", "Destroyed"};
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                new ChangeStatusTask(uuid, types[which]).execute();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });
        b.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_BORROW) {
            if (resultCode == RESULT_OK) {
                String contents;

                if (!intent.getStringExtra("SCAN_RESULT").isEmpty()) {
                    contents = intent.getStringExtra("SCAN_RESULT");
                    Log.i("contents", contents);
                    new GetUserTask(contents, REQUEST_BORROW).execute();
                } else {
                    finish();
                    Toast.makeText(MainMenuActivityLibrarian.this, getString(R.string.scan_canceled), Toast.LENGTH_SHORT).show();
                }

            }
        } else if (requestCode == REQUEST_RETURN) {

            if (resultCode == RESULT_OK) {
                String contents;

                if (!intent.getStringExtra("SCAN_RESULT").isEmpty()) {
                    contents = intent.getStringExtra("SCAN_RESULT");
                    Log.i("contents", contents);
                    new ReturnBookTask(contents).execute();
                } else {
                    finish();
                    Toast.makeText(MainMenuActivityLibrarian.this, getString(R.string.scan_canceled), Toast.LENGTH_SHORT).show();
                }

            }
        } else if (requestCode == REQUEST_CHANGE_STATUS) {

            if (resultCode == RESULT_OK) {
                String contents;

                if (!intent.getStringExtra("SCAN_RESULT").isEmpty()) {
                    contents = intent.getStringExtra("SCAN_RESULT");
                    Log.i("contents", contents);
                    showConditionDialog(contents);

                } else {
                    finish();
                    Toast.makeText(MainMenuActivityLibrarian.this, getString(R.string.scan_canceled), Toast.LENGTH_SHORT).show();
                }

            }
        }
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(MainMenuActivityLibrarian.this, getString(R.string.scan_canceled), Toast.LENGTH_SHORT).show();
        }
    }

    /////////////////////////////////////////

    public class GetUserTask extends AsyncTask<Void, Void, Void> {

        private String uuid;
        private int request;

        GetUserTask(String uuid, int request) {
            this.uuid = uuid;
            this.request = request;
        }

        @Override
        protected Void doInBackground(Void... params) {

            getUser();
            return null;

        }

        private void getUser() {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair("token", SettingsManager.getToken()));
            params.add(new BasicNameValuePair("idNumber", uuid));
            try {
                String response = ServerAPI.GET("/rest/user/", params);
                if (request == REQUEST_BORROW) {
                    Intent intent = new Intent(MainMenuActivityLibrarian.this, BorrowABookActivity.class);
                    intent.putExtra("response", response);
                    startActivity(intent);
                }
            } catch (Exception401 e) { //token expired
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(MainMenuActivityLibrarian.this).logout();
            } catch (Exception404 e) { //no user with uuid
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.wrong_book_uuid), Toast.LENGTH_LONG);
            } catch (Exception500 e) { //else
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
            }
        }
    }

    ///////////////////////////////////

    public class ReturnBookTask extends AsyncTask<Void, Void, Void> { //TODO: show book info

        private String uuid;

        ReturnBookTask(String uuid) {
            this.uuid = uuid;
            new SettingsManager(MainMenuActivityLibrarian.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            returnBook(uuid);
            return null;
        }

        private boolean returnBook(String uuid) {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair("bookUuid", uuid));
            params.add(new BasicNameValuePair("token", SettingsManager.getToken()));
            try {
                ServerAPI.POST("/rest/returnBook/", params);
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.book_return_success), Toast.LENGTH_SHORT);
                return true;
            } catch (Exception401 e) { //token expired
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(MainMenuActivityLibrarian.this).logout();
                return false;
            } catch (Exception404 e) {
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.wrong_book_uuid), Toast.LENGTH_LONG);
                return false;
            } catch (Exception500 e) { //else
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
                return false;
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
                return false;
            }
        }
    }

    /////////////////////

    public class ChangeStatusTask extends AsyncTask<Void, Void, Void> {

        private String uuid;
        private String condition;

        ChangeStatusTask(String uuid, String condition) {
            this.uuid = uuid;
            this.condition = condition;
            new SettingsManager(MainMenuActivityLibrarian.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            changeStatus();

            return null;
        }

        private boolean changeStatus() {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair("uuid", uuid));
            params.add(new BasicNameValuePair("token", SettingsManager.getToken()));
            params.add(new BasicNameValuePair("condition", condition));
            try {
                ServerAPI.POST("/rest/changeCondition/", params);
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.status_changed), Toast.LENGTH_SHORT);
                return true;
            } catch (Exception401 e) { //token expired
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(MainMenuActivityLibrarian.this).logout();
                return false;
            } catch (Exception404 e) {
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.wrong_book_uuid), Toast.LENGTH_LONG);
                return false;
            } catch (Exception500 e) { //else
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
                return false;
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(MainMenuActivityLibrarian.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
                return false;
            }
        }
    }
}