package ch.bruin.dev.l2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.selectorDialog.CryptoSelectDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReceiveActivity extends CryptoMethodListenerActivity {

    public void onMethodSelected(CryptoMethod method) {
        //TODO
        Log.i("Protocol", "Selected protocol " + method.getName());
        if (method.requiresKey()) {
            Log.e("Protocol", "Received protocol still requires a key, this should never happen");
        }
    }

    private class Data {
        public byte[] data;

        public Data() {
            this.data = null;
        }

        public boolean isEmpty() {
            return data == null;
        }

        public String toReadable() {
            if (data == null) return "";
            return Base64.encodeToString(data,Base64.NO_WRAP + Base64.URL_SAFE);
        }

        public void setText(String text) {
            this.data = Base64.decode(text,Base64.NO_WRAP + Base64.URL_SAFE);
        }

        public void setRaw(byte[] data) {
            this.data = data;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Data rx_data = new Data();


        boolean binary = true;
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent, rx_data); // Handle text being sent
                binary = !rx_data.isEmpty();
            } else if (type.startsWith("image/")) {
                handleSendImage(intent, rx_data); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent, rx_data); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

        if (rx_data.isEmpty()) {
            ((TextView) findViewById(R.id.rx_data)).setText("EMPTY");
        } else {
            TextView dataView = (TextView) findViewById(R.id.rx_data);
            dataView.setText(new String(rx_data.toReadable()));
            dataView.setMovementMethod(new ScrollingMovementMethod());
            ((TextView) findViewById(R.id.rx_data_type)).setText(binary ? "binary data" : "base64");
        }
    }

    private void handleSendText(Intent intent, Data rx_data) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            rx_data.setText(sharedText);
        }
    }

    private void handleSendImage(Intent intent, Data rx_data) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            try {
                byte[] bytes = readBytes(imageUri);
                rx_data.setRaw(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSendMultipleImages(Intent intent, Data rx_data) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            //TODO
        }
    }

    private byte[] readBytes(Uri uri) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public void onEncode(View v) {
        CryptoSelectDialog newFragment = new CryptoSelectDialog();
        newFragment.setParentActivity(this);
        newFragment.show(getSupportFragmentManager(), "dialog");
//        CryptoSelectDialog dialog = new CryptoSelectDialog(this, CryptoSelectDialog.ENCODE);
//        dialog.show();
    }

    public void onDecode(View v) {
        CryptoSelectDialog newFragment = new CryptoSelectDialog();
        newFragment.setParentActivity(this);
        newFragment.show(getSupportFragmentManager(), "dialog");
//        CryptoSelectDialog dialog = new CryptoSelectDialog(this, CryptoSelectDialog.DECODE);
//        dialog.show();
    }
}
