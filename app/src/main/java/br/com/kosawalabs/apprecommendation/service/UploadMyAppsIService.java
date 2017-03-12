package br.com.kosawalabs.apprecommendation.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

import br.com.kosawalabs.apprecommendation.R;

public class UploadMyAppsIService extends IntentService {
    private static final String TAG = UploadMyAppsIService.class.getSimpleName();

    private static final String ACTION_UPLOAD_APPS = "br.com.kosawalabs.apprecommendation.service.action.UPLOAD_APPS";

    public UploadMyAppsIService() {
        super("UploadMyAppsIService");
    }

    public static void startActionUploadApps(Context context) {
        Intent intent = new Intent(context, UploadMyAppsIService.class);
        intent.setAction(ACTION_UPLOAD_APPS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_APPS.equals(action)) {
                handleActionUploadMyApps();
            }
        }
    }

    private void handleActionUploadMyApps() {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        int count = 0;
        for (ApplicationInfo packageInfo : packages) {
            if (isNotInBlackList(packageInfo)) {
                count++;
                Log.d(TAG, "Installed package :" + packageInfo.packageName);
            }
        }
        Log.d(TAG, getString(R.string.toast_packages_found, count));
    }

    private boolean isNotInBlackList(ApplicationInfo packageInfo) {
        return !packageInfo.packageName.startsWith("android")
                && !packageInfo.packageName.startsWith("com.android.");
    }
}
