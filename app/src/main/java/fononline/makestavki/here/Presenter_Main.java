package fononline.makestavki.here;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.UUID;

public class Presenter_Main extends Presenter_Base<View_Main> {

    private String url;
    private String key;
    private Uri uriLocal;

    private CookieManager cookieManager;

    private class Dater {
        String uuid;
        long currentState;
    }

    private Dater dater = new Dater();

    @Override
    public void onCreateView(Bundle saveInstance) {
        mView.showProgress();
//        url = mView.getContext().getString(R.string.opening_url);
        url = "https://m.bwin.ru/ru/mobileportal/register?wm=4583973&utm_source=partners&utm_medium=paid&utm_campaign=wakeapp";
        key = mView.getContext().getString(R.string.riderect_url);
        if (saveInstance == null) {
            dater = new Dater();
            dater.uuid = UUID.randomUUID().toString();
        }

        cookieManager = CookieManager.getInstance();
    }

    @Override
    public void onStart() {
        dater.currentState = System.currentTimeMillis();
    }

    @Override
    public void onStop() {
        dater.currentState = 0;
    }


    void go(WebView webView, Uri uri) {
        mView.hideProgress();
        uriLocal = uri;

        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView,true);
        }
        webView.setWebViewClient(base());
        init(webView.getSettings());
        String link = uriLocal != null ? getTransformUrl(uriLocal, url) : url;
        Log.i("TEST_DEEP", link);

        String cookie = cookieManager.getCookie(url);
        Log.d("COOKIE", cookie);

        webView.loadUrl(link);
    }

    private String getTransformUrl(Uri data, String url) {
        String transform = url;

        String QUERY_1 = "sub_id_1";
        String QUERY_2 = "sub_id_2";
        String QUERY_3 = "sub_id_3";

        if (data.getEncodedQuery().contains(QUERY_1)) {
            String queryValueFirst = "?sub_id_1=" + data.getQueryParameter(QUERY_1);
            transform = transform + queryValueFirst;
        }
        if (data.getEncodedQuery().contains(QUERY_2)) {
            String queryValueSecond = "&sub_id_2=" + data.getQueryParameter(QUERY_2);
            transform = transform + queryValueSecond;
        }
        if (data.getEncodedQuery().contains(QUERY_3)) {
            String queryValueSecond = "&sub_id_3=" + data.getQueryParameter(QUERY_3);
            transform = transform + queryValueSecond;
        }

        return transform;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(WebSettings webSettings) {
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
    }

    private WebViewClient base() {
        return new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieSyncManager.getInstance().sync();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.contains(key)) {
                    view.loadUrl(url);
                } else {
                    mView.openStavki();
                }
                mView.onOverloading(url);
                return true;
            }
        };
    }
}
