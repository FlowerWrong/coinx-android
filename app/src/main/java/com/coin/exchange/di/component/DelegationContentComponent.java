package com.coin.exchange.di.component;

import com.coin.exchange.di.module.DelegationContentModule;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.fragment.trade.delegation.DelegationContentFragment;

import dagger.Component;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */

@ActivityScope
@Component(modules = {DelegationContentModule.class}, dependencies = AppComponent.class)
public interface DelegationContentComponent {

    void inject(DelegationContentFragment delegationContentFragment);

}
