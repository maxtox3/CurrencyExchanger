package gusev.max.tinkoffexchanger.screen.currency;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;
import gusev.max.tinkoffexchanger.data.model.vo.CurrencyVO;
import gusev.max.tinkoffexchanger.screen.exchange.ExchangeActivity;

public class CurrencyFragment extends Fragment implements CurrencyContract.View, CurrencyViewHolder.CurrencyListener {

    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.progress_bar)
    ProgressBar loadingView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.long_selected_currency)
    TextView longSelectedCurrencyView;
    @BindView(R.id.top_currency_layout)
    CardView topCurrencyLayout;
    @BindView(R.id.all_currencies_text_view)
    TextView allCurrenciesLabel;
    @BindView(R.id.retry_button)
    Button retry;

    @OnClick(R.id.retry_button)
    public void retryButtonClicked() {
        presenter.refreshCurrencies();
    }

    private CurrencyPresenter presenter;
    private CurrencyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency, container, false);
        ButterKnife.bind(this, view);

        adapter = new CurrencyAdapter(inflater, this);
        setHasOptionsMenu(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        presenter = new CurrencyPresenter(this);
        if(view != null) {
            return view;
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.currency_menu, menu);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_refresh:
                presenter.refreshCurrencies();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showCurrencies(CurrencyVO currencies) {
        TransitionManager.beginDelayedTransition(container);
        retry.setVisibility(View.GONE);
        allCurrenciesLabel.setVisibility(View.VISIBLE);
        adapter.setCurrencies(currencies.getCurrencyList());
        if (currencies.getOnTopCurrency().getBase() != null) {
            longSelectedCurrencyView.setText(currencies.getOnTopCurrency().getBase());
            topCurrencyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError() {
        TransitionManager.beginDelayedTransition(container);
        topCurrencyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        allCurrenciesLabel.setVisibility(View.GONE);
        retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading(Boolean show) {
        TransitionManager.beginDelayedTransition(container);
        if(show) {
            loadingView.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCurrencyClicked(Currency currency) {
        presenter.onCurrencyClicked(currency);
        ExchangeActivity.start(getActivity());
    }

    @Override
    public void onCurrencyLongClicked(Currency currency) {
        presenter.onCurrencyLongCLick(currency);
    }

    @Override
    public void onStarClicked(Currency currency) {
        presenter.onStarButtonClick(currency);
    }

}
