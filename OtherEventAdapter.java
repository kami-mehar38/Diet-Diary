package a098.ramzan.kamran.paleodietdiary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This project PaleoDietDiary is created by Kamran Ramzan on 04-Mar-17.
 */

class OtherEventAdapter extends ArrayAdapter<OtherEventInfo> {

    private Context context;
    private List<OtherEventInfo> list = new ArrayList<>();

    OtherEventAdapter(Context context, int id, int eventName, List<OtherEventInfo> list) {
        super(context, id, eventName, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);

    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.others_new_event, parent, false);

        }

        TextView textView = (TextView) convertView.findViewById(R.id.eventName);
        textView.setText(list.get(position).getEventName());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.eventIcon);
        imageView.setImageResource(list.get(position).getEventIcon());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

}

