package br.com.kosawalabs.apprecommendation.presentation;


public interface AppListPresenter {
    void fetchFirstPage();

    void fetchNextPage();

    boolean shouldLoadMore();
}
