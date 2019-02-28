package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * https://www.okex.com/docs/zh/#spot-line
 */
public class CandleRes {
    @SerializedName("time")
    private String time;

    @SerializedName("open")
    private Double open;

    @SerializedName("high")
    private Double high;

    @SerializedName("low")
    private Double low;

    @SerializedName("close")
    private Double close;

    @SerializedName("volume")
    private Double volume;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }
}
