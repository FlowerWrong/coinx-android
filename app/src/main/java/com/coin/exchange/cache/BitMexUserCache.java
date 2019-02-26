package com.coin.exchange.cache;

import android.text.TextUtils;

import com.coin.exchange.model.bitMex.BitMEXUserModel;
import com.coin.exchange.utils.GsonUtils;

public class BitMexUserCache {

    private static BitMEXUserModel instance;
    private final static String TAG = "BITMEX_USER_INFO_CACHE";

    private BitMexUserCache() {
    }

    /**
     * 是否在缓存中，已经存有
     *
     * @return true：存有；false：未存有
     */
    public static boolean hasInit() {
        return getDefault() != null;
    }

    /**
     * 获取 OkExUserModel（如果为第一次，会进行一次从SharePreference中获取，具体看{@link #refresh}）
     *
     * @return
     */
    public static BitMEXUserModel getDefault() {
        if (instance == null) {
            synchronized (BitMexUserCache.class) {
                if (instance == null) {
                    refresh();
                }
            }
        }
        return instance;
    }

    /**
     * 刷新内存中的数据，重新从 SharePreference 获取
     */
    public static void refresh() {
        synchronized (BitMexUserCache.class) {
            String json = CacheHelper.getInstance().getCache(TAG);
            if (TextUtils.isEmpty(json)) {
                instance = null;
            } else {
                instance = (BitMEXUserModel) GsonUtils.getInstance().fromJson(json, BitMEXUserModel.class);
            }
        }
    }

    /**
     * 移除用户信息内存，例如用户退出时，调用
     */
    public static void remove() {
        CacheHelper.getInstance().removeCache(TAG);
        instance = null;
    }

    /**
     * 判断是否为空
     *
     * @return true为空，false说明已有数据
     */
    public static boolean isEmpty() {
        return getDefault() == null;
    }

    /**
     * 保存整个Object数据，会将其进行转成json串
     *
     * @param bitMEXUserModel 用户数据
     */
    public static void save(BitMEXUserModel bitMEXUserModel) {
        instance = bitMEXUserModel;
        String json = GsonUtils.getGson().toJson(bitMEXUserModel);
        CacheHelper.getInstance().setCache(TAG, json);
    }

}
