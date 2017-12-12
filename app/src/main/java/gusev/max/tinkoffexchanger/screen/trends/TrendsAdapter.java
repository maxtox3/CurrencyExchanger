package gusev.max.tinkoffexchanger.screen.trends;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.TrendsViewHolder>{

    private final String YELLOW = "#FFF59D";
    private final String WHITE = "#FFFFFF";
    private List<Currency> currencies;
    private OnItemClickListener listener;
    private int positionSelected = 0;

    TrendsAdapter(List<Currency> currencies, OnItemClickListener listener) {
        this.currencies = currencies;
        this.listener = listener;
    }

    void select(int position) {
        positionSelected = position;
        notifyDataSetChanged();
    }

    @Override
    public TrendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_trends, parent, false);
        return new TrendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrendsViewHolder holder, int position) {
        holder.currency.setText(currencies.get(position).getBase());

        if (position == positionSelected) {
            holder.itemView.setBackgroundColor(Color.parseColor(YELLOW));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(WHITE));
        }

        if (listener != null) {
            holder.bind(currencies.get(position), listener, position);
        }
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    void setSelectedCurrency(String selectedCurrency) {
        for(int i = 0; i < currencies.size(); i++){
            if(currencies.get(i).getBase().equals(selectedCurrency))
                positionSelected = i;
        }
        notifyDataSetChanged();
    }

    int getSelectedCurrencyPosition() {
        return positionSelected;
    }

    public interface OnItemClickListener {
        void onItemClickListener(Currency currency, int position);
    }

    class TrendsViewHolder extends RecyclerView.ViewHolder {

        TextView currency;

        TrendsViewHolder(View itemView) {
            super(itemView);

            currency = itemView.findViewById(R.id.trends_currency);
        }

        public void bind(final Currency item,final OnItemClickListener listener,int position) {
            itemView.setOnClickListener(view -> listener.onItemClickListener(item, position));
        }
    }
}
