package com.hiteshchopra.magnumhitesh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hiteshchopra.magnumhitesh.API.APIClient;
import com.hiteshchopra.magnumhitesh.API.APIInterface;
import com.hiteshchopra.magnumhitesh.Models.GithubUser;
import com.hiteshchopra.magnumhitesh.Models.Items;
import com.hiteshchopra.magnumhitesh.Pagination.PaginationAdapter;
import com.hiteshchopra.magnumhitesh.Pagination.PaginationScrollListener;
import com.hiteshchopra.magnumhitesh.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchResultsActivity extends AppCompatActivity {

    Intent intent;
    String githubUserName;
    APIInterface service;
    RecyclerView mRecyclerView;
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    public ProgressBar getProgressBar;
    ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 20;
    private int currentPage = PAGE_START;
    private LinearLayout mainLayout;
    View myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        intent = getIntent();
        getProgressBar = findViewById(R.id.loadmore_progress);
        LayoutInflater inflater = getLayoutInflater();
        myLayout = inflater.inflate(R.layout.no_results, mainLayout, false);

        githubUserName = intent.getStringExtra("userName");
        mRecyclerView = findViewById(R.id.recycleViewer);
        progressBar = findViewById(R.id.main_progress);
        adapter = new PaginationAdapter(this);
        mainLayout = findViewById(R.id.searchLinearLayout);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        service = APIClient.getClient().create(APIInterface.class);
        progressBar.setVisibility(View.VISIBLE);
        loadFirstPage();
    }

    void loadFirstPage() {
        callGithubApi().enqueue(new Callback<GithubUser>() {
            @Override
            public void onResponse(Call<GithubUser> call, Response<GithubUser> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e("Response Code", response.code() + "");
                if (response.isSuccessful()) {
                    List<Items> results = fetchItems(response);
                    if (results.size() == 0) {
                        LinearLayout mainLayout = findViewById(R.id.searchLinearLayout);
                        mainLayout.removeAllViews();
                        // inflate (create) another copy of our custom layout
                        LayoutInflater inflater = getLayoutInflater();
                        View myLayout = inflater.inflate(R.layout.no_results, mainLayout, false);
                        mainLayout.addView(myLayout);
                    }

                    adapter.addAll(results);
                    if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                    else {
                        // error case
                        switch (response.code()) {
                            case 404:
                                Toast.makeText(SearchResultsActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(SearchResultsActivity.this, "Server broken", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(SearchResultsActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GithubUser> call, Throwable throwable) {
                mainLayout.removeAllViews();
                // inflate (create) another copy of our custom layout
                LayoutInflater inflater = getLayoutInflater();
                View myLayout = inflater.inflate(R.layout.network_failure, mainLayout, false);
                mainLayout.addView(myLayout);
                Toast.makeText(SearchResultsActivity.this, "Following error occurred-: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public List<Items> fetchItems(Response<GithubUser> response) throws NullPointerException {
        GithubUser githubUser = response.body();
        try {
            adapter.showProgress();
            if (githubUser == null || githubUser.getItems().size() == 0) {
                adapter.removeProgress();
                Toast.makeText(this, "Finished loading", Toast.LENGTH_SHORT).show();
            }
            if (githubUser == null)
                Toast.makeText(this, "Some error occured", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        assert githubUser != null;
        return githubUser.getItems();
    }

    private void loadNextPage() {
        Log.d("TAG", "loadNextPage: " + currentPage);
        callGithubApi().enqueue(new Callback<GithubUser>() {
            @Override
            public void onResponse(Call<GithubUser> call, Response<GithubUser> response) {
                Log.e("Response Code", response.code() + "");
                adapter.removeLoadingFooter();
                isLoading = false;
                List<Items> results = fetchItems(response);
                if (results.size() == 0)
                    Log.e("List empty", "Finished");
                adapter.addAll(results);
                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<GithubUser> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private Call<GithubUser> callGithubApi() {
        return service.getUserDetails(githubUserName, currentPage);
    }
}