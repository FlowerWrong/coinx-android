package com.coin.exchange.mvp.KLine;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.adapter.KlineTradeAdapter;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.config.okEx.ChannelHelper;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.database.CollectionModel;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTradesList;
import com.coin.exchange.mvp.KLine.di.DaggerKLineComponent;
import com.coin.exchange.mvp.KLine.di.KLineModule;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.GsonUtils;
import com.coin.exchange.utils.NumberUtil;
import com.coin.exchange.view.TradeActivity;
import com.coin.exchange.webSocket.bitMex.BitMEXWebSocketManager;
import com.coin.exchange.webSocket.okEx.okExFuture.FutureWebSocketManager;
import com.google.gson.reflect.TypeToken;
import com.coin.libbase.model.CommonRes;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.activity.JBaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;


public final class KLineActivity extends JBaseActivity<KLinePresenter> implements KLineView {

    private DecimalFormat df = new DecimalFormat("######0.00");
    private DecimalFormat df4 = new DecimalFormat("######0.0000");
    @NonNull
    private final List<FuturesInstrumentsTradesList> tradesList = new ArrayList<>();
    @Inject
    PreferenceManager preferenceManager;
    @Inject
    CollectionModel collectionModel;

    @BindView(R.id.time_show_line)
    View time_show_line;

    @BindView(R.id.iv_kline_back)
    ImageView ivKlineBack;
    @BindView(R.id.tv_kline_currency)
    TextView tvKlineCurrency;
    @BindView(R.id.kline_trade_time)
    TextView kline_trade_time;
    @BindView(R.id.rl_kline_top)
    RelativeLayout rlKlineTop;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_kline_up_down)
    ImageView ivKlineUpDown;
    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.tv_percent_prise)
    TextView tvPercentPrise;
    @BindView(R.id.tv_kline_24high_value)
    TextView tvKline24highValue;
    @BindView(R.id.tv_kline_market)
    TextView tvKlineMarket;
    @BindView(R.id.tv_kline_market_value)
    TextView tvKlineMarketValue;
    @BindView(R.id.tv_kline_24low)
    TextView tvKline24low;
    @BindView(R.id.tv_kline_24number)
    TextView tvKline24number;
    @BindView(R.id.ll_kline_value_show)
    LinearLayout llKlineValueShow;
    @BindView(R.id.time_bottom_show_line)
    View timeBottomShowLine;

    @BindView(R.id.ll_kline_bottom)
    LinearLayout llKlineBottom;
    @BindView(R.id.rv_kline_trade)
    RecyclerView rvOptionalList;
    @BindView(R.id.ll_trade_list_show)
    LinearLayout ll_trade_list_show;

    private KlineTradeAdapter klineTradeAdapter;

    private String instrumentId = "";
    private String insIdCase_3 = ""; // 合约id前面3位小写
    private String time = "";
    private String from = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_kline;
    }

    @Override
    protected void initIntent(Intent intent) {
        instrumentId = getIntent().getStringExtra(FragmentConfig.INSTRUMENT_ID);
        insIdCase_3 = instrumentId.substring(0, 3).toLowerCase();
        time = getIntent().getStringExtra(FragmentConfig.TYPE);
        from = getIntent().getStringExtra(FragmentConfig.FROM);
    }

    protected void initView() {
        tvKlineCurrency.setText(insIdCase_3.toUpperCase() + " " + time);

        rvOptionalList.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        klineTradeAdapter = new KlineTradeAdapter(this);
        rvOptionalList.setAdapter(klineTradeAdapter);
        rvOptionalList.setHasFixedSize(true);
    }

    protected void initData() {
        if (from.equals(AppUtils.OKEX)) {
            mPresenter.getFuturesInfo(instrumentId);

            mPresenter.getFuturesInstrumentsTrades(instrumentId, "1", null, "10");
        } else {
            mPresenter.getBitmexInstrument(instrumentId);

            mPresenter.getBitmexTradeList(instrumentId);
        }
    }

    @Override
    protected void registerDagger() {
        DaggerKLineComponent.builder()
                .appComponent(AppApplication.getAppComponent())
                .kLineModule(new KLineModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.iv_kline_back, R.id.tv_kline_buy, R.id.tv_kline_sell})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_kline_buy:
                ToTradeActivity();
                break;
            case R.id.tv_kline_sell:
                ToTradeActivity();
                break;
            case R.id.iv_kline_back:
                if (this.getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                } else if (this.getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT) {
                    finish();
                }
                break;
        }
    }

    private void ToTradeActivity() {
        EventBus.getDefault().postSticky(new Event.SendInstrument(time, instrumentId, from));
        Intent intent = new Intent(this, TradeActivity.class);
        intent.putExtra(FragmentConfig.INSTRUMENT_ID, instrumentId);
        intent.putExtra(FragmentConfig.TYPE, time);
        intent.putExtra(FragmentConfig.FROM, from);
        startActivity(intent);
    }

    @Override
    public void onGetFuturesInfo(FuturesInstrumentsTickerList futuresInfo) {
        tvPrice.setText("$" + df.format(futuresInfo.getLast()));
        tvKline24highValue.setText(df.format(futuresInfo.getHigh_24h()));
        tvKline24number.setText(futuresInfo.getVolume_24h() + "");
        tvKline24low.setText(df.format(futuresInfo.getLow_24h()));
        mPresenter.getCandles(instrumentId, futuresInfo.getLast());
    }

    @Override
    public void onGetFuturesInfoError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetCandles(List<List<Double>> lists, double prise) {
        double spread = prise - lists.get(0).get(1);
        double p = ((spread) / lists.get(0).get(1)) * 100;
        setPriceValue(p, spread);
    }

    private void setPriceValue(double p, double spread) {
        if (p < 0) {
            tvPrice.setTextColor(AppUtils.getDecreaseColor());
            ivKlineUpDown.setBackgroundResource(AppUtils.getDownBg());
            tvPercent.setBackground(AppUtils.getDecreaseBg());
            tvPercent.setText("" + df.format(p) + "%");
            tvPercentPrise.setText("" + df.format(spread));
        } else {
            tvPrice.setTextColor(AppUtils.getIncreaseColor());
            ivKlineUpDown.setBackgroundResource(AppUtils.getUpBg());
            tvPercent.setBackground(AppUtils.getIncreaseBg());
            tvPercent.setText("+" + df.format(p) + "%");
            tvPercentPrise.setText("+" + df.format(spread));
        }
    }

    @Override
    public void onGetCandlesError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetFuturesInstrumentsTrades(List<FuturesInstrumentsTradesList> tradesLists) {
        tradesList.clear();
        tradesList.addAll(tradesLists);
        Collections.reverse(tradesList);
        klineTradeAdapter.updateItems(tradesList);
        // 发送订阅成交信息，要在的destroy 判断是否应该取消订阅
        FutureWebSocketManager.getInstance().subscribeTrade(insIdCase_3, ChannelHelper.getTime(time));
    }

    @Override
    public void onGetFuturesInstrumentsTradesError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexInstrument(List<InstrumentItemRes> instrumentItemResList) {
        InstrumentItemRes res = instrumentItemResList.get(0);
        DecimalFormat d = df;
        if (res.getQuoteCurrency().contains("USD")) {
            tvPrice.setText("$" + df.format(res.getLastPrice()));
        } else {
            kline_trade_time.setText(R.string.quantity_btc);
            tvPrice.setText("฿" + BigDecimal.valueOf(res.getLastPrice()));
            d = df4;
        }
        tvKline24highValue.setText(d.format(res.getHighPrice()));
        tvKline24number.setText(d.format(res.getForeignNotional24h() / 10000) + "万");
        tvKline24low.setText(d.format(res.getLowPrice()));
        tvKlineMarket.setVisibility(View.VISIBLE);
        tvKlineMarketValue.setVisibility(View.VISIBLE);
        tvKlineMarketValue.setText(d.format(res.getTotalVolume() / 100000000) + "亿");

        double p = res.getLastChangePcnt() * 100;
        setPriceValue(p, res.getLastPrice() * res.getLastChangePcnt());
    }

    @Override
    public void onGetBitmexInstrumentError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexTradeList(List<FuturesInstrumentsTradesList> tradesLists) {
        tradesList.addAll(tradesLists);
        klineTradeAdapter.updateItems(tradesList);
        BitMEXWebSocketManager.getInstance().subscribeTradeList(instrumentId);
    }

    @Override
    public void onGetBitmexTradeListError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    protected void onMessage(List list) {
        try {
            List<CommonRes> commonResList = list;
            if (from.equals(AppUtils.OKEX)) {
                String channel = commonResList.get(0).getChannel();
                if (channel.contains("trade")) {  // 对trade的推送消息才进行处理
                    String icon = channel.substring(17, 20); // 截取币种，如btc
                    if (insIdCase_3.equals(icon)) {
                        String toJson = GsonUtils.getGson().toJson(commonResList.get(0).getData()); // object 转String
                        final List<List<String>> tradeList = GsonUtils.getGson().fromJson(toJson,
                                new TypeToken<List<List<String>>>() {
                                }.getType());

                        if (tradeList.size() > 0) {
                            final List<FuturesInstrumentsTradesList> ftList = new ArrayList<>();
                            FuturesInstrumentsTradesList ftRes = new FuturesInstrumentsTradesList();
                            for (int i = 0; i < tradeList.size(); i++) {
                                ftRes.setTrade_id(tradeList.get(i).get(0));
                                ftRes.setPrice(NumberUtil.getDouble(tradeList.get(i).get(1), 0.0));
                                ftRes.setQty(NumberUtil.getInt(tradeList.get(i).get(2), 0, 10));
                                ftRes.setTimestamp(tradeList.get(i).get(3));
                                ftRes.setSide(tradeList.get(i).get(4));
                                if (i < 9) {
                                    ftList.add(ftRes);
                                }
                            }

                            final List<FuturesInstrumentsTradesList> tempftList = new ArrayList<>();
                            for (int i = 0; i < tradesList.size() - ftList.size(); i++) {
                                tempftList.add(tradesList.get(i));
                            }
                            // 返回到主线程刷新数据
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tradesList.clear();
                                    tradesList.addAll(ftList);
                                    tradesList.addAll(tempftList);
                                    klineTradeAdapter.updateItems(tradesList);
                                }
                            });
                        }
                    }
                }
            } else {  // bitmex
                String table = commonResList.get(0).getTable();
                Object object = commonResList.get(0).getData();
                if (table == null || object == null) {
                    return;
                }
                String sub = GsonUtils.getInstance().toJson(object);
                if (table.equals("trade")) {
                    List<FuturesInstrumentsTradesList> lists = GsonUtils.getGson().fromJson(sub,
                            new TypeToken<List<FuturesInstrumentsTradesList>>() {
                            }.getType());

                    if (lists.size() > 0) {
                        final List<FuturesInstrumentsTradesList> ftList = new ArrayList<>();
                        for (int i = 0; i < lists.size(); i++) {
                            if (i < 10) {
                                ftList.add(lists.get(i));
                            }
                        }

                        final List<FuturesInstrumentsTradesList> tempftList = new ArrayList<>();
                        for (int i = 0; i < tradesList.size() - lists.size(); i++) {
                            if (i < 10) {
                                tempftList.add(tradesList.get(i));
                            }
                        }

                        // 返回到主线程刷新数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tradesList.clear();
                                tradesList.addAll(ftList);
                                tradesList.addAll(tempftList);
                                klineTradeAdapter.updateItems(tradesList);

                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            llKlineValueShow.setVisibility(View.GONE);
            timeBottomShowLine.setVisibility(View.GONE);
            llKlineBottom.setVisibility(View.GONE);
            ll_trade_list_show.setVisibility(View.GONE);
        } else if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            ll_trade_list_show.setVisibility(View.VISIBLE);
            llKlineValueShow.setVisibility(View.VISIBLE);
            timeBottomShowLine.setVisibility(View.VISIBLE);
            llKlineBottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (from.equals(AppUtils.OKEX)) {
            FutureWebSocketManager.getInstance().unsubscribeTrade(insIdCase_3,
                    ChannelHelper.getTime(time));
        } else {
            BitMEXWebSocketManager.getInstance().unsubscribeTradeList(instrumentId);
        }
    }
}
