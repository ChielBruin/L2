package ch.bruin.dev.l2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogListener;
import ch.bruin.dev.l2.selectorDialog.BinaryDialogWrapper;

public abstract class TranscodingActivity extends AppCompatActivity implements TranscodingCallback {
    protected Switch swt;
    protected TextView dataView;
    protected Button btn_decode;
    protected Button btn_encode;

    private byte[] data;
    private TranscodingHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.helper = new TranscodingHelper(this, this);
    }

    protected void setButtons(int btn_encodeId, int btn_decodeId) {
        btn_encode = findViewById(btn_encodeId);
        btn_decode = findViewById(btn_decodeId);

        btn_encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.encode(data);
            }
        });
        btn_decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.decode(data);
            }
        });
    }

    protected void setDataView(int dataViewId) {
        dataView = findViewById(dataViewId);
    }

    protected void setSwitch(int swtId) {
        swt = findViewById(swtId);

        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateTextView();
            }
        });
    }

    public void setData(byte[] data, boolean isPlaintext) {
        this.data = data;
        this.swt.setChecked(!isPlaintext);
        updateTextView();
    }

    public void setData(String data, boolean isPlaintext) {
        if (isPlaintext) {
            this.swt.setChecked(false);
            this.data = data.getBytes();
        } else {
            this.swt.setChecked(true);
            this.data = TranscodingHelper.fromBase64(data);
        }
        updateTextView();
    }

    private void updateTextView() {
        if (data == null || data.length == 0) {
            dataView.setText("EMPTY DATA");
            return;
        }

        String text;
        try {
            if (swt.isChecked()) {
                text = TranscodingHelper.toBase64(data);
            } else {
                text = new String(data);
            }

            // Do not update if nothing changed
            if (dataView.getText().toString().equals(text)) return;
            dataView.setText(text);
        } catch (IllegalArgumentException ex) {
            Log.e("SwitchConversion", "Conversion failed, ignoring input");
            Snackbar.make(dataView, "Invalid Base64 notation", Snackbar.LENGTH_LONG).show();
            swt.setChecked(!swt.isChecked());
        }
    }

    @Override
    public void onTranscodeFinish(TranscodingMode mode, byte[] original, CryptoMethod method, byte[] result) {
        this.data = result;

        if (mode == TranscodingMode.ENCODE) {
            String query = "Select how you would like to send the data";
            BinaryDialogWrapper.ask(this, query, "Text", "File", new BinaryDialogListener<byte[]>() {
                Intent sendIntent = new Intent();
                { sendIntent.setAction(Intent.ACTION_SEND); }

                @Override
                public void dialogPositiveResult(byte[] data, String option) {
                    if (swt.isChecked()) {
                        sendIntent.putExtra(Intent.EXTRA_TEXT, TranscodingHelper.toBase64(data).trim());
                    } else {
                        sendIntent.putExtra(Intent.EXTRA_TEXT, new String(data).trim());
                    }
                    sendIntent();
                }

                @Override
                public void dialogNegativeResult(byte[] data, String option) {
                    //TODO
                    Log.e("ReceiveSendAction", "Binary data file not yet supported, using Base64 instead");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, TranscodingHelper.toBase64(data).trim());
                    sendIntent();
                }

                public void sendIntent() {
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }, data);
        } else if (mode == TranscodingMode.DECODE) {
            updateTextView();
        }
    }
}
