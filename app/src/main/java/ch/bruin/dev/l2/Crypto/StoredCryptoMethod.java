package ch.bruin.dev.l2.Crypto;

public class StoredCryptoMethod extends CryptoMethod {
    private byte[] key;
    private CryptoMethod protocol;

    public StoredCryptoMethod(String name, CryptoMethod protocol, byte[] key) {
        super(name, protocol.getName(), protocol.getKeySize(), false);
        this.key = key;
        this.protocol = protocol;
        if (key.length * 8 != protocol.getKeySize()) throw new IllegalArgumentException("Key size incorrect, expected " + protocol.getKeySize());
    }

    @Override
    public byte[] encode(byte[] plaintext, byte[] key) {
        return protocol.encode(plaintext, key);
    }

    @Override
    public byte[] decode(byte[] ciphertext, byte[] key) {
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
    public String getProtocolFamily() {
        String name = this.getName();
        if (name.length() > 4) {
            name = name.substring(0, 3);
        }
        return name;
    }

    @Override
    public String getShortDescription() {
        return protocol.getShortDescription();
    }

    @Override
    public String getDescription() {
        return protocol.getDescription();
    }

    @Override
    public CryptoStrength getStrength() {
        return protocol.getStrength();
    }
}
