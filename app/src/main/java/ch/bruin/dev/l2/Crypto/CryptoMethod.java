package ch.bruin.dev.l2.Crypto;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.InvalidKeyException;

public abstract class CryptoMethod {
    private String name;
    private int keySize;
    private String protocol;
    private boolean requiresKey;
//
//    public void draw(Context context) {
//        LinearLayout container = new LinearLayout(context);
//        container.setOrientation(LinearLayout.HORIZONTAL);
//
//
//    }

    public CryptoMethod(String name, String protocol, int keySize, boolean requiresKey) {
        this.name = name;
        this.protocol = protocol;
        this.keySize = keySize;
        this.requiresKey = requiresKey;
    }

    public void draw(LinearLayout container, Context c) {
        RelativeLayout box = new RelativeLayout(c);
        TextView name = new TextView(c);
        name.setText(this.name);
        box.addView(name);

        if (requiresKey) {
            // show closed lock
        } else {
            // show open lock
        }

        container.addView(box);
    }

    public abstract byte[] encode(byte[] plaintext,  String key);
    public abstract byte[] decode(byte[] ciphertext, String key);


    public byte[] encodeWithoutKey(byte[] plaintext) throws InvalidKeyException {
        throw new InvalidKeyException();
    }
    public byte[] decodeWithoutKey(byte[] ciphertext) throws InvalidKeyException {
        throw new InvalidKeyException();
    }

    public String getName() {
        return name;
    }

    public int getKeySize() {
        return keySize;
    }

    public boolean requiresKey() {
        return requiresKey;
    }

    public abstract String  getFamily();

    public abstract String getShortDescription();

    public abstract String getDescription();
}