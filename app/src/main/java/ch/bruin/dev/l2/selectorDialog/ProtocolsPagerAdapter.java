package ch.bruin.dev.l2.selectorDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.Crypto.RSA;
import ch.bruin.dev.l2.Crypto.StoredCryptoMethod;

import java.util.ArrayList;
import java.util.Arrays;

public class ProtocolsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<CryptoMethod> presets = new ArrayList<>();
    private ArrayList<CryptoMethod> customs = new ArrayList<>();

    Bundle args;
    String [] titles;
    private CryptoSelectDialog dialog;

    public ProtocolsPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.args = bundle;

        presets.add(new StoredCryptoMethod("bob", new RSA(32), "1234".getBytes()));

        customs.add(new RSA(128));
        customs.add(new RSA(256));
        customs.add(new RSA(8));
    }

    @Override
    public Fragment getItem(int idx) {
        CryptoListFragment fragment = new CryptoListFragment();
        Bundle args = new Bundle();

        switch (idx) {
            case 0:
                args.putSerializable("protocols", presets);
                break;
            case 1:
                args.putSerializable("protocols", customs);
                break;
        }
        args.putSerializable("rootDialog", dialog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public void setDialog(CryptoSelectDialog dialog) {
        this.dialog = dialog;
    }
}