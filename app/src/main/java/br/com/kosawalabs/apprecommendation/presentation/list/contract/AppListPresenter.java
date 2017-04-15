package br.com.kosawalabs.apprecommendation.presentation.list.contract;


public abstract class AppListPresenter {
    public interface AppListPresenterFromView {
        void init();

        void refreshList();

        void loadMore();

        long getPageSize();

        void setRecommended(boolean isRecommended);

        boolean hasStopLoading();
    }
}
