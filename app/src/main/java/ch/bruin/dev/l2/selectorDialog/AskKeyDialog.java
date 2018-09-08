package ch.bruin.dev.l2.selectorDialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.Crypto.StoredCryptoMethod;
import ch.bruin.dev.l2.R;
import ch.bruin.dev.l2.TranscodingHelper;

public class AskKeyDialog extends Dialog implements View.OnClickListener {
    private final CryptoMethod method;
    private final TranscodingHelper callback;

    public AskKeyDialog(CryptoMethod method, TranscodingHelper callback, AppCompatActivity a) {
        super(a);
        this.method = method;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Keys in Base64 should be allowed as it is otherwise impossible to enter certain keys
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ask_key);
        final EditText keyField = findViewById(R.id.passwordField);
        final TextView lengthView = findViewById(R.id.length);
        final Button button = findViewById(R.id.btn_continue);

        keyField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                int delta = editable.toString().getBytes().length * 8 - method.getKeySize();
                if (delta < 0) {
                    lengthView.setText("<");
                    lengthView.setTextColor(Color.RED);
                    button.setClickable(false);
                } else if (delta > 0) {
                    lengthView.setText(">");
                    lengthView.setTextColor(Color.RED);
                    button.setClickable(false);
                } else {    // Equal
                    lengthView.setText("OK");
                    lengthView.setTextColor(Color.GREEN);
                    button.setClickable(true);
                }
            }
        });
        findViewById(R.id.btn_continue).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                byte[] password = ((TextInputEditText) findViewById(R.id.passwordField)).getText().toString().getBytes();
                if (password.length * 8 == method.getKeySize()) {
                    this.dismiss();
                    callback.onMethodSelected(new StoredCryptoMethod(method.getName(), method, password));
                } else {
                    // The size check is enforced, so this should not happen
                    Log.wtf("KeyDialog", "Entered key is the wrong size");
                }
                break;
        }
    }
}