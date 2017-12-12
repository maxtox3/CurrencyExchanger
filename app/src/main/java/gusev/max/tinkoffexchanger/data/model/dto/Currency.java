package gusev.max.tinkoffexchanger.data.model.dto;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "currency")
public class Currency implements Comparable{

    @SerializedName("base")
    @PrimaryKey
    @NonNull
    private String base;

    @SerializedName("liked")
    private boolean liked;

    @SerializedName("last_used")
    private long lastUsed;

    @SerializedName("rate")
    private double rate;

    public Currency(){}

    public Currency(@NonNull String base, boolean liked, long lastUsed, double rate) {
        this.base = base;
        this.liked = liked;
        this.lastUsed = lastUsed;
        this.rate = rate;
    }

    @NonNull
    public String getBase() {
        return base;
    }

    public void setBase(@NonNull String base) {
        this.base = base;
    }

    public boolean isLiked() {
        return liked;
    }

    public int getLiked(){

        if(liked) {
            return 1;
        }
        return 0;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setLiked(int liked){
        this.liked = liked == 1;
    }

    public void changeLiked(){
        liked = !liked;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Currency another = (Currency) o;

        if (this.isLiked() && !another.isLiked()) {
            return -1;
        } else if (!this.isLiked() && another.isLiked()) {
            return 1;
        } else {
            return (int) (another.getLastUsed() - this.getLastUsed());
        }
    }
}
