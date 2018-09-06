package ch.bruin.dev.l2.selectorDialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.bruin.dev.l2.Crypto.CryptoMethod;
import ch.bruin.dev.l2.R;

import java.util.ArrayList;

public class CryptoMethodListAdapter extends ArrayAdapter<CryptoMethod> implements View.OnClickListener {

    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtFamily;
        TextView txtDescription;
        ImageView imgStrength;
    }

    public CryptoMethodListAdapter(ArrayList<CryptoMethod> data, Context context) {
        super(context, R.layout.fragment_item, data);
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        CryptoMethod dataModel=(CryptoMethod) object;
        Log.wtf("AAAAA", ""+v.getId());
        switch (v.getId())
        {
            case R.id.container:
                Snackbar.make(v, "Release date " + dataModel.getDescription(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        CryptoMethod dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_item, parent, false);
            viewHolder.txtFamily = (TextView) convertView.findViewById(R.id.family);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.description);
            viewHolder.imgStrength = (ImageView) convertView.findViewById(R.id.img_strength);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtFamily.setText(dataModel.getFamily());
        viewHolder.txtDescription.setText(dataModel.getShortDescription());
        // Return the completed view to render on screen

        return convertView;
    }
}
