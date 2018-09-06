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

    @Override
    public String getFamily() {
        return "RSA";
    }

    @Override
    public String getShortDescription() {
        return "RSA" + this.getKeySize();
    }

    @Override
    public String getDescription() {
        return "RSA (Rivest–Shamir–Adleman) is one of the first public-key cryptosystems and is widely used for secure data transmission.";
    }
}
