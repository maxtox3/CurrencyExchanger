package gusev.max.tinkoffexchanger.screen.trends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;

public class TrendsFragment extends Fragment implements TrendsContract.View {

    private final String WEEK = "week";
    private final String TWO_WEEKS = "two_weeks";
    private final String MONTH = "month";
    Unbinder unbinder;
    private TrendsPresenter presenter;
    private TrendsAdapter adapter;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.trends_progress)
    ProgressBar progressBar;
    @BindView(R.id.trends_radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.week_trends_radio)
    RadioButton weekRadioBtn;
    @BindView(R.id.two_weeks_trends_radio)
    RadioButton twoWeeksRadioBtn;
    @BindView(R.id.month_trends_radio)
    RadioButton monthRadioButton;
    @BindView(R.id.analytics_chart)
    LineChart lineChart;
    @BindView(R.id.trends_recycler)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trends, null);
        unbinder = ButterKnife.bind(this, view);

        setupWidgets();

        presenter = new TrendsPresenter(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void showCurrencies(List<Currency> currencies) {
        setupRecycler(currencies);
    }

    @Override
    public void setPeriod(String period) {
        switch (period) {
            case WEEK:
                weekRadioBtn.setChecked(true);
                break;

            case TWO_WEEKS:
                twoWeeksRadioBtn.setChecked(true);
                break;

            case MONTH:
                monthRadioButton.setChecked(true);
                break;
        }
    }

    @Override
    public void setSelectedCurrency(String baseOfSelectedCurrency) {
        adapter.setSelectedCurrency(baseOfSelectedCurrency);
        layoutManager.scrollToPositionWithOffset(adapter.getSelectedCurrencyPosition(), 20);
    }

    @Override
    public void setRates(List<Float> rates) {
        buildCharts(rates);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading(Boolean show) {
        if(show){
            progressBar.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.INVISIBLE);
        } else {
            lineChart.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

    }

    private void setupWidgets() {
        layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.week_trends_radio:
                    presenter.onPeriodSelect(WEEK);
                    break;

                case R.id.two_weeks_trends_radio:
                    presenter.onPeriodSelect(TWO_WEEKS);
                    break;

                case R.id.month_trends_radio:
                    presenter.onPeriodSelect(MONTH);
                    break;
            }
        });

        setupRecycler(new ArrayList<>());
    }

    private void setupRecycler(List<Currency> currencies) {
        adapter = new TrendsAdapter(currencies,
                (currency, position) -> {
                    adapter.select(position);
                    presenter.onCurrencySelect(currency);
                });
        recyclerView.setAdapter(adapter);
    }

    private void buildCharts(List<Float> rates) {
        TransitionManager.beginDelayedTransition(lineChart);
        List<Entry> values = new ArrayList<>();

        for (int i = 0; i < rates.size(); i++) {
            values.add(new Entry(i, rates.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(values, "Rates");
        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}
