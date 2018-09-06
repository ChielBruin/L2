package ch.bruin.dev.l2.selectorDialog;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.R;

import java.util.ArrayList;

public class CryptoListFragment extends ListFragment {

    public CryptoListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_tab1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<CryptoMethod> protocols;

        try {
            // noinspection unchecked
            protocols = (ArrayList<CryptoMethod>) getArguments().getSerializable("protocols");
        } catch (ClassCastException e) {
            Log.e("ListFragment", "Could not deserialize the available protocols, using an empty list instead.");
            protocols = new ArrayList<>();
        }

        CryptoMethodListAdapter adapter = new CryptoMethodListAdapter(protocols, getContext());
        setListAdapter(adapter);

        final ArrayList<CryptoMethod> finalProtocols = protocols;
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CryptoMethod method = finalProtocols.get(i);
                CryptoSelectDialog dialog = (CryptoSelectDialog) getArguments().getSerializable("rootDialog");
                dialog.onMethodSelected(method);
            }
        });

    }

}
