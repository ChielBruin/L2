package ch.bruin.dev.l2;

import android.support.v7.app.AppCompatActivity;
import ch.bruin.dev.l2.Crypto.CryptoMethod;

public abstract class CryptoMethodListenerActivity extends AppCompatActivity {
    public abstract void onMethodSelected(CryptoMethod method);
}
