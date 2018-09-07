package ch.bruin.dev.l2.selectorDialog;

public interface BinaryDialogListener<T> {
    public void dialogResult(T data, boolean positive);
}
