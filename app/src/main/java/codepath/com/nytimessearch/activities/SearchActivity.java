package codepath.com.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codepath.com.nytimessearch.Article;
import codepath.com.nytimessearch.ArticleArrayAdapter;
import codepath.com.nytimessearch.EndlessScrollListener;
import codepath.com.nytimessearch.R;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.filter;
import static android.R.attr.id;
import static android.os.Build.VERSION_CODES.M;
import static com.loopj.android.http.AsyncHttpClient.log;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    String filterDate;
    String filterSort;
    String filterCategory;
    Boolean isFilterSet = false;
    int scrollPage;
    String query;
    int totalHits;
    int totalPages;
    String url =  "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    String apiKey = "695e5019fa1c42bb895130acc6737614";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

    }


    private void setupViews() {
        //etQuery = (EditText) findViewById(R.id.etQuery);
        //btnSearch = (Button) findViewById(R.id.btnSearch);
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);


        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                log.d("scroll", "adapter count " + adapter.getCount() + "totalItemsCount  " + totalItemsCount);
                log.d("Date page", "is " + page);

                    loadNextDataFromApi(page);

                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });



        //listner for grid results
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Create an intent to dispaly an article
                Intent i= new Intent(getApplicationContext(),ArticleActivity.class);

                //get the article to display
                Article article = articles.get(position);

                //pass the article into intent
                //i.putExtra("url", article.getWebUrl());
                i.putExtra("article",article);

                // lauch activity
                startActivity(i);
            }
        });


    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data

        updateData(offset);

    }

    private void updateData(final int pageNum) {
        //duplicate method to test scrolling

        //String query = etQuery.getText().toString();
        //Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();

        AsyncHttpClient client = new AsyncHttpClient();
        //String url =  "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        //params.put("api-key", "695e5019fa1c42bb895130acc6737614");
        params.put("api-key", apiKey);

        params.put("page",pageNum);
        params.put("q", query);
        if (isFilterSet) {
            params.put("begin_date", filterDate);
            params.put("sort", filterSort);
            params.put("fq", "news_desk:(" + filterCategory + ")");
        }

        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("Fail", errorResponse.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("Debug", response.toString());

                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    //Log.d("Debug", response.toString());
                    //articles.addAll(Article.fromJSONArray(articleJsonResults));
                    //adapter.clear();
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));

                    adapter.notifyDataSetChanged();

                    //Log.d("Debug", articles.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //final MenuItem searchItem = menu.findItem(R.id.);
        MenuItem searchItem = menu.findItem(R.id.nySearchSettings);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                // perform query here
                query = searchQuery;
                adapter.clear();
                scrollPage =0;
                updateData(scrollPage);


                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        switch (item.getItemId()) {
            case R.id.nySearchSettings:
                // set search settings
                //SearchSettings();
                return true;
            case R.id.nyFilter:
                // filter search
                filterResults();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }

    /**
     * Launching new activity
     * */

    int REQUEST_CODE = 20;
    private void filterResults() {
        Intent i = new Intent(this, FilterActivity.class);
        //startActivity(i);
        startActivityForResult(i,REQUEST_CODE);
    }


    protected void onActivityResult(int requstCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && requstCode == REQUEST_CODE) {

            String returnDate = data.getExtras().getString("return_begin_date");
            String returnSort = data.getExtras().getString("return_sort");
            String returnCategory = data.getExtras().getString("return_category");

            filterDate = returnDate;
            filterSort = returnSort;
            filterCategory = returnCategory;
            isFilterSet = true;

            adapter.clear();
            scrollPage =0;
            //if (isNetworkAvailable()) {
                updateData(scrollPage);
            //} else {
            //    Toast.makeText(this, "Internet connection unavailable", Toast.LENGTH_SHORT).show();
            //}
            //scrollPage = 0;
            //filterQuery(returnDate,returnSort,returnCategory);


        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    //https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=227c750bb7714fc39ef1559ef1bd8329

}
