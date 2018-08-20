package codepath.com.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//import android.widget.ShareActionProvider;

import codepath.com.nytimessearch.Article;
import codepath.com.nytimessearch.R;

import static codepath.com.nytimessearch.R.id.wvArticle;

public class ArticleActivity extends AppCompatActivity {

    //private ShareActionProvider miShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //String url = getIntent().getStringExtra("url");
        
        //Instead fetching entire article
        Article article = (Article) getIntent().getSerializableExtra("article");

        WebView webView = (WebView) findViewById(wvArticle);

        webView.setWebViewClient(new WebViewClient(){
            /*@Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }*/

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });

        //webView.loadUrl(url);
        webView.loadUrl(article.getWebUrl());
        webView.setVerticalScrollBarEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);

        MenuItem item = menu.findItem(R.id.item_share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //ShareActionProvider miShare = (ShareActionProvider) item.getActionProvider();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // get reference to WebView
        WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
        // pass in the URL currently being used by the WebView
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());

        if (miShare != null) {
            miShare.setShareIntent(shareIntent);
        }
        return super.onCreateOptionsMenu(menu);

        //return true;
    }

    //Can add onOptionsItemSelected with new intent and either startactivity(intent) lauches share options or miShare.setShareIntent


}
