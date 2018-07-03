package com.lcsd.dongzhi.http;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.lcsd.dongzhi.entity.UserInfo;
import com.lcsd.dongzhi.util.StringUtil;
import com.tencent.smtt.sdk.QbSdk;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/12/14.
 */
public class AppContext extends Application {
    private static AppContext appContext = null;
    private static Context context;
    private static MyOkHttp mMyOkHttp;

    {
        PlatformConfig.setWeixin("wxae75580fed67b914", "3dc75e59a0aec303b44096a19d503078");
        PlatformConfig.setQQZone("1106133179", "oG1yjpQdFaomUug5");
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        context = this.getApplicationContext();
        UMShareAPI.get(this);
        QbSdk.initX5Environment(context, null );
    }

    public static AppContext getInstance() {
        if (appContext == null) {
            return new AppContext();
        }
        return appContext;
    }

    public static MyOkHttp getmMyOkHttp() {
        if (mMyOkHttp == null) {
            //持久化存储cookie
            ClearableCookieJar cookieJar =
                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
            //自定义OkHttp
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    .writeTimeout(10000L, TimeUnit.MILLISECONDS)
                    .cookieJar(cookieJar)       //设置开启cookie
                    .build();
            mMyOkHttp = new MyOkHttp(okHttpClient);
        }
        return mMyOkHttp;
    }

    /**
     * 获取缓存用户信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return Storage.GetUserInfo() == null ? new UserInfo() : Storage
                .GetUserInfo();
    }

    /**
     * 保存缓存用户信息
     *
     * @param user
     */
    public void saveUserInfo(final UserInfo user) {
        if (user != null) {
            Storage.ClearUserInfo();
            Storage.saveUsersInfo(user);
        }
    }

    /**
     * 用户存在是ture 否则是false
     *
     * @return
     */
    public boolean checkUser() {
        if (StringUtil.isEmpty(getUserInfo().getUser_id())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 清除缓存用户信息
     *
     * @param
     */
    public void cleanUserInfo() {
        Storage.ClearUserInfo();
    }
}
