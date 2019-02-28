package com.coin.libbase.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/6
 * @description WebSocket通用的返回值外壳
 * bitmex: {"table":"instrument","action":"update","data":[{"symbol":"XBTUSD","openValue":10283699963520,"fairPrice":3805.18,"markPrice":3805.18,"timestamp":"2019-02-28T04:50:50.386Z"}]}
 * okex: [{"binary":1,"channel":"addChannel","data":{"result":true,"channel":"ok_sub_futureusd_eos_ticker_quarter"}}]
 */
public class CommonRes {
    // 具体数据
    @SerializedName("data")
    private Object data;

    // 请求数据类型  okex
    @SerializedName("channel")
    private String channel;

    // 请求数据类型 bitmex
    @SerializedName("table")
    private String table;
    // 操作类型 bitmex
    @SerializedName("action")
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "CommonRes{" +
                "data=" + data +
                ", channel='" + channel + '\'' +
                ", table='" + table + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
