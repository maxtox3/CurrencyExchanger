package gusev.max.tinkoffexchanger.screen.exchange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.screen.widgets.ProgressButton;

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
    private Double multiplier;

    @OnTextChanged(value = R.id.amount_from, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void fromAmountChanged(Editable editable) {
        if (!editable.toString().equals("")
                && amountFrom.hasFocus()
                && !editable.toString().equals(".")) {
            amountTo.setText(formatFloat(Double.parseDouble(editable.toString()) * multiplier));
            presenter.onFieldsChange(getExchangeVO());
        }
    }

    @OnTextChanged(value = R.id.amount_to, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void toAmountChanged(Editable editable) {
        if (!editable.toString().equals("")
                && Double.parseDouble(editable.toString()) != 0
                && amountTo.hasFocus()
                && !editable.toString().equals(".")) {
            amountFrom.setText(formatFloat(Double.parseDouble(editable.toString()) / multiplier));
            presenter.onFieldsChange(getExchangeVO());
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
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new ExchangePresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showRates(ExchangeVO rates) {
        multiplier = rates.getAmountTo();
        fromLabel.setText(rates.getBaseFrom());
        toLabel.setText(rates.getBaseTo());
        amountFrom.setText(rates.getAmountFrom().toString());
        amountTo.setText(formatFloat(rates.getAmountTo()));
    }

    @Override
    public void showLoading(Boolean show) {
        if(show) {
            exchangeBtn.showProgress();
        } else {
            exchangeBtn.hideProgress();
        }
    }

    @Override
    public void enableFields(Boolean enable) {
        if(enable){
            amountFrom.setEnabled(true);
            amountTo.setEnabled(true);
        } else {
            amountFrom.setEnabled(false);
            amountTo.setEnabled(false);
        }
    }

    @Override
    public void showDialog() {
        DialogFragment newFragment = ExchangeDialogFragment.newInstance(getExchangeVO());
        newFragment.show(getSupportFragmentManager(), DIALOG);
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

    public void okClicked(){
        presenter.exchange(getExchangeVO());
    }

    public void cancelClicked() {
        presenter.getRates();
    }

    private String formatFloat(Double number) {
        return new DecimalFormat("#.###").format(number);
    }
}
