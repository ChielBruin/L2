package ch.bruin.dev.l2.Crypto;

public class RSA extends CryptoMethod {
    public RSA(int keySize) {
        super("RSA" + keySize, "RSA", keySize, true);
    }

    @Override
    public byte[] encode(byte[] plaintext, String key) {
        return plaintext;
    }

    @Override
    public byte[] decode(byte[] ciphertext, String key) {
        return ciphertext;
    }
}
