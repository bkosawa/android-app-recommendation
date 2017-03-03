package br.com.kosawalabs.apprecommendation.presentation;


public interface AppListPresenter {
    void fetchFirstPage();

    boolean shouldLoadMore();
}
