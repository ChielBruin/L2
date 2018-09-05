package ch.bruin.dev.l2.selectorDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.Crypto.StoredCryptoMethod;
import ch.bruin.dev.l2.CryptoMethodListenerActivity;
import ch.bruin.dev.l2.R;

public class AskKeyDialog extends Dialog implements View.OnClickListener {
    private final CryptoMethod method;
    public CryptoMethodListenerActivity parentActivity;

    public AskKeyDialog(CryptoMethodListenerActivity a, CryptoMethod method) {
        super(a);
        this.parentActivity = a;
        this.method = method;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ask_key);
        findViewById(R.id.btn_continue).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                String password = ((TextInputEditText) findViewById(R.id.passwordField)).getText().toString();
                if (password.length() == method.getKeySize()) {
                    this.dismiss();
                    parentActivity.onMethodSelected(new StoredCryptoMethod(method.getName(), method, password));
                } else {
                    //TODO
                }
                break;
        }
    }
}