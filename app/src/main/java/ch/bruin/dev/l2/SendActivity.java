package ch.bruin.dev.l2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class SendActivity extends TranscodingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        this.setButtons(R.id.btn_encode, R.id.btn_decode);
        this.setDataView(R.id.textField);
        this.setSwitch(R.id.swt_display_mode);

        this.setData(this.dataView.getText().toString(), true);

        this.dataView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO: remove the need for the next line
                if (swt.isChecked()) {
                    Log.i("Base64 Edit", "Operation not implemented yet");
                    return;
                };
                setData(dataView.getText().toString().trim(), !swt.isChecked());
            }
        });
    }
}
