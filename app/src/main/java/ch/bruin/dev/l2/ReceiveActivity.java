package ch.bruin.dev.l2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogListener;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReceiveActivity  extends TranscodingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        this.setButtons(R.id.btn_encode, R.id.btn_decode);
        this.setDataView(R.id.rx_data);
        this.setSwitch(R.id.swt_display_mode);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            } else if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent);
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }



    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            //If its valid Base64 it can be both, otherwise it must be plaintext
            if (sharedText.matches("[A-Za-z0-1_-]+")) {
                String query = "We are not sure what kind of data this is, please select the correct data type.";
                BinaryDialogWrapper.ask(this, query, "text", "binary", new BinaryDialogListener<String>() {
                    @Override
                    public void dialogResult(String data, boolean positive) {
                        setData(data, positive);
                    }
                }, sharedText);
            } else {
                setData(sharedText, true);
            }
        }
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            try {
                setData(readBytes(imageUri), false);
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
}
