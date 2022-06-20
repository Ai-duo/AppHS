package com.add.appxm;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.add.appxm.databinding.ActivityWebviewBinding;

public class FourthFragment extends Fragment {
    ActivityWebviewBinding binding;
    WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.activity_webview, container, false);
            webView = binding.web;
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setInitialScale(100);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);

        }
        webView.loadUrl("https://things.iot198.com/xypa/view/dashboard/showLED.jsp");
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        if (webView!=null){
            webView.clearCache(true);
            webView.clearFormData();
        }
        super.onDestroy();
    }
}
