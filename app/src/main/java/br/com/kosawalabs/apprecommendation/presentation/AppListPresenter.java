package br.com.kosawalabs.apprecommendation.presentation;


import br.com.kosawalabs.apprecommendation.data.AppDataRepository;

public class AppListPresenter {
    private AppDataRepository repository;

    public AppListPresenter(AppDataRepository repository) {
        this.repository = repository;
    }

    public void fetchFirstPage() {

    }
}
