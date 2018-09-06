package ch.bruin.dev.l2.Crypto;

public class RSA extends CryptoMethod {
    public RSA(int keySize) {
        super("RSA" + keySize, "RSA", keySize, true);
    }

    @Override
    public byte[] encode(byte[] plaintext, byte[] key) {
        return plaintext;
    }

    @Override
    public byte[] decode(byte[] ciphertext, byte[] key) {
        return ciphertext;
    }

    @Override
    public String getShortDescription() {
        return "RSA" + this.getKeySize();
    }

    @Override
    public String getDescription() {
        return "RSA (Rivest–Shamir–Adleman) is one of the first public-key cryptosystems and is widely used for secure data transmission.";
    }

    @Override
    public CryptoStrength getStrength() {
        int len = getKeySize();
        if (len <= 8) {
            return CryptoStrength.NONE;
        } else if (len <= 64) {
            return CryptoStrength.WEAK;
        } else if (len <= 128) {
            return CryptoStrength.STRONG;
        } else {
            return CryptoStrength.UNBREAKABLE;
        }
    }
}
