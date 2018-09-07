package ch.bruin.dev.l2;

import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.selectorDialog.CryptoSelectDialog;

import java.security.InvalidKeyException;

public class TranscodingHelper {

    private enum TranscodingMode {
        ENCODE,
        DECODE,
        NONE;
    }

    private TranscodingMode currentMode;
    private byte[] data;

    private final AppCompatActivity rootActivity;
    private final TransCodingCallback callback;

    private static final int B64_FLAGS = Base64.NO_WRAP + Base64.URL_SAFE;

    public TranscodingHelper(TransCodingCallback callback, AppCompatActivity activity) {
        this.callback = callback;
        this.rootActivity = activity;
        this.currentMode = TranscodingMode.NONE;
    }

    public byte[] encode(byte[] plaintext, CryptoMethod method) {
        try {
                return method.encodeWithoutKey(plaintext);
        } catch (InvalidKeyException e) {
            Log.e("Protocol", "Received protocol still requires a key, this should never happen");
            throw new IllegalArgumentException("Key not set");
        }
    }

    public byte[] encodeString(String plaintext, CryptoMethod method) {
        return this.encode(plaintext.getBytes(), method);
    }

    public void encode(byte[] plaintext) {
        CryptoSelectDialog newFragment = new CryptoSelectDialog();
        newFragment.setCallback(this);
        newFragment.show(rootActivity.getSupportFragmentManager(), "dialog");
        this.currentMode = TranscodingMode.ENCODE;
        this.data = plaintext;
    }

    public void encodeString(String plaintext) {
        this.encode(plaintext.getBytes());
    }


    public byte[] decode(byte[] ciphertext, CryptoMethod method) {
        try {
            return method.decodeWithoutKey(ciphertext);
        } catch (InvalidKeyException e) {
            Log.e("Protocol", "Received protocol still requires a key, this should never happen");
            throw new IllegalArgumentException("Key not set");
        }
    }

    public byte[] decodeString(String ciphertext, CryptoMethod method) {
        return this.decode(ciphertext.getBytes(), method);
    }

    public void decode(byte[] ciphertext) {
        CryptoSelectDialog newFragment = new CryptoSelectDialog();
        newFragment.setCallback(this);
        newFragment.show(rootActivity.getSupportFragmentManager(), "dialog");
        this.currentMode = TranscodingMode.DECODE;
        this.data = ciphertext;
    }

    public void decodeString(String ciphertext) {
        this.encode(ciphertext.getBytes());
    }

    public void onMethodSelected(CryptoMethod method) {
        Log.i("Protocol", "Selected protocol " + method.getName());
        switch (this.currentMode) {
            case ENCODE:
                callback.onTranscodeFinish(data, method, this.encode(data, method));
                break;
            case DECODE:
                callback.onTranscodeFinish(data, method, this.decode(data, method));
                break;
            default:
                throw new IllegalStateException("Received callback but no request is pending");
        }
        this.data = null;
        this.currentMode = TranscodingMode.NONE;
    }

    public static byte[] fromBase64(String b64) {
        return Base64.encode(b64.getBytes(), B64_FLAGS);
    }

    public static String stringFromBase64(String b64) {
        return Base64.encodeToString(b64.getBytes(), B64_FLAGS);
    }

    public static String toBase64(byte[] data) {
        return new String(Base64.decode(data, B64_FLAGS));
    }
}
