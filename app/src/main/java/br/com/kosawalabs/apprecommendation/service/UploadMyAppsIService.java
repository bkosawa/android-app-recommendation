package br.com.kosawalabs.apprecommendation.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class UploadMyAppsIService extends IntentService {
    private static final String ACTION_UPLOAD_APPS = "br.com.kosawalabs.apprecommendation.service.action.UPLOAD_APPS";

    private static final String EXTRA_PACKAGE_LIST = "br.com.kosawalabs.apprecommendation.service.extra.PARAM1";

    public UploadMyAppsIService() {
        super("UploadMyAppsIService");
    }

    public static void startActionUploadApps(Context context, ArrayList<String> packages) {
        Intent intent = new Intent(context, UploadMyAppsIService.class);
        intent.setAction(ACTION_UPLOAD_APPS);
        intent.putExtra(EXTRA_PACKAGE_LIST, packages);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_APPS.equals(action)) {
                final ArrayList<String> param1 = intent.getStringArrayListExtra(EXTRA_PACKAGE_LIST);
                handleActionUploadMyApps(param1);
            }
        }
    }

    private void handleActionUploadMyApps(List<String> param1) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
