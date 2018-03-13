package hm.com.giftsonli.apgo;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.applinks.AppLinkData;

import io.reactivex.Single;

public class PresenterMain extends BasePresenter<ViewMain> {

    private String keyRedirect;
    private Uri uriLocal;

    @Override
    public void onCreateView(Bundle saveInstance) {
        mView.showProgress();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }


    void showWebView(String url, String keyRedirect, Uri uriLocal) {
        this.keyRedirect = keyRedirect;
        this.uriLocal = uriLocal;
        openWebView(url);
        mView.hideProgress();

    }



    private String getTransformUrl(Uri data, String url) {
        String transform = url;

        String QUERY_1 = "sub1";
        String QUERY_2 = "sub2";

        String QUERY_1_1 = "sub1";
        String QUERY_2_1 = "sub2";

        if (data.getEncodedQuery().contains(QUERY_1_1)) {
            String queryValueFirst = "sub1=" + data.getQueryParameter(QUERY_1_1);
            transform = transform.replace(QUERY_1,  queryValueFirst);
        }
        if (data.getEncodedQuery().contains(QUERY_2_1)) {
            String queryValueSecond = "sub2=" + data.getQueryParameter(QUERY_2_1);
            transform = transform.replace(QUERY_2, queryValueSecond);
        }
        if (transform.contains("=custom")) {
            transform.replaceAll("=custom", "");
        }
        return transform;
    }

    private void openWebView(String url) {
        mView.getWebView().setWebViewClient(createWebClient());
        initWebSettings( mView.getWebView().getSettings());
        mView.getWebView().loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSettings(WebSettings webSettings) {
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
    }

    private WebViewClient createWebClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.contains(keyRedirect)) {
                    if (url.contains("go.wakeapp.ru") && uriLocal != null) {
                        view.loadUrl(getTransformUrl(uriLocal, url));
                    } else {
                        view.loadUrl(url);
                    }
                } else {
                    mView.onErrorOther();
                }

                return true;
            }
        };
    }
}