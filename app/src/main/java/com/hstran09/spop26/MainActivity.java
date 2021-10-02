package com.hstran09.spop26;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.hstran09.spop26.adapter.SearchResponseResultAdapter;
import com.hstran09.spop26.common.RestClient;
import com.hstran09.spop26.data.APIInterface;
import com.hstran09.spop26.model.SearchResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView searchItemsView;
    RecyclerView.Adapter searchItemsViewAdapter; // FIXME Should we use the global variable?

    SearchView searchBar;

    APIInterface apiEndpoint;

    List<SearchResponse.Result> searchItems;

    private void initView() {
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        searchBar = findViewById(R.id.search_bar);
    }

    private void createApiEndpoint() {
        apiEndpoint = RestClient.getClient().create(APIInterface.class);
    }

    private void initSearchItemsView() {
        searchItemsView = findViewById(R.id.search_response_items_view);

        searchItems = Collections.emptyList();
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        searchItemsViewAdapter = new SearchResponseResultAdapter(
                this,
                searchItems);

        searchItemsView.setLayoutManager(recyclerViewLayoutManager);
        searchItemsView.setAdapter(searchItemsViewAdapter);
        searchItemsView.setHasFixedSize(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initSearchItemsView();
        createApiEndpoint();

        queryThenSetItemsOnView("start");

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryThenSetItemsOnView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.isEmpty())
                    setItemsToSearchItemsView(new ArrayList<>());
                return false;
            }
        });
        searchBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast t = Toast.makeText(MainActivity.this, "close", Toast.LENGTH_SHORT);
                t.show();
                setItemsToSearchItemsView(new ArrayList<>());
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((SearchResponseResultAdapter) searchItemsViewAdapter).setItemOnClick(
                new SearchResponseResultAdapter.ItemOnClick() {
                    @Override
                    public void onClick(View view, int position) {

                        SearchResponse.Result result = searchItems.get(position);

                        Intent intent = new Intent(MainActivity.this, NowPlayActivity.class);
                        intent.putExtra("id", result.getId());
                        intent.putExtra("audio", result.getAudio());
                        intent.putExtra("description", result.getDescriptionOriginal());
                        intent.putExtra("thumbnail", result.getThumbnail());
                        intent.putExtra("episodeTitle", result.getTitleOriginal());
                        intent.putExtra("podcastTitle", result.getPodcast().getTitleOriginal());
                        intent.putExtra("publisher", result.getPodcast().getPublisherOriginal());

                        startActivity(intent);
                    }
                });
    }

    private void queryThenSetItemsOnView(String textToSearch) {
        Call<SearchResponse> searchResponseCall = apiEndpoint.search(textToSearch);
        searchResponseCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                System.out.println("onResponse");
                Optional.ofNullable(response.body())
                        .ifPresent(searchResponse -> {
                            searchItems = Objects.isNull(searchResponse.getResults())
                                    ? new ArrayList<>()
                                    : searchResponse.getResults();
                            setItemsToSearchItemsView(searchItems);
                        });
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                System.out.println("onFailure");
            }
        });
    }

    private void setItemsToSearchItemsView(List<SearchResponse.Result> items) {
        searchItemsViewAdapter = new SearchResponseResultAdapter(
                getApplicationContext(),
                items);

        searchItemsView.setAdapter(searchItemsViewAdapter);


    }

//    public void testButtonOnClick(View view) {
//
//        Intent intent = new Intent(MainActivity.this, NowPlayActivity.class);
//        intent.putExtra("result.episode.id", "result.getId()");
//        startActivity(intent);
//    }
}