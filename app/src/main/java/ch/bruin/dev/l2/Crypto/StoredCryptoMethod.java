package ch.bruin.dev.l2.Crypto;

public class StoredCryptoMethod extends CryptoMethod {
    private String key;
    private CryptoMethod protocol;

    public StoredCryptoMethod(String name, CryptoMethod protocol, String key) {
        super(name, protocol.getName(), protocol.getKeySize(), false);
        this.key = key;
        this.protocol = protocol;
        if (key.length() != protocol.getKeySize()) throw new IllegalArgumentException("Key size incorrect, expected " + protocol.getKeySize());
    }

    @Override
    public byte[] encode(byte[] plaintext, String key) {
        return protocol.encode(plaintext, key);
    }

    @Override
    public byte[] decode(byte[] ciphertext, String key) {
        return protocol.decode(ciphertext, key);
    }

    @Override
    public byte[] encodeWithoutKey(byte[] plaintext) {
        return protocol.encode(plaintext, key);
    }

    @Override
    public byte[] decodeWithoutKey(byte[] ciphertext) {
        return protocol.decode(ciphertext, key);
    }

    @Override
    public String getFamily() {
        return this.getName();
    }

    @Override
    public String getShortDescription() {
        return protocol.getShortDescription();
    }

    @Override
    public String getDescription() {
        return protocol.getDescription();
    }
}
