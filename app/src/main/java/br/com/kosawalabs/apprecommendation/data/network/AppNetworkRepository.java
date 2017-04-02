package br.com.kosawalabs.apprecommendation.data.network;

import java.util.List;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.network.retrofit.ServiceGenerator;
import br.com.kosawalabs.apprecommendation.data.network.retrofit.service.AppRecommendationClient;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.data.pojo.AppResult;
import br.com.kosawalabs.apprecommendation.data.pojo.PackageName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.kosawalabs.apprecommendation.data.DataError.FORBIDDEN;
import static br.com.kosawalabs.apprecommendation.data.DataError.NOT_FOUND;

public class AppNetworkRepository implements AppDataRepository {
    private AppRecommendationClient client;

    public AppNetworkRepository(String token) {
        client = ServiceGenerator.createService(AppRecommendationClient.class, token);
    }

    @Override
    public void getApps(Long offset, Long limit, final DataCallback<List<App>> callback) {
        client.getApps(offset, limit).enqueue(new Callback<AppResult>() {
            @Override
            public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                int responseCode = response.code();
                switch (responseCode) {
                    case 200:
                        AppResult appResult = response.body();
                        if (appResult != null && appResult.getResults() != null) {
                            callback.onSuccess(appResult.getResults());
                        } else {
                            callback.onError(new DataError("Empty Body"));
                        }
                        break;
                    case FORBIDDEN:
                        callback.onError(new DataError("Error Status: " + responseCode, FORBIDDEN));
                        break;
                    default:
                        callback.onError(new DataError("Error Status: " + responseCode));
                }
            }

            @Override
            public void onFailure(Call<AppResult> call, Throwable t) {
                String errorMessage = t.getCause() != null ? t.getCause().getLocalizedMessage() : "Error on Request";
                callback.onError(new DataError(errorMessage));
            }
        });
    }

    @Override
    public void getApp(Integer appId, final DataCallback<App> callback) {
        client.getApp(appId).enqueue(new Callback<App>() {
            @Override
            public void onResponse(Call<App> call, Response<App> response) {
                int responseCode = response.code();
                switch (responseCode) {
                    case 200:
                        App app = response.body();
                        if (app != null) {
                            callback.onSuccess(app);
                        } else {
                            callback.onError(new DataError("Empty Body"));
                        }
                        break;
                    case FORBIDDEN:
                        callback.onError(new DataError("Error Status: " + responseCode, FORBIDDEN));
                        break;
                    default:
                        callback.onError(new DataError("Error Status: " + responseCode));
                }
            }

            @Override
            public void onFailure(Call<App> call, Throwable t) {
                String errorMessage = t.getCause() != null ? t.getCause().getLocalizedMessage() : "Error on Request";
                callback.onError(new DataError(errorMessage));
            }
        });
    }

    @Override
    public void getRecommendedApps(Long offset, Long limit, final DataCallback<List<App>> callback) {
        client.getRecommendedApps(offset, limit).enqueue(new Callback<AppResult>() {
            @Override
            public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                int responseCode = response.code();
                switch (responseCode) {
                    case 200:
                        AppResult appResult = response.body();
                        if (appResult != null && appResult.getResults() != null) {
                            callback.onSuccess(appResult.getResults());
                        } else {
                            callback.onError(new DataError("Empty Body"));
                        }
                        break;
                    case NOT_FOUND:
                        callback.onError(new DataError("Error Status: " + responseCode, NOT_FOUND));
                        break;
                    case FORBIDDEN:
                        callback.onError(new DataError("Error Status: " + responseCode, FORBIDDEN));
                        break;
                    default:
                        callback.onError(new DataError("Error Status: " + responseCode));
                }
            }

            @Override
            public void onFailure(Call<AppResult> call, Throwable t) {
                String errorMessage = t.getCause() != null ? t.getCause().getLocalizedMessage() : "Error on Request";
                callback.onError(new DataError(errorMessage));
            }
        });
    }

    @Override
    public void sendMyApps(List<PackageName> packageNames, final DataCallback<Void> callback) {
        client.postMyApps(packageNames).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int responseCode = response.code();
                switch (responseCode) {
                    case 200:
                        callback.onSuccess(response.body());
                        break;
                    default:
                        callback.onError(new DataError("Error Status: " + responseCode));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String errorMessage = t.getCause() != null ? t.getCause().getLocalizedMessage() : "Error on Request";
                callback.onError(new DataError(errorMessage));
            }
        });
    }
}
