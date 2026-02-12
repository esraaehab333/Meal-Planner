package com.example.mealplanner.presentation.search.presenter;

import com.example.mealplanner.datasource.search.remote.SearchRemoteDataSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchPresenterImp implements SearchPresenter {

    private SearchView view;
    private SearchRemoteDataSource remoteDataSource;
    private CompositeDisposable disposables = new CompositeDisposable();

    public SearchPresenterImp(SearchView view, SearchRemoteDataSource remoteDataSource) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
    }
    private void handleError(Throwable throwable) {
        if (view != null) {
            view.onLoading(false);
            if (throwable instanceof java.io.IOException) {
                view.onNoInternet();
            } else {
                view.onSearchFailure(throwable.getMessage());
            }
        }
    }

    @Override
    public void searchByIngredient(String ingredient) {
        if (view != null) view.onLoading(true);
        disposables.add(
                remoteDataSource.filterByIngredient(ingredient)
                        .subscribe(meals -> {
                            view.onLoading(false);
                            view.onSearchSuccess(meals);
                        }, this::handleError)
        );
    }

    @Override
    public void searchByCategory(String category) {
        if (view != null) view.onLoading(true);
        disposables.add(
                remoteDataSource.filterByCategory(category)
                        .subscribe(meals -> {
                            view.onLoading(false);
                            view.onSearchSuccess(meals);
                        }, this::handleError)
        );
    }

    @Override
    public void searchByArea(String area) {
        if (view != null) view.onLoading(true);
        disposables.add(
                remoteDataSource.filterByArea(area)
                        .subscribe(meals -> {
                            view.onLoading(false);
                            view.onSearchSuccess(meals);
                        }, this::handleError)
        );
    }

    @Override
    public void searchByName(String name) {
        if (view != null) view.onLoading(true);
        disposables.add(
                remoteDataSource.searchByName(name)
                        .subscribe(meals -> {
                            view.onLoading(false);
                            view.onSearchByNameSuccess(meals);
                        }, this::handleError)
        );
    }

    @Override
    public void getListArea() {
        if (view != null) view.onLoading(true);
        disposables.add(
                remoteDataSource.getAreasList()
                        .subscribe(areas -> {
                            view.onLoading(false);
                            view.onDisplayListArea(areas);
                        }, this::handleError)
        );
    }

    @Override
    public void getListIngredients() {
        if (view != null) view.onLoading(true);
        disposables.add(
                remoteDataSource.getIngredientsList()
                        .subscribe(ingredients -> {
                            view.onLoading(false);
                            view.onDisplayListIngredients(ingredients);
                        }, this::handleError)
        );
    }

    @Override
    public void getListCategory() {
        if (view != null) view.onLoading(true);
        disposables.add(
                remoteDataSource.getCategoriesList()
                        .subscribe(categories -> {
                            view.onLoading(false);
                            view.onDisplayListCategory(categories);
                        }, this::handleError)
        );
    }

}