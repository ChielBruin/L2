package ch.bruin.dev.l2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.selectorDialog.CryptoSelectDialog;

import java.security.InvalidKeyException;

public class SendActivity extends CryptoMethodListenerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
    }

    void onSend(View view) {
        CryptoSelectDialog newFragment = new CryptoSelectDialog();
        newFragment.setParentActivity(this);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onMethodSelected(CryptoMethod method) {
        //TODO: export binary format
        Log.i("Protocol", "Selected protocol " + method.getName());
        if (method.requiresKey()) {
            Log.e("Protocol", "Received protocol still requires a key, this should never happen");
        } else {
            String plaintext = ((EditText)findViewById(R.id.textField)).getText().toString();
            try {
                byte[] ciphertext = method.encodeWithoutKey(plaintext.getBytes());
                String b64ciphertext = Base64.encodeToString(ciphertext, Base64.NO_WRAP + Base64.URL_SAFE);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, b64ciphertext);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            } catch (InvalidKeyException e) {
                Log.wtf("protocol", "Key is included but is gone when we try to use it");
                e.printStackTrace();
            }
        }
    }
}
