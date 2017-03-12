package br.com.kosawalabs.apprecommendation.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class UploadMyAppsIService extends IntentService {
    private static final String ACTION_UPLOAD_APPS = "br.com.kosawalabs.apprecommendation.service.action.UPLOAD_APPS";

    private static final String EXTRA_PACKAGE_LIST = "br.com.kosawalabs.apprecommendation.service.extra.PARAM1";

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
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
