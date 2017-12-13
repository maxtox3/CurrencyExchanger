package gusev.max.tinkoffexchanger.screen.exchange;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;

public class ExchangeDialogFragment extends DialogFragment {

    private static String KEY_OBJECT = "object";
    private ExchangeVO viewObject;

    public static ExchangeDialogFragment newInstance(ExchangeVO viewObject) {
        ExchangeDialogFragment f = new ExchangeDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_OBJECT, viewObject);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewObject = (ExchangeVO) getArguments().getSerializable(KEY_OBJECT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Актуальный курс: ")
                .setMessage(buildMessage())
                .setPositiveButton("Обменять", (dialog, id) -> ((ExchangeActivity) getActivity()).okClicked(viewObject))
                .setNegativeButton("Отмена", (dialog, id) -> ((ExchangeActivity) getActivity()).cancelClicked())
                .setCancelable(true);

        return builder.create();
    }

    private String buildMessage() {
        return viewObject.getAmountFrom() + " => " + viewObject.getAmountTo();
    }
}
