package ch.bruin.dev.l2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSend(View view) {
        Intent SendActivityIntent = new Intent(this, SendActivity.class);
        startActivity(SendActivityIntent);
    }

    public void onAbout(View view) {
        Intent AboutActivityIntent = new Intent(this, AboutActivity.class);
        startActivity(AboutActivityIntent);
    }
}
