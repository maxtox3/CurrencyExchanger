package gusev.max.tinkoffexchanger.data.model.dto;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "exchange")
public class Exchange {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @SerializedName("base_from")
    private String baseFrom;

    @SerializedName("base_to")
    private String baseTo;

    @SerializedName("amount_from")
    private Double amountFrom;

    @SerializedName("amount_to")
    private Double amountTo;

    @SerializedName("date")
    private Long date;

    @Ignore
    public Exchange(String baseFrom, String baseOfCurrencyTo, Double amountFrom, Double amountTo, Long date) {
        this.baseFrom = baseFrom;
        this.baseTo = baseOfCurrencyTo;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
        this.date = date;
    }

    public Exchange(int id, String baseFrom, String baseTo, Double amountFrom, Double amountTo, Long date) {
        this.id = id;
        this.baseFrom = baseFrom;
        this.baseTo = baseTo;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBaseFrom(@NonNull String baseFrom) {
        this.baseFrom = baseFrom;
    }

    public void setBaseTo(String baseTo) {
        this.baseTo = baseTo;
    }

    public void setAmountFrom(Double amountFrom) {
        this.amountFrom = amountFrom;
    }

    public void setAmountTo(Double amountTo) {
        this.amountTo = amountTo;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getBaseFrom() {
        return baseFrom;
    }

    public String getBaseTo() {
        return baseTo;
    }

    public Double getAmountFrom() {
        return amountFrom;
    }

    public Double getAmountTo() {
        return amountTo;
    }

    public Long getDate() {
        return date;
    }
}
