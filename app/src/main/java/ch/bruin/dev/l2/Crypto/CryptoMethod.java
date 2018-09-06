package ch.bruin.dev.l2.Crypto;

import java.security.InvalidKeyException;

public abstract class CryptoMethod {
    public enum CryptoStrength {
        NONE,
        WEAK,
        STRONG,
        UNBREAKABLE;
    }

    private String name;
    private int keySize;
    private String protocolFamily;
    private boolean requiresKey;

    public CryptoMethod(String name, String protocolFamily, int keySize, boolean requiresKey) {
        if (keySize % 8 != 0) throw new IllegalArgumentException("Key size must be a multiple of 8");
        this.name = name;
        this.keySize = keySize;
        this.requiresKey = requiresKey;
        this.protocolFamily = protocolFamily;
    }

    public abstract byte[] encode(byte[] plaintext,  byte[] key);
    public abstract byte[] decode(byte[] ciphertext, byte[] key);


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

    public String getProtocolFamily() {
        return protocolFamily;
    }

    public abstract String getShortDescription();

    public abstract String getDescription();

    public abstract CryptoStrength getStrength();
}