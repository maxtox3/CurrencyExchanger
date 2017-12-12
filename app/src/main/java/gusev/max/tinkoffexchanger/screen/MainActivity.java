package gusev.max.tinkoffexchanger.screen;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import gusev.max.tinkoffexchanger.R;
import gusev.max.tinkoffexchanger.screen.currency.CurrencyFragment;
import gusev.max.tinkoffexchanger.screen.history.HistoryFragment;
import gusev.max.tinkoffexchanger.screen.trends.TrendsFragment;

public class MainActivity extends AppCompatActivity {

    private static String FRAGMENT_NAME = "fragment_name";
    private static String CURRENCY = "currency";
    private static String HISTORY = "history";
    private static String TRENDS = "trends";

    private String tag = CURRENCY;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            tag = savedInstanceState.getString(FRAGMENT_NAME);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, createFragment(tag), tag)
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.exchange_button:
                    Fragment currency = getSupportFragmentManager().findFragmentByTag(CURRENCY);
                    if (currency == null) {
                        currency = new CurrencyFragment();
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, currency, CURRENCY)
                            .commit();
                    break;
                case R.id.history_button:
                    Fragment history = getSupportFragmentManager().findFragmentByTag(HISTORY);
                    if (history == null) {
                        history = new HistoryFragment();
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, history, HISTORY)
                            .commit();
                    break;
                case R.id.trends_button:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new TrendsFragment())
                            .commit();
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        outState.putString(FRAGMENT_NAME, currentFragment.getTag());
        super.onSaveInstanceState(outState);
    }

    private Fragment createFragment(String fragmentName) {

        Fragment fragment = null;

        switch (fragmentName) {

            case "history":
                fragment = new HistoryFragment();
                break;

            case "trends":
                fragment = new TrendsFragment();
                break;

            case "currency":
                fragment = new CurrencyFragment();
                break;
        }

        return fragment;
    }
}
