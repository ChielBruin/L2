package ch.bruin.dev.l2;

import ch.bruin.dev.l2.Crypto.CryptoMethod;

public interface CryptoSelectionCallback {
    public void onSelect(CryptoMethod method, byte[] key);
}
