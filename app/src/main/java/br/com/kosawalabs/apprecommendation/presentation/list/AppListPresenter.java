package br.com.kosawalabs.apprecommendation.presentation.list;


public interface AppListPresenter {
    void init();

    void refreshList();

    void loadMore();

    long getPageSize();

    void setRecommended(boolean isRecommended);
}
