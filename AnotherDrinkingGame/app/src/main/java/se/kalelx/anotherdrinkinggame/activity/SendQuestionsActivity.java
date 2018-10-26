package se.kalelx.anotherdrinkinggame.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import se.kalelx.anotherdrinkinggame.R;

public class SendQuestionsActivity extends AppCompatActivity {

    private WebView mFormWebView;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, SendQuestionsActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_questions);
        mFormWebView = findViewById(R.id.form_web_view);
        mFormWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        mFormWebView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSc_KYY05js_uNNEceNJSMXE1D68foWIsTJTthSSWT5Mnu07dg/viewform?usp=sf_link");
        WebSettings webSettings = mFormWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}
