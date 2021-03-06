package io.ipoli.android.app.modules;

import android.content.Context;
import android.text.TextUtils;

import com.flurry.android.FlurryAgent;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.ipoli.android.AnalyticsConstants;
import io.ipoli.android.BuildConfig;
import io.ipoli.android.Constants;
import io.ipoli.android.app.services.AnalyticsService;
import io.ipoli.android.app.services.FlurryAnalyticsService;
import io.ipoli.android.app.utils.LocalStorage;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 1/7/16.
 */
@Module
public class AnalyticsModule {

    @Provides
    @Singleton
    public AnalyticsService provideAnalyticsService(Context context, LocalStorage localStorage) {
        if (!BuildConfig.DEBUG) {

            String playerId = localStorage.readString(Constants.KEY_PLAYER_ID);
            if (!TextUtils.isEmpty(playerId)) {
                FlurryAgent.setUserId(playerId);
            }
            FlurryAgent.setLogEnabled(false);
            FlurryAgent.init(context, AnalyticsConstants.PROD_FLURRY_KEY);
        } else {
            FlurryAgent.setLogEnabled(true);
            FlurryAgent.setCaptureUncaughtExceptions(false);
            FlurryAgent.init(context, AnalyticsConstants.DEV_FLURRY_KEY);
        }

        return new FlurryAnalyticsService();
    }
}
