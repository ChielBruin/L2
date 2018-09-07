package ch.bruin.dev.l2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogListener;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReceiveActivity  extends AppCompatActivity implements TransCodingCallback {

    private TranscodingHelper helper;
    private byte[] rx_data;

    private TextView textView;
    private Switch swtDisplayMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        this.helper = new TranscodingHelper(this, this);
        this.textView = findViewById(R.id.rx_data);
        this.swtDisplayMode = findViewById(R.id.swt_display_mode);

        swtDisplayMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateTextField();
            }
        });

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        rx_data = null;
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                rx_data = handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                rx_data = handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                rx_data = handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

        updateTextField();
    }

    private void updateTextField() {

        if (rx_data == null || rx_data.length == 0) {
            textView.setText("EMPTY DATA");
            return;
        }

        String text;
        if (swtDisplayMode.isChecked()) {
            text = TranscodingHelper.toBase64(rx_data);
        } else {
            text = new String(rx_data);;
        }
        textView.setText(text);
    }

    private byte[] handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            //If its valid Base64 it can be both, otherwise it must be plaintext
            if (sharedText.matches("[A-Za-z0-1_-]+")) {
                String query = "We are not sure what kind of data this is, please select the correct data type.";
                BinaryDialogWrapper.ask(this, query, "text", "binary", new BinaryDialogListener<String>() {
                    @Override
                    public void dialogResult(String data, boolean positive) {
                        if (positive) {
                            rx_data = data.getBytes();
                            swtDisplayMode.setChecked(true);
                        } else {
                            rx_data = TranscodingHelper.fromBase64(data);
                            swtDisplayMode.setChecked(false);
                        }
                        updateTextField();
                    }
                }, sharedText);
            } else {
                return sharedText.getBytes();
            }
        }
        return null;
    }

    private byte[] handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            try {
                return readBytes(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private byte[] handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            //TODO
        }
        return null;
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
        this.helper.encode(rx_data);
    }

    public void onDecode(View v) {
        this.helper.decode(rx_data);
    }

    @Override
    public void onTranscodeFinish(TranscodingMode mode, byte[] original, CryptoMethod method, byte[] result) {
        rx_data = result;

        if (mode == TranscodingMode.ENCODE) {
            String query = "Select how we should send the data";
            BinaryDialogWrapper.ask(this, query, "Text", "File", new BinaryDialogListener<byte[]>() {
                @Override
                public void dialogResult(byte[] data, boolean positive) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    if (positive) {
                        if (swtDisplayMode.isChecked()) {
                            sendIntent.putExtra(Intent.EXTRA_TEXT, TranscodingHelper.toBase64(data));
                        } else {
                            sendIntent.putExtra(Intent.EXTRA_TEXT, new String(data));
                        }
                    } else {
                        //TODO
                        Log.e("ReceiveSendAction", "Binary data file not yet supported, using Base64 instead");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, TranscodingHelper.toBase64(data));
                    }
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }, rx_data);
        } else if (mode == TranscodingMode.DECODE) {
            updateTextField();
        }
    }
}
