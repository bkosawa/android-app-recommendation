package br.com.kosawalabs.apprecommendation.data.network;

import java.util.List;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.network.retrofit.ServiceGenerator;
import br.com.kosawalabs.apprecommendation.data.network.retrofit.service.AppRecommendationClient;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.data.pojo.AppResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    default:
                        callback.onError(new DataError("Error Status: " + responseCode));
                }
            }

            @Override
            public void onFailure(Call<AppResult> call, Throwable t) {
                callback.onError(new DataError(t.getCause().getLocalizedMessage()));
            }
        });
    }
}
