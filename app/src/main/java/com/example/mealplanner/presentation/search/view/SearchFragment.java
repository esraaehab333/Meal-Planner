package com.example.mealplanner.presentation.search.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.data.models.Area;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Ingredient;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.datasource.search.remote.SearchRemoteDataSource;
import com.example.mealplanner.presentation.search.presenter.SearchPresenterImp;
import com.example.mealplanner.presentation.search.presenter.SearchView;
import com.example.mealplanner.utils.CustomSnackbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchFragment extends Fragment implements SearchView, OnMealSearchClick {

    private TextInputEditText etSearch;
    private ProgressBar progressBar;
    private RecyclerView rvMeals;

    private MaterialButton btnCategoryFilter, btnAreaFilter, btnIngredientFilter;
    private View btnClearFilters;

    private SearchResultAdapter adapter;
    private SearchPresenterImp presenter;

    private final CompositeDisposable uiDisposables = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();

    private final List<String> categoryList = new ArrayList<>();
    private final List<String> areaList = new ArrayList<>();
    private final List<String> ingredientList = new ArrayList<>();

    private String selectedCategory = null;
    private String selectedArea = null;
    private String selectedIngredient = null;

    private List<Meal> lastResults = new ArrayList<>();
    private String lastQuery = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupListeners();

        presenter = new SearchPresenterImp(this, new SearchRemoteDataSource());

        // Fetch filter data
        presenter.getListCategory();
        presenter.getListArea();
        presenter.getListIngredients();

        setupSearchObservable();
    }

    private void initViews(View view) {
        etSearch = view.findViewById(R.id.et_search);
        progressBar = view.findViewById(R.id.progressBar);
        rvMeals = view.findViewById(R.id.searchResultsRecyclerView);

        btnCategoryFilter = view.findViewById(R.id.btn_filter_category);
        btnAreaFilter = view.findViewById(R.id.btn_filter_area);
        btnIngredientFilter = view.findViewById(R.id.btn_filter_ingredient);
        btnClearFilters = view.findViewById(R.id.btn_clear_filters);
    }

    private void setupRecyclerView() {
        adapter = new SearchResultAdapter(this);
        rvMeals.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvMeals.setAdapter(adapter);
    }

    private void setupListeners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchSubject.onNext(s == null ? "" : s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Dialog Triggers
        btnCategoryFilter.setOnClickListener(v -> showFilterDialog("Select Category", categoryList, selection -> {
            selectedCategory = selection;
            updateFilterButtonState(btnCategoryFilter, "Category", selectedCategory);
            renderFiltered();
        }));

        btnAreaFilter.setOnClickListener(v -> showFilterDialog("Select Area", areaList, selection -> {
            selectedArea = selection;
            updateFilterButtonState(btnAreaFilter, "Area", selectedArea);
            renderFiltered();
        }));

        btnIngredientFilter.setOnClickListener(v -> showFilterDialog("Select Ingredient", ingredientList, selection -> {
            selectedIngredient = selection;
            updateFilterButtonState(btnIngredientFilter, "Ingredient", selectedIngredient);
            renderFiltered();
        }));

        // Clear All
        if (btnClearFilters != null) {
            btnClearFilters.setOnClickListener(v -> clearAllFilters());
        }
    }

    private void setupSearchObservable() {
        uiDisposables.add(
                searchSubject
                        .map(s -> s == null ? "" : s.trim())
                        .debounce(350, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(query -> {
                            lastQuery = query;
                            if (query.isEmpty()) {
                                lastResults = new ArrayList<>();
                                adapter.setMealList(new ArrayList<>());
                                return;
                            }
                            presenter.searchByName(query);
                        }, throwable -> {})
        );
    }

    private void clearAllFilters() {
        selectedCategory = null;
        selectedArea = null;
        selectedIngredient = null;

        updateFilterButtonState(btnCategoryFilter, "Category", null);
        updateFilterButtonState(btnAreaFilter, "Area", null);
        updateFilterButtonState(btnIngredientFilter, "Ingredient", null);

        renderFiltered();
    }

    private void showFilterDialog(String title, List<String> items, OnFilterSelectedListener listener) {

        String[] itemArray = items.toArray(new String[0]);

        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setItems(itemArray, (dialog, which) -> {
                    listener.onSelected(itemArray[which]);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateFilterButtonState(MaterialButton btn, String defaultLabel, String selectedValue) {
        if (selectedValue == null) {
            btn.setText(defaultLabel);
            btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray));
            btn.setStrokeColor(androidx.core.content.ContextCompat.getColorStateList(requireContext(), R.color.color_secondary_darker));
        } else {
            btn.setText(selectedValue);

            btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_primary_variant));
            btn.setStrokeColor(androidx.core.content.ContextCompat.getColorStateList(requireContext(), R.color.color_primary));
        }
    }


    private void renderFiltered() {
        if (lastQuery == null || lastQuery.trim().isEmpty()) {
            adapter.setMealList(new ArrayList<>());
            return;
        }
        List<Meal> filtered = applyFilters(lastResults);
        adapter.setMealList(filtered);
    }

    private List<Meal> applyFilters(List<Meal> origin) {
        if (origin == null) return new ArrayList<>();

        List<Meal> out = new ArrayList<>();
        for (Meal m : origin) {
            if (m == null) continue;

            if (selectedCategory != null) {
                String cat = safe(m.getStrCategory());
                if (!cat.equalsIgnoreCase(selectedCategory)) continue;
            }

            if (selectedArea != null) {
                String area = safe(m.getStrArea());
                if (!area.equalsIgnoreCase(selectedArea)) continue;
            }

            if (selectedIngredient != null) {
                boolean has = false;
                for (String ing : m.getIngredientsList()) {
                    if (safe(ing).equalsIgnoreCase(selectedIngredient)) {
                        has = true;
                        break;
                    }
                }
                if (!has) continue;
            }
            out.add(m);
        }
        return out;
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }


    @Override
    public void onLoading(boolean isLoading) {
        if (progressBar == null) return;
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onNoInternet() {
        CustomSnackbar.showError( getView(),"No internet connection");
    }

    @Override
    public void onSearchFailure(String message) {
        CustomSnackbar.showError( getView(),"Search error");
    }

    @Override
    public void onSearchByNameSuccess(List<Meal> meals) {
        lastResults = meals == null ? new ArrayList<>() : meals;
        renderFiltered();
    }

    @Override public void onGetFullMealSuccess(Meal meal) {}
    @Override public void onSearchSuccess(List<Meal> meals) {}

    @Override
    public void onDisplayListCategory(List<Category> categories) {
        categoryList.clear();
        if (categories != null) {
            for (Category c : categories) {
                if (c != null && c.getStrCategory() != null && !c.getStrCategory().isEmpty()) {
                    categoryList.add(c.getStrCategory());
                }
            }
        }
    }

    @Override
    public void onDisplayListArea(List<Area> areas) {
        areaList.clear();
        if (areas != null) {
            for (Area a : areas) {
                if (a != null && a.getStrArea() != null && !a.getStrArea().isEmpty()) {
                    areaList.add(a.getStrArea());
                }
            }
        }
    }

    @Override
    public void onDisplayListIngredients(List<Ingredient> ingredients) {
        ingredientList.clear();
        if (ingredients != null) {
            for (Ingredient i : ingredients) {
                if (i != null && i.getStrIngredient() != null && !i.getStrIngredient().isEmpty()) {
                    ingredientList.add(i.getStrIngredient());
                }
            }
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        if (meal == null) return;
        try {
            SearchFragmentDirections.ActionSearchFragmentToMealFragment action =
                    SearchFragmentDirections.actionSearchFragmentToMealFragment(meal);
            NavHostFragment.findNavController(this).navigate(action);
        } catch (Exception e) {
            CustomSnackbar.showError( getView(),"Navigation action not found.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uiDisposables.clear();
        if (presenter != null) presenter.clear();
    }

    interface OnFilterSelectedListener {
        void onSelected(String selection);
    }
}