package ch.bruin.dev.l2.selectorDialog;

public interface BinaryDialogListener<T> {
    public void dialogPositiveResult(T data, String option);
    public void dialogNegativeResult(T data, String option);
}
