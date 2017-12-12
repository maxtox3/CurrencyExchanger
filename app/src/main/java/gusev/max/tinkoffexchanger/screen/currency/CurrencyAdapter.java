package gusev.max.tinkoffexchanger.screen.currency;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import gusev.max.tinkoffexchanger.data.model.dto.Currency;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {

    private final LayoutInflater inflater;

    private CurrencyViewHolder.CurrencyListener currencyClickedListener;

    private List<Currency> currencyList;

    public CurrencyAdapter(LayoutInflater inflater,
                           CurrencyViewHolder.CurrencyListener currencyClickedListener) {
        this.inflater = inflater;
        this.currencyClickedListener = currencyClickedListener;
    }

    void setCurrencies(List<Currency> currencies) {
        currencyList = currencies;
        notifyDataSetChanged();
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CurrencyViewHolder.create(inflater, currencyClickedListener);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        holder.bind(currencyList.get(position));
    }

    @Override
    public int getItemCount() {
        return currencyList == null ? 0 : currencyList.size();
    }


}
