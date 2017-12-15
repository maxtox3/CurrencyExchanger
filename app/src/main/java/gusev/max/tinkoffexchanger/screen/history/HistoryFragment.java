package gusev.max.tinkoffexchanger.screen.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;
import gusev.max.tinkoffexchanger.data.repository.RepositoryProvider;
import gusev.max.tinkoffexchanger.screen.filters.FiltersActivity;

public class HistoryFragment extends Fragment implements HistoryContract.View {

    private Unbinder unbinder;
    private HistoryAdapter adapter;
    private HistoryPresenter presenter;

    @BindView(R.id.historyRecyclerView) RecyclerView recyclerView;

    @BindView(R.id.label_checks)
    TextView labelChecks;
    @BindView(R.id.checks_field)
    TextView checksField;
    @BindView(R.id.label__period)
    TextView labelPeriod;
    @BindView(R.id.period_field)
    TextView periodField;
    @BindView(R.id.label_date_from_to)
    TextView labelDateFromTo;
    @BindView(R.id.date_from_to_field)
    TextView dateFromToField;
    @BindView(R.id.empty_history_label)
    TextView emptyHistory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);
        unbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        adapter = new HistoryAdapter(new ArrayList<>()) ;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        presenter = new HistoryPresenter(this, RepositoryProvider.provideRepository());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                startFiltersActivity();
                break;
        }
        return true;
    }

    @Override
    public void showExchanges(List<ExchangeVO> exchanges) {
        TransitionManager.beginDelayedTransition(recyclerView);
        adapter.setExchanges(exchanges);
    }

    @Override
    public void showChecks(String checks) {
        if(checks == null){
            labelChecks.setVisibility(View.GONE);
            checksField.setVisibility(View.GONE);
        } else {
            labelChecks.setVisibility(View.VISIBLE);
            checksField.setVisibility(View.VISIBLE);
            checksField.setText(checks);
        }
    }

    @Override
    public void showPeriodType(String periodType) {
        labelPeriod.setVisibility(View.VISIBLE);
        periodField.setVisibility(View.VISIBLE);
        periodField.setText(periodType);

    }

    @Override
    public void showDateToDateFrom(String s) {
        if (s == null) {
            labelDateFromTo.setVisibility(View.GONE);
            dateFromToField.setVisibility(View.GONE);
        } else {
            labelDateFromTo.setVisibility(View.VISIBLE);
            dateFromToField.setVisibility(View.VISIBLE);
            dateFromToField.setText(s);
        }
    }

    @Override
    public void showEmptyHistory() {
        emptyHistory.setVisibility(View.VISIBLE);
    }

    private void startFiltersActivity() {
        FiltersActivity.start(getActivity());
    }
}
