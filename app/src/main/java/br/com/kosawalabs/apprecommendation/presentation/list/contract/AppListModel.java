package br.com.kosawalabs.apprecommendation.presentation.list.contract;

public interface AppListModel {
    boolean isLogged();

    void fetchFirstPage();

    void fetchNextPage();

    void setRecommended(boolean recommended);

    int getPageSize();

    boolean hasStopLoading();
}
