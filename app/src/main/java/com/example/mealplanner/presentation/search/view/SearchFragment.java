package com.example.mealplanner.presentation.search.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.data.models.Area;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Ingredient;
import com.example.mealplanner.data.models.IngredientMealDetails;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.datasource.search.remote.SearchRemoteDataSource;
import com.example.mealplanner.presentation.home.view.HomeFragmentDirections;
import com.example.mealplanner.presentation.search.presenter.SearchPresenter;
import com.example.mealplanner.presentation.search.presenter.SearchPresenterImp;
import com.example.mealplanner.presentation.search.presenter.SearchView;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView, OnMealSearchClick {

    private TextInputEditText searchEditText;
    private Chip chipCountry, chipCategory, chipIngredient;
    private ProgressBar progressBar;
    private RecyclerView ingredientsRecyclerView, categoriesRecyclerView, areasRecyclerView, searchResultsRecyclerView;

    private SearchIngradiantAdapter ingredientAdapter;
    private SearchCategoryAdapter categoryAdapter;
    private SearchAreaAdapter areaAdapter;
    private SearchResultAdapter searchResultAdapter;

    private SearchPresenter presenter;
    private String currentFilter = "";

    private List<Ingredient> fullIngredientsList = new ArrayList<>();
    private List<Category> fullCategoriesList = new ArrayList<>();
    private List<Area> fullAreasList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        setupRecyclerViews();
        setupPresenter();
        setupListeners();
        return view;
    }

    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        chipCountry = view.findViewById(R.id.chipCountry);
        chipCategory = view.findViewById(R.id.chipCategory);
        chipIngredient = view.findViewById(R.id.chipIngredient);
        progressBar = view.findViewById(R.id.progressBar);

        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        areasRecyclerView = view.findViewById(R.id.areasRecyclerView);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
    }

    private void setupRecyclerViews() {
        ingredientAdapter = new SearchIngradiantAdapter(ingredient -> {
            presenter.searchByIngredient(ingredient.getStrIngredient());
            currentFilter = "ingredient";
        });
        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        // 2. إعداد الـ Categories (Grid)
        categoryAdapter = new SearchCategoryAdapter(category -> {
            presenter.searchByCategory(category.getStrCategory());
            currentFilter = "category";
        });
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // 3. التعديل المطلوب: إعداد الـ Areas لتكون Grid بدلاً من Linear
        areaAdapter = new SearchAreaAdapter(area -> {
            presenter.searchByArea(area.getStrArea());
            currentFilter = "area";
        });
        // قمنا بتغييرها هنا لتصبح شبكة من عمودين
        areasRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        areasRecyclerView.setAdapter(areaAdapter);
        // 4. إعداد نتائج البحث (التي تظهر فيها الوجبات)
        searchResultAdapter = new SearchResultAdapter(this);
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        searchResultsRecyclerView.setAdapter(searchResultAdapter);
    }

    private void setupPresenter() {
        presenter = new SearchPresenterImp(this, new SearchRemoteDataSource());
    }

    private void setupListeners() {
        chipCountry.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentFilter = "country";
                hideAllRecyclerViews();
                areasRecyclerView.setVisibility(View.VISIBLE);
                presenter.getListArea(); // جلب قائمة البلاد
                searchEditText.setText("");
                searchEditText.setHint("Search in countries...");
            } else {
                checkDefaultState();
            }
        });

        // مستمع لفلتر التصنيفات
        chipCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentFilter = "category";
                hideAllRecyclerViews();
                categoriesRecyclerView.setVisibility(View.VISIBLE);
                presenter.getListCategory();
                searchEditText.setText("");
                searchEditText.setHint("Search in categories...");
            } else {
                checkDefaultState();
            }
        });

        // مستمع لفلتر المكونات
        chipIngredient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentFilter = "ingredient";
                hideAllRecyclerViews();
                ingredientsRecyclerView.setVisibility(View.VISIBLE);
                presenter.getListIngredients();
                searchEditText.setText("");
                searchEditText.setHint("Search in ingredients...");
            } else {
                checkDefaultState();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().trim();
                if (searchText.isEmpty()) {
                    restoreFullList();
                } else {
                    performLocalSearch(searchText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void checkDefaultState() {
        if (!chipCountry.isChecked() && !chipCategory.isChecked() && !chipIngredient.isChecked()) {
            currentFilter = "";
            searchEditText.setHint(getString(R.string.search_bar_home));
            hideAllRecyclerViews();
        }
    }
    private void handleChipChange(String filter, boolean isChecked, RecyclerView targetRv) {
        if (isChecked) {
            currentFilter = filter;
            hideAllRecyclerViews();
            targetRv.setVisibility(View.VISIBLE);
            if (filter.equals("country")) presenter.getListArea();
            else if (filter.equals("category")) presenter.getListCategory();
            else presenter.getListIngredients();
            searchEditText.setText("");
        } else if (!chipCountry.isChecked() && !chipCategory.isChecked() && !chipIngredient.isChecked()) {
            currentFilter = "";
            hideAllRecyclerViews();
        }
    }

    private void performLocalSearch(String searchText) {
        String query = searchText.toLowerCase();
        switch (currentFilter) {
            case "country":
                List<Area> filteredAreas = new ArrayList<>();
                for (Area a : fullAreasList) if (a.getStrArea().toLowerCase().contains(query)) filteredAreas.add(a);
                areaAdapter.setAreaList(filteredAreas);
                break;
            case "category":
                List<Category> filteredCats = new ArrayList<>();
                for (Category c : fullCategoriesList) if (c.getStrCategory().toLowerCase().contains(query)) filteredCats.add(c);
                categoryAdapter.setCategoryList(filteredCats);
                break;
            case "ingredient":
                List<Ingredient> filteredIngs = new ArrayList<>();
                for (Ingredient i : fullIngredientsList) if (i.getStrThumb().toLowerCase().contains(query)) filteredIngs.add(i);
                ingredientAdapter.setIngredientList(filteredIngs);
                break;
            default:
                presenter.searchByName(query);
                break;
        }
    }

    private void restoreFullList() {
        if (currentFilter.equals("country")) areaAdapter.setAreaList(fullAreasList);
        else if (currentFilter.equals("category")) categoryAdapter.setCategoryList(fullCategoriesList);
        else if (currentFilter.equals("ingredient")) ingredientAdapter.setIngredientList(fullIngredientsList);
    }

    private void hideAllRecyclerViews() {
        ingredientsRecyclerView.setVisibility(View.GONE);
        categoriesRecyclerView.setVisibility(View.GONE);
        areasRecyclerView.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.GONE);
    }

    @Override public void onDisplayListArea(List<Area> areas) {
        fullAreasList = areas;
        areaAdapter.setAreaList(areas);
    }

    @Override public void onLoading(boolean isLoading) { progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE); }

    @Override
    public void onGetFullMealSuccess(Meal meal) {
        onLoading(false);
        if (meal != null) {
            NavHostFragment.findNavController(this)
                    .navigate(SearchFragmentDirections.actionSearchFragmentToMealFragment(meal));
        } else {
            Toast.makeText(getContext(), "Failed to get meal details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onSearchSuccess(List<Meal> meals) {
        hideAllRecyclerViews();
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
        searchResultAdapter.setMealList(meals);
    }
    @Override public void onSearchByNameSuccess(List<Meal> meals) { onSearchSuccess(meals); }
    @Override public void onSearchFailure(String error) { Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show(); }
    @Override public void onNoInternet() { Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show(); }
    @Override public void onDisplayListIngredients(List<Ingredient> ings) { fullIngredientsList = ings; ingredientAdapter.setIngredientList(ings); }
    @Override public void onDisplayListCategory(List<Category> cats) { fullCategoriesList = cats; categoryAdapter.setCategoryList(cats); }

    @Override
    public void onMealClick(Meal meal) {
            Log.d("SEARCH_DEBUG", "Meal Clicked: " + meal.getStrMeal()); // ضيفي السطر ده

        NavHostFragment.findNavController(this)
                .navigate(SearchFragmentDirections.actionSearchFragmentToMealFragment(meal));
    }
}