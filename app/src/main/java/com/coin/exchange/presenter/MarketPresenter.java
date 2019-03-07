package com.coin.exchange.presenter;

import com.coin.exchange.view.fragment.main.MarketView;
import com.coin.libbase.presenter.BasePresenter;

import javax.inject.Inject;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */
public class MarketPresenter extends BasePresenter<MarketView> {
    @Inject
    public MarketPresenter(MarketView mView) {
        super(mView);
    }
}
