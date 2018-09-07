package ch.bruin.dev.l2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogListener;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogWrapper;

public class SendActivity extends AppCompatActivity implements TransCodingCallback {

    private TranscodingHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        this.helper = new TranscodingHelper(this, this);
    }

    public void onEncode(View view) {
        String plaintext = ((EditText)findViewById(R.id.textField)).getText().toString();
        helper.encodeString(plaintext);
    }

    public void onDecode(View view) {
        String ciphertext = ((EditText)findViewById(R.id.textField)).getText().toString();
        helper.decodeString(ciphertext);
    }

    @Override
    public void onTranscodeFinish(TranscodingMode mode, byte[] original, CryptoMethod method, byte[] result) {
        if (mode == TranscodingMode.ENCODE) {
            String b64ciphertext = TranscodingHelper.toBase64(result);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, b64ciphertext);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (mode == TranscodingMode.DECODE) {
            BinaryDialogWrapper.ask(this, "How would you like to display the result?", "Text", "Binary", new BinaryDialogListener<byte[]>() {
                @Override
                public void dialogResult(byte[] data, boolean positive) {
                    String result;
                    if (positive) {
                        result = new String(data);
                    } else {
                        result = TranscodingHelper.toBase64(data);
                    }
                    ((EditText) findViewById(R.id.textField)).setText(result);
                }
            }, result);
        }
    }
}
