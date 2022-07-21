package com.example.infinity;

import android.app.Notification;

import androidx.annotation.Nullable;

import com.example.infinity.Utils.DownloadServArgs;
import com.example.infinity.Utils.DownloadUtil;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.PlatformScheduler;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.util.Util;

import java.util.List;


public class MediaDownloadService extends DownloadService{


    public MediaDownloadService() {
        super( DownloadServArgs.DOWNLOAD_NOTIFICATION_ID,
                DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
                DownloadServArgs.DOWNLOAD_CHANNEL_ID,
                R.string.exo_download_notification_channel_name,
                /* channelDescriptionResourceId= */ 0);
    }

    @Override
    protected DownloadManager getDownloadManager() {
        return DownloadUtil.getDownloadManager(this);
    }

    @Nullable
    @Override
    protected Scheduler getScheduler() {
        return Util.SDK_INT >= 21 ? new PlatformScheduler(this, 1) : null;
    }

    @Override
    protected Notification getForegroundNotification(List<Download> downloads) {
        return DownloadUtil.getDownloadNotificationHelper(/* context= */ this)
                .buildProgressNotification(
                        /* context= */ this,
                        R.mipmap.ic_launcher_foreground,
                        /* contentIntent= */ null,
                        /* message= */ null,
                        downloads);
    }


    /*public MediaDownloadService() {
        super(DownloadServArgs.DOWNLOAD_NOTIFICATION_ID , DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL
                ,DownloadServArgs.DOWNLOAD_CHANNEL_ID ,R.string.download_channel_name);


    }


    @Override
    protected DownloadManager getDownloadManager() {
        return DownloadUtil.getDownloadManager(this);
    }

    @Nullable
    @Override
    protected Scheduler getScheduler() {
        return null;
    }

    @Override
    protected Notification getForegroundNotification(DownloadManager.TaskState[] taskStates) {
        return DownloadNotificationUtil.buildProgressNotification(this , R.mipmap.ic_launcher_foreground,
                DownloadServArgs.DOWNLOAD_CHANNEL_ID , null , null ,taskStates);
    }*/
}
