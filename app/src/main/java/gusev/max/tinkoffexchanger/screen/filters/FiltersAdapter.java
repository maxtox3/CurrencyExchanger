package gusev.max.tinkoffexchanger.screen.filters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;
import java.util.Set;

import gusev.max.tinkoffexchanger.R;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FilterHolder> {

    public interface CurrencyListener {
        void onCurrencyCheck(Set<String> checkers);
    }

    private List<String> currencies;
    private Set<String> checked;
    private CurrencyListener currencyClickedListener;

    FiltersAdapter(List<String> currencies, Set<String> checked, CurrencyListener checkListener) {
        this.currencies = currencies;
        this.checked = checked;
        this.currencyClickedListener = checkListener;
    }

    @Override
    public FiltersAdapter.FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter_currency, parent, false);

        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(FiltersAdapter.FilterHolder holder, int position) {
        holder.checkBox.setText(currencies.get(position));

        for (String each: checked) {
            if (each.equals(currencies.get(position))) {
                holder.checkBox.setChecked(true);
            }
        }
        holder.bind(currencies.get(position));
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    Set<String> getChecked() {
        return checked;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setData(List<String> currencies, Set<String> checked){
        this.currencies = currencies;
        this.checked = checked;
        notifyDataSetChanged();
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        FilterHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.currency_check_box);
        }

        public void bind(final String item) {
            checkBox.setOnClickListener(view -> {
                if (checkBox.isChecked()) {
                    checked.add(item);
                } else {
                    checked.remove(item);
                }
                currencyClickedListener.onCurrencyCheck(getChecked());
            });
        }
    }
}
