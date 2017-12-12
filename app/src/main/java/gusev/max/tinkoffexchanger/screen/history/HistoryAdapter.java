package gusev.max.tinkoffexchanger.screen.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.vo.ExchangeVO;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{


    private List<ExchangeVO> exchanges;

    HistoryAdapter(List<ExchangeVO> exchanges) {
        this.exchanges = exchanges;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exchange, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.fromCurrency.setText(exchanges.get(position).getBaseFrom());
        holder.toCurrency.setText(exchanges.get(position).getBaseTo());
        holder.fromAmount.setText(formatFloat(exchanges.get(position).getAmountFrom()));
        holder.toAmount.setText(formatFloat(exchanges.get(position).getAmountTo()));
    }

    @Override
    public int getItemCount() {
        return exchanges.size();
    }

    void setExchanges(List<ExchangeVO> exchanges) {
        this.exchanges = exchanges;
        notifyDataSetChanged();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView fromCurrency;
        TextView toCurrency;
        TextView fromAmount;
        TextView toAmount;

        HistoryViewHolder(View itemView) {
            super(itemView);

            fromCurrency = itemView.findViewById(R.id.base_from_exchange);
            toCurrency = itemView.findViewById(R.id.base_to_exchange);
            fromAmount = itemView.findViewById(R.id.amount_from_exchange);
            toAmount = itemView.findViewById(R.id.amount_to_exchange);
        }

    }

    private String formatFloat(Double number) {
        return new DecimalFormat("#.##").format(number);
    }
}
