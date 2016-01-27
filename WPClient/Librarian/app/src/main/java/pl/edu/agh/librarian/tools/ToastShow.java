package pl.edu.agh.librarian.tools;

import android.app.Activity;
import android.widget.Toast;

public class ToastShow {
    public static void showToastOnUIThread(final Activity a, final String message, final int length) {
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(a, message, length).show();
            }
        });
    }
}
