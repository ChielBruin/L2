package ch.bruin.dev.l2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import ch.bruin.dev.l2.Crypto.CryptoMethod;

public class SendActivity extends AppCompatActivity implements TransCodingCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
    }

    public void onSend(View view) {
        TranscodingHelper helper = new TranscodingHelper(this, this);
        String plaintext = ((EditText)findViewById(R.id.textField)).getText().toString();
        helper.encodeString(plaintext);
    }

    @Override
    public void onTranscodeFinish(byte[] original, CryptoMethod method, byte[] result) {
        String b64ciphertext = TranscodingHelper.toBase64(result);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, b64ciphertext);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
