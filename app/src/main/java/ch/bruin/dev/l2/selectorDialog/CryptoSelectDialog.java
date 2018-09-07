package ch.bruin.dev.l2.selectorDialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.R;
import ch.bruin.dev.l2.TranscodingHelper;

import java.io.Serializable;

public class CryptoSelectDialog extends DialogFragment implements Serializable {
    private TranscodingHelper callback;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_crypto_method_select, container);

        final FragmentTabHost mTabHost = view.findViewById(R.id.tabs);

        mTabHost.setup(getActivity(), getChildFragmentManager());
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Presets"), Fragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Manual"), Fragment.class, null);


        ProtocolsPagerAdapter adapter = new ProtocolsPagerAdapter(getChildFragmentManager(), getArguments());

        final ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        adapter.setDialog(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                mTabHost.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int i = mTabHost.getCurrentTab();
                viewPager.setCurrentItem(i);
            }
        });

        return view;
    }

    public void onMethodSelected(CryptoMethod method) {
        this.dismiss();
        if (method.requiresKey()) {
            AskKeyDialog dialog = new AskKeyDialog(method, callback, parentActivity);
            dialog.show();
        } else {
            callback.onMethodSelected(method);
        }
    }

    public void setParentActivity(AppCompatActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void setCallback(TranscodingHelper callback) {
        this.callback = callback;
    }
}
