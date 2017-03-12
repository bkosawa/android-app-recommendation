package br.com.kosawalabs.apprecommendation.presentation.list;


public interface AppListPresenter {
    void fetchFirstPage();

    void fetchNextPage();

    boolean shouldLoadMore();

    boolean listIsAtTheEnd();
}
