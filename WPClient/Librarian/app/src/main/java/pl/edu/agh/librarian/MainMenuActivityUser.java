package pl.edu.agh.librarian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.edu.agh.librarian.tools.Logout;

public class MainMenuActivityUser extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_activity_user);
        initButtons();
    }

    private void initButtons() {
        Button browse = (Button) findViewById(R.id.browseUserButton);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivityUser.this, BrowseBooksActivity.class);
                startActivity(intent);
            }
        });

        Button borrowed = (Button) findViewById(R.id.borrowedUserButton);
        borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivityUser.this, BorrowedBooksActivity.class);
                startActivity(intent);
            }
        });

        Button logout = (Button) findViewById(R.id.logoutUserButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Logout(MainMenuActivityUser.this).logout();
            }
        });
    }
}
