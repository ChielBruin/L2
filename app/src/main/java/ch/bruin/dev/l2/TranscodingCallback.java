package ch.bruin.dev.l2;

import ch.bruin.dev.l2.Crypto.CryptoMethod;

public interface TranscodingCallback {
    public enum TranscodingMode {
        ENCODE,
        DECODE,
        NONE;
    }

    public void onTranscodeFinish(TranscodingMode mode, byte[] original, CryptoMethod method, byte[] result);
}
