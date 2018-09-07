package ch.bruin.dev.l2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        this.helper = new TranscodingHelper(this, this);

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

        if (rx_data == null || rx_data.length == 0) {
            ((TextView) findViewById(R.id.rx_data)).setText("EMPTY DATA");
        } else {
            BinaryDialogWrapper.ask(this, "How should we display the data", "Text", "Binary", new BinaryDialogListener<byte[]>() {
                @Override
                public void dialogResult(byte[] data, boolean positive) {
                    TextView dataView = (TextView) findViewById(R.id.rx_data);
                    String text;
                    if (positive) {
                        text = new String(data);
                    } else {
                        text = TranscodingHelper.toBase64(data);
                    }
                    dataView.setText(text);
                    dataView.setMovementMethod(new ScrollingMovementMethod());
                    ((TextView) findViewById(R.id.rx_data_type)).setText(positive ? "plain" : "binary");
                }
            }, rx_data);
        }
    }

    private byte[] handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            return sharedText.getBytes();
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
            BinaryDialogWrapper.ask(this, query, "Text", "Binary", new BinaryDialogListener<byte[]>() {
                @Override
                public void dialogResult(byte[] data, boolean positive) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    if (positive) {
                        sendIntent.putExtra(Intent.EXTRA_TEXT, TranscodingHelper.toBase64(data));
                    } else {
                        //TODO
                        Log.e("ReceiveSendAction", "Binary data not yet supported");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, new String(data));
                    }
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }, rx_data);
        } else if (mode == TranscodingMode.DECODE) {
            String query = "Select how we should display the data";
            BinaryDialogWrapper.ask(this, query, "Text", "Binary", new BinaryDialogListener<byte[]>() {
                @Override
                public void dialogResult(byte[] data, boolean positive) {
                    String result;
                    if (positive) {
                        result = new String(data);
                    } else {
                        result = TranscodingHelper.toBase64(data);
                    }
                    ((TextView) findViewById(R.id.rx_data)).setText(result);
                    ((TextView) findViewById(R.id.rx_data_type)).setText("Result");
                }
            }, rx_data);
        }
    }
}
