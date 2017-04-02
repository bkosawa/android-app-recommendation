package br.com.kosawalabs.apprecommendation.presentation.list;


public interface AppListPresenter {
    void fetchFirstPage();

    void fetchNextPage();

    void fetchRecommendedFirstPage();

    void fetchRecommendedNextPage();
}
