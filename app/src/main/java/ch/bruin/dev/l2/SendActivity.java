package ch.bruin.dev.l2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.InvalidKeyException;

import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogListener;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogWrapper;
import ch.bruin.dev.l2.selectorDialog.CryptoSelectDialog;

public class SendActivity extends AppCompatActivity implements CryptoSelectionCallback {

    private EditText tx_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        this.tx_data = findViewById(R.id.tx_data);
    }

    public void onEncode(View view) {
        CryptoSelectDialog newFragment = new CryptoSelectDialog();
        newFragment.setCallback(this);
        newFragment.setParentActivity(this);
        newFragment.show(this.getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onSelect(CryptoMethod method, byte[] key) {
        byte[] data = tx_data.getText().toString().getBytes();
        byte[] encoded;
        if (key == null) {
            try {
                encoded = method.encodeWithoutKey(data);
            } catch (InvalidKeyException e) {
                encoded = new byte[0];
                Toast.makeText(getApplicationContext(), "Invalid key", Toast.LENGTH_SHORT).show();
            }
        } else {
            encoded = method.encode(data, key);
        }

        String query = "Select how you would like to send the data";
        BinaryDialogWrapper.ask(this, query, "Text", "File", new BinaryDialogListener<byte[]>() {
            Intent sendIntent = new Intent();

            {
                sendIntent.setAction(Intent.ACTION_SEND);
            }

            @Override
            public void dialogPositiveResult(byte[] data, String option) {
                sendIntent.putExtra(Intent.EXTRA_TEXT, TranscodingHelper.toBase64(data));
                sendIntent();
            }

            @Override
            public void dialogNegativeResult(byte[] data, String option) {
                //TODO
                Toast.makeText(getApplicationContext(), "Image encoding not yet supported", Toast.LENGTH_LONG).show();
                sendIntent.putExtra(Intent.EXTRA_TEXT, TranscodingHelper.toBase64(data));
                sendIntent();
            }

            public void sendIntent() {
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        }, encoded);
    }
}
