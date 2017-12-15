package gusev.max.tinkoffexchanger.screen.exchange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;
import gusev.max.tinkoffexchanger.screen.widgets.ProgressButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExchangeActivity extends AppCompatActivity implements ExchangeContract.View {

    @BindView(R.id.currency_from_text_view)
    TextView fromLabel;
    @BindView(R.id.currency_to_text_view)
    TextView toLabel;
    @BindView(R.id.amount_from)
    EditText amountFrom;
    @BindView(R.id.amount_to)
    EditText amountTo;
    @BindView(R.id.exchange_button)
    ProgressButton exchangeBtn;

    private final String DIALOG = "dialog";
    private final String EXCHANGED = "Exchanged!";
    private ExchangePresenter presenter;
    private boolean blockTo;
    private boolean blockFrom;
    private double multiplier;

    @OnTextChanged(value = R.id.amount_from, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void fromAmountChanged(Editable editable) {
        if (editable.toString().isEmpty()) {
            blockFields(true);
            amountTo.setText("0.0");
            amountFrom.setText("0.0");
            blockFields(false);
        }

        if (!blockFrom & amountFrom.hasFocus()
                & !editable.toString().isEmpty()
                & !editable.toString().equals(".")) {
            blockTo = true;
            amountTo.setText(Double.parseDouble(editable.toString()) * multiplier + "");
            blockTo = false;
            presenter.onFieldsChange(getExchangeVO(), true);
        }
    }

    @OnTextChanged(value = R.id.amount_to, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void toAmountChanged(Editable editable) {
        if (editable.toString().isEmpty()) {
            blockFields(true);
            amountTo.setText("0.0");
            amountFrom.setText("0.0");
            blockFields(false);
        }

        if (!blockTo & amountTo.hasFocus()
                & !editable.toString().isEmpty()
                & !editable.toString().equals(".")) {
            blockFrom = true;
            amountFrom.setText(Double.parseDouble(editable.toString()) / multiplier + "");
            blockFrom = false;
            presenter.onFieldsChange(getExchangeVO(), true);
        }
    }

    @OnClick(R.id.exchange_button)
    public void exchangeButtonClicked() {
        presenter.onExchangeButtonClick(getExchangeVO());
    }

    public static void start(Activity activity) {
        activity.startActivity(
                new Intent(activity, ExchangeActivity.class)
        );
    }

    @Override
    public void onBackPressed() {
        presenter.cacheExchange(null);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new ExchangePresenter(this, RepositoryProvider.provideRepository(), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading(Boolean show) {
        if (show) {
            exchangeBtn.showProgress();
        } else {
            exchangeBtn.hideProgress();
        }
    }

    @Override
    public void enableFields(Boolean enable) {
        if (enable) {
            amountFrom.setEnabled(true);
            amountTo.setEnabled(true);
        } else {
            amountFrom.setEnabled(false);
            amountTo.setEnabled(false);
        }
    }

    @Override
    public void showDialog(ExchangeVO exchangeVO) {
        ExchangeDialogFragment
                .newInstance(exchangeVO)
                .show(getSupportFragmentManager(), DIALOG);
    }

    @Override
    public void showSuccess() {
        Toast.makeText(this, EXCHANGED, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    public ExchangeVO getExchangeVO() {
        return new ExchangeVO(
                fromLabel.getText().toString(),
                toLabel.getText().toString(),
                Double.parseDouble(amountFrom.getText().toString()),
                Double.parseDouble(amountTo.getText().toString())
        );
    }

    @Override
    public void showRatesAfterLoading(String baseFrom, String baseTo, Double valueFrom, Double valueTo) {
        blockFields(true);
        fromLabel.setText(baseFrom);
        toLabel.setText(baseTo);
        multiplier = valueTo;
        amountFrom.setText(formatFloatToString(valueFrom));
        amountTo.setText(formatFloatToString(valueTo));
        blockFields(false);
    }

    private void blockFields(Boolean block){
        blockTo = block;
        blockFrom = block;
    }

    public void okClicked(ExchangeVO exchangeVO) {
        presenter.exchange(exchangeVO);
    }

    public void cancelClicked() {
        presenter.getRates();
    }

    private String formatFloatToString(Double number) {
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormatSymbols sfs = new DecimalFormatSymbols();
        sfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sfs);
        return df.format(number);
    }
}
