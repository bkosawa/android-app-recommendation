package br.com.kosawalabs.apprecommendation.data;

public interface DataCallback<T> {
    void onSuccess(T result);

    void onError(DataError error);
}
