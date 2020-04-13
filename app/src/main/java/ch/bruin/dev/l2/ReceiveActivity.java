package ch.bruin.dev.l2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidKeyException;

import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.selectorDialog.CryptoSelectDialog;

public class ReceiveActivity  extends AppCompatActivity implements CryptoSelectionCallback {
    private TextView dataView;
    private Button decodeButton;

    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        dataView = findViewById(R.id.rx_data);
        decodeButton = findViewById(R.id.btn_decode);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            handleSendText(intent);
            updateDataView();
        } else {
            Toast.makeText(getApplicationContext(), "Unsupported media type", Toast.LENGTH_LONG).show();
        }
    }

    private void updateDataView() {
        dataView.setText(TranscodingHelper.toBase64(bytes));
    }

    private byte[] handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            sharedText = sharedText.trim();
            if (sharedText.matches("[A-Za-z0-9_-]+")) {
                bytes = TranscodingHelper.fromBase64(sharedText);
            } else {
                bytes = sharedText.getBytes();
            }
            return bytes;
        }
        return new byte[0];
    }

    public void onDecode(View view) {
        CryptoSelectDialog newFragment = new CryptoSelectDialog();
        newFragment.setCallback(this);
        newFragment.setParentActivity(this);
        newFragment.show(this.getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onSelect(CryptoMethod method, byte[] key) {
        byte[] decoded;
        if (key == null) {
            try {
                decoded = method.decodeWithoutKey(bytes);
            } catch (InvalidKeyException e) {
                decoded = new byte[0];
                Toast.makeText(getApplicationContext(), "Invalid key", Toast.LENGTH_SHORT).show();
            }
        } else {
            decoded = method.decode(bytes, key);
        }
        dataView.setText(new String(decoded));
        decodeButton.setVisibility(View.GONE);
    }
}
