package ch.bruin.dev.l2.selectorDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class BinaryDialogWrapper {

    public static <T> void ask(Context context, String query, final String positive, final String negative, final BinaryDialogListener<T> callback, final T data) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        callback.dialogPositiveResult(data, positive);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        callback.dialogNegativeResult(data, negative);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(query).setPositiveButton(positive, dialogClickListener)
                .setNegativeButton(negative, dialogClickListener).show();
    }
}
