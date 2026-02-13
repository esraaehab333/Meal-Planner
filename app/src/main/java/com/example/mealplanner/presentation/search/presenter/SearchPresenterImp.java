package com.example.mealplanner.presentation.search.presenter;

import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.datasource.search.remote.SearchRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenterImp implements SearchPresenter {

    private SearchView view;
    private SearchRemoteDataSource remoteDataSource;
    private CompositeDisposable disposables = new CompositeDisposable();

    private List<Meal> lastResults = new ArrayList<>();
    private String lastQuery = "";

    public SearchPresenterImp(SearchView view, SearchRemoteDataSource remoteDataSource) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
    }

    public List<Meal> getLastResults() {
        return lastResults == null ? new ArrayList<>() : lastResults;
    }

    public String getLastQuery() {
        return lastQuery == null ? "" : lastQuery;
    }

    public void clear() {
        disposables.clear();
        view = null;
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
    public void searchByName(String name) {
        lastQuery = name == null ? "" : name.trim();
        if (view != null) view.onLoading(true);

        disposables.add(
                remoteDataSource.searchByName(lastQuery)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(meals -> {
                            lastResults = meals == null ? new ArrayList<>() : meals;
                            if (view != null) {
                                view.onLoading(false);
                                view.onSearchByNameSuccess(lastResults);
                            }
                        }, this::handleError)
        );
    }

    @Override
    public void getListArea() {
        disposables.add(
                remoteDataSource.getAreasList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(areas -> {
                            if (view != null) view.onDisplayListArea(areas);
                        }, this::handleError)
        );
    }

    @Override
    public void getListIngredients() {
        disposables.add(
                remoteDataSource.getIngredientsList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(ingredients -> {
                            if (view != null) view.onDisplayListIngredients(ingredients);
                        }, this::handleError)
        );
    }

    @Override
    public void getListCategory() {
        disposables.add(
                remoteDataSource.getCategoriesList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(categories -> {
                            if (view != null) view.onDisplayListCategory(categories);
                        }, this::handleError)
        );
    }

    @Override public void searchByIngredient(String ingredient) {}
    @Override public void searchByCategory(String category) {}
    @Override public void searchByArea(String area) {}
}
