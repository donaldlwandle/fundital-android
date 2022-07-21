package com.example.infinity.Utils;

import android.content.Context;

import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.Executors;

public class DownloadUtil {
    private static Cache downloadCache;
    private static HttpDataSource.Factory dataSourceFactory ;
    private static DatabaseProvider databaseProvider ;
    private static  DownloadNotificationHelper downloadNotificationHelper;

    private static DownloadManager downloadManager;

    private static synchronized DatabaseProvider getDatabaseProvider(Context context) {
        if (databaseProvider == null) {
            databaseProvider = new ExoDatabaseProvider(context);
        }
        return databaseProvider;
    }

    public static synchronized HttpDataSource.Factory getHttpDataSourceFactory(Context context) {
        if (dataSourceFactory == null) {
            dataSourceFactory = new DefaultHttpDataSource.Factory();
        }
        return dataSourceFactory;
    }

    public static synchronized Cache getDownloadCache(Context mContext){

        if (downloadCache == null){
            File cacheDirectory = new File(mContext.getExternalFilesDir(null) ,"amajilanjilanwebemrhetjha");
            downloadCache = new SimpleCache(
                    cacheDirectory,
                    new NoOpCacheEvictor(),
                    getDatabaseProvider(mContext));


        }
        return downloadCache ;

    }

    public static synchronized DownloadManager getDownloadManager(Context mContext){

        if (downloadManager == null){
            File actionFile = new File(mContext.getExternalCacheDir() ,"actions");
            downloadManager =  new DownloadManager(
                    mContext,
                    getDatabaseProvider(mContext),
                    getDownloadCache(mContext),
                    getHttpDataSourceFactory(mContext),
                    Executors.newFixedThreadPool(/* nThreads= */ 6));

            downloadManager.setMaxParallelDownloads(3);
            /*downloadManager = new DownloadManager(getCache(mContext) ,
                    new DefaultDataSourceFactory(mContext , Util.getUserAgent(mContext , "Fundital")),
                    actionFile , ProgressiveDownloadAction.DESERIALIZER);*/
        }
        return downloadManager ;
    }

    public static synchronized DownloadNotificationHelper getDownloadNotificationHelper(
            Context context) {
        if (downloadNotificationHelper == null) {
            downloadNotificationHelper =
                    new DownloadNotificationHelper(context, DownloadServArgs.DOWNLOAD_CHANNEL_ID);
        }
        return downloadNotificationHelper;
    }
}
