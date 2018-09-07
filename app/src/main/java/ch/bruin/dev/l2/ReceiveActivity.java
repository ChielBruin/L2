package ch.bruin.dev.l2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import ch.bruin.dev.l2.Crypto.CryptoMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReceiveActivity  extends AppCompatActivity implements TransCodingCallback  {

    private class Data {
        public byte[] data;
        public byte[] result;

        public Data() {
            this.data = null;
            this.result = null;
        }

        public boolean isEmpty() {
            return data == null;
        }

        public String toReadable() {
            if (data == null) return "";
            return TranscodingHelper.toBase64(data);
        }

        public void setText(String text) {
            this.data = TranscodingHelper.fromBase64(text);
        }

        public void setRaw(byte[] data) {
            this.data = data;
        }
    }

    private boolean currentModeEncode = false;
    private Data rx_data;
    private TranscodingHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        this.helper = new TranscodingHelper(this, this);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        rx_data = new Data();


        boolean binary = true;
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
                binary = !rx_data.isEmpty();
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
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

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            rx_data.setText(sharedText);
        }
    }

    private void handleSendImage(Intent intent) {
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

    private void handleSendMultipleImages(Intent intent) {
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
        this.currentModeEncode = true;
        this.helper.encode(rx_data.data);
    }

    public void onDecode(View v) {
        this.currentModeEncode = false;
        this.helper.decode(rx_data.data);
    }

    @Override
    public void onTranscodeFinish(byte[] original, CryptoMethod method, byte[] result) {
        rx_data.result = result;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        displayResult(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        displayResult(false);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select how we should " + (currentModeEncode ? "send" : "display") + " the data").setPositiveButton("Text", dialogClickListener)
                .setNegativeButton("Binary", dialogClickListener).show();
    }

    private void displayResult(boolean plaintext) {
        if (currentModeEncode) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            if (plaintext) {
                sendIntent.putExtra(Intent.EXTRA_TEXT, rx_data.toReadable());
            } else {
                //TODO
                Log.e("ReceiveSendAction", "Binary data not yet supported");
                sendIntent.putExtra(Intent.EXTRA_TEXT, new String(rx_data.data));
            }
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else {
            String result;
            if (plaintext) {
                result = new String(rx_data.data);
            } else {
                result = rx_data.toReadable();
            }
            ((TextView) findViewById(R.id.rx_data)).setText(result);
            ((TextView) findViewById(R.id.rx_data_type)).setText("Result");

            rx_data.setRaw(rx_data.result);
            rx_data.result = null;
        }
    }

}
