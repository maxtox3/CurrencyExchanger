package gusev.max.tinkoffexchanger.screen.currency;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.data.model.dto.Currency;

class CurrencyViewHolder extends RecyclerView.ViewHolder {

    public interface CurrencyListener {
        void onCurrencyClicked(Currency currency);
        void onCurrencyLongClicked(Currency currency);
        void onStarClicked(Currency currency);
    }

    static CurrencyViewHolder create(
            LayoutInflater inflater,
            CurrencyListener clickedListener) {

        return new CurrencyViewHolder(inflater.inflate(R.layout.item_currency,
                null,
                false),
                clickedListener);

    }

    @BindView(R.id.currency_name)
    TextView currencyName;
    @BindView(R.id.star)
    ImageButton starImageButton;

    private Currency mCurrency;

    private CurrencyViewHolder(
            View itemView,
            CurrencyListener clickedListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> clickedListener.onCurrencyClicked(mCurrency));
        itemView.setOnLongClickListener(v -> {
            clickedListener.onCurrencyLongClicked(mCurrency);
            return true;
        });
        starImageButton.setOnClickListener(v -> clickedListener.onStarClicked(mCurrency));
    }

    void bind(Currency currency) {
        mCurrency = currency;

        if(currency.isLiked()){
            starImageButton.setBackgroundResource(R.drawable.star_selected);
        } else {
            starImageButton.setBackgroundResource(R.drawable.star);
        }
        currencyName.setText(currency.getBase());
    }

}
