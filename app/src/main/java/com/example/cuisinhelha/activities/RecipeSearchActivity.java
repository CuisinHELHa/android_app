package com.example.cuisinhelha.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.cuisinhelha.R;
import com.example.cuisinhelha.adapters.RecipeSearchResultAdapter;
import com.example.cuisinhelha.helpers.UserPreferences;
import com.example.cuisinhelha.interfaces.IHeaderNavigation;
import com.example.cuisinhelha.models.Recipe;
import com.example.cuisinhelha.services.RecipeRepositoryService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, IHeaderNavigation {

    public static final String EXTRA_SEARCH_ACTIVITY = "EXTRA_SEARCH_ACTIVITY";

    private EditText etSearch;
    private ListView lvResult;
    private RecipeSearchResultAdapter adapter;

    private List<Recipe> recipes;

    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);
        pref = getSharedPreferences(UserPreferences.PREFERENCES_NAME, MODE_PRIVATE);

        recipes = new ArrayList<>();

        etSearch = findViewById(R.id.search_et);
        lvResult = findViewById(R.id.result_lv);

        adapter = new RecipeSearchResultAdapter(this, R.id.result_lv, recipes, pref.getInt(UserPreferences.ID_USER, -1), pref.getBoolean(UserPreferences.USER_TYPE, false));
        lvResult.setAdapter(adapter);
        lvResult.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.wtf("detail", "test");
        Intent intent = new Intent(this, RecipeDetail.class);
        intent.putExtra(EXTRA_SEARCH_ACTIVITY, recipes.get(position).getIdRecipe());
        startActivity(intent);
    }

    public void searchRecipes(View view) {
        RecipeRepositoryService.queryText(etSearch.getText().toString())
                .enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                        recipes.clear();
                        recipes.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        Log.wtf("SuperError", "Une erreur lors de l'accès à la table 'recipe' est survenue");
                    }
                });
    }

    public void loadDetailsActivity(int id)
    {
        Intent intent = new Intent(this, RecipeDetail.class);
        intent.putExtra(EXTRA_SEARCH_ACTIVITY, id);
        startActivity(intent);
    }

    @Override
    public void loadProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void loadHomeActivity(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void loadRecipeSearchActivity(View view) {}
}
