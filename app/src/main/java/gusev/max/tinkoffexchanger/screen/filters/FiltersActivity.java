package gusev.max.tinkoffexchanger.screen.filters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.vo.FilterVO;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;

public class FiltersActivity extends AppCompatActivity implements FiltersContract.View, FiltersAdapter.CurrencyListener {
    
    private final String ALL_TIME = "all_time";
    private final String WEEK = "week";
    private final String MONTH = "month";
    private final String PERIOD = "period";

    private FiltersPresenter presenter;
    private FiltersAdapter adapter;

    DatePickerDialog dialogDateFrom;
    DatePickerDialog dialogDateTo;

    @BindView(R.id.filter_radios)
    RadioGroup radioGroup;
    @BindView(R.id.all_time_radio)
    RadioButton allTimeRadio;
    @BindView(R.id.week_radio)
    RadioButton weekRadio;
    @BindView(R.id.month_radio)
    RadioButton monthRadio;
    @BindView(R.id.period_radio)
    RadioButton periodRadio;

    @BindView(R.id.date_from_button)
    Button dateFrom;
    @BindView(R.id.date_to_button)
    Button dateTo;

    @BindView(R.id.filters_recycler)
    RecyclerView recyclerView;

    @OnClick({R.id.all_time_radio, R.id.week_radio, R.id.month_radio, R.id.period_radio})
    public void onRadioButtonClicked(RadioButton radioButton) {

        switch (radioButton.getId()) {
            case R.id.all_time_radio:
                allTimeRadioClick(radioButton);
                break;

            case R.id.week_radio:
                weekRadioClick(radioButton);
                break;

            case R.id.month_radio:
                monthRadioClick(radioButton);
                break;

            case R.id.period_radio:
                periodRadioClick(radioButton);
                break;
        }
    }

    @OnClick(R.id.date_from_button)
    public void fromClicked() {
        dialogDateFrom.show();
    }

    @OnClick(R.id.date_to_button)
    public void toClicked() {
        dialogDateTo.show();
    }

    @OnClick(R.id.apply_filter_button)
    public void applyClicked() {
        presenter.saveFilter(getFilterVO());
        finish();
    }

    public static void start(Activity activity) {
        activity.startActivity(
                new Intent(activity, FiltersActivity.class)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);

        setupWidgets();

        presenter = new FiltersPresenter(this, RepositoryProvider.provideRepository());
    }

    private void setupWidgets() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new FiltersAdapter(new ArrayList<>(), new HashSet<>(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        dialogDateFrom = new DatePickerDialog(
                this,
                (datePicker, i, i1, i2) -> dateFrom.setText(dateFormatter(i, i1 + 1, i2)),
                2017, 1, 1);

        dialogDateTo = new DatePickerDialog(
                this,
                (datePicker, i, i1, i2) -> dateTo.setText(dateFormatter(i, i1 + 1, i2)),
                2017, 1, 1);
    }

    @Override
    public void onBackPressed() {
        presenter.saveFilter(null);
        super.onBackPressed();
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
    public void onCurrencyCheck(Set<String> checkers) {
        presenter.setCheckers(checkers);
    }

    @Override
    public void showCurrencies(List<String> currencies, Set<String> checks) {
        adapter.setData(currencies, checks);
    }

    @Override
    public void showPeriod(String period) {
        switch (period) {
            case ALL_TIME:
                showAllTimePeriod();
                break;
            case WEEK:
                showWeekPeriod();
                break;
            case MONTH:
                showMothPeriod();
                break;
            case PERIOD:
                showPeriodEdge();
                break;
        }
    }

    @Override
    public void showDateEdges(String from, String to) {
        dateFrom.setText(from);
        dateTo.setText(to);
    }

    private void showPeriodEdge() {
        allTimeRadio.setChecked(true);
        weekRadio.setChecked(false);
        monthRadio.setChecked(false);
        periodRadio.setChecked(true);
        dateFrom.setVisibility(View.VISIBLE);
        dateTo.setVisibility(View.VISIBLE);
    }

    private void showMothPeriod() {
        allTimeRadio.setChecked(false);
        weekRadio.setChecked(false);
        monthRadio.setChecked(true);
        periodRadio.setChecked(false);
        dateFrom.setVisibility(View.GONE);
        dateTo.setVisibility(View.GONE);
    }

    private void showWeekPeriod() {
        allTimeRadio.setChecked(false);
        weekRadio.setChecked(true);
        monthRadio.setChecked(false);
        periodRadio.setChecked(false);
        dateFrom.setVisibility(View.GONE);
        dateTo.setVisibility(View.GONE);
    }

    private void showAllTimePeriod() {
        allTimeRadio.setChecked(true);
        weekRadio.setChecked(false);
        monthRadio.setChecked(false);
        periodRadio.setChecked(false);
        dateFrom.setVisibility(View.GONE);
        dateTo.setVisibility(View.GONE);
    }

    private void allTimeRadioClick(RadioButton radioButton){
        if (radioButton.isChecked()) {
            dateFrom.setVisibility(View.GONE);
            dateTo.setVisibility(View.GONE);
        }
        presenter.setPeriodType(ALL_TIME);
    }

    private void  weekRadioClick(RadioButton radioButton){
        if (radioButton.isChecked()) {
            dateFrom.setVisibility(View.GONE);
            dateTo.setVisibility(View.GONE);
        }
        presenter.setPeriodType(WEEK);
    }

    private void monthRadioClick(RadioButton radioButton){
        if (radioButton.isChecked()) {
            dateFrom.setVisibility(View.GONE);
            dateTo.setVisibility(View.GONE);
        }
        presenter.setPeriodType(MONTH);
    }

    private void periodRadioClick(RadioButton radioButton){
        if (radioButton.isChecked()) {
            dateFrom.setVisibility(View.VISIBLE);
            dateTo.setVisibility(View.VISIBLE);
        }
        presenter.setPeriodType(PERIOD);
    }


    private String dateFormatter(int year, int month, int day) {
        String result;
        if (day < 10) {
            if (month < 10) {
                result = "0" + day + "/0" + month + "/" + year;
            } else {
                result = "0" + day + "/" + month + "/" + year;
            }
        } else {
            if (month < 10) {
                result = day + "/0" + month + "/" + year;
            } else {
                result = day + "/" + month + "/" + year;
            }
        }

        return result;
    }

    private FilterVO getFilterVO() {
        return new FilterVO(
                adapter.getCurrencies(),
                adapter.getChecked(),
                getPeriodType(),
                dateTo.getText().toString(),
                dateFrom.getText().toString()
        );
    }

    private String getPeriodType() {
        String type;
        switch (radioGroup.getCheckedRadioButtonId()) {

            case R.id.all_time_radio:
                type = ALL_TIME;
                break;
            case R.id.week_radio:
                type = WEEK;
                break;
            case R.id.month_radio:
                type = MONTH;
                break;
            case R.id.period_radio:
                type = PERIOD;
                break;

            default:
                type = ALL_TIME;
        }

        return type;
    }
}
