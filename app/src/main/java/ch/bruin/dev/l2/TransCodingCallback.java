package ch.bruin.dev.l2;

import ch.bruin.dev.l2.Crypto.CryptoMethod;

public interface TransCodingCallback {
    public void onTranscodeFinish(byte[] original, CryptoMethod method, byte[] result);
}
