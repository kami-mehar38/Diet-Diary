package a098.ramzan.kamran.paleodietdiary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This project PaleoDietDiary is created by Kamran Ramzan on 05-Mar-17.
 */

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private Context context;
    private List<EventInfo> eventInfoList = new ArrayList<>();
    private DateUtills dateUtills;

    EventsAdapter(Context context) {
        this.context = context;
        dateUtills = new DateUtills(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventInfo eventInfo = eventInfoList.get(position);
        holder.eventName.setText(eventInfo.getEventName());
        holder.eventIcon.setImageDrawable(context.getResources().getDrawable(eventInfo.getEventIcon()));
        holder.eventDescription.setText(eventInfo.getEventDescription());
        String[] timeFormatted = eventInfo.getEventTime().split(":");

        holder.eventTime.setText(dateUtills.getRemindTime(Integer.parseInt(timeFormatted[0]), Integer.parseInt(timeFormatted[1].split(" ")[0])));
        holder.eventDate.setText(eventInfo.getEventDate());
        holder.eventClass.setText(eventInfo.getEventClass());
        holder.id.setText(String.valueOf(eventInfo.getId()));
    }

    @Override
    public int getItemCount() {
        return eventInfoList.size();
    }

    void addItem(EventInfo eventInfo) {
        eventInfoList.add(eventInfo);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        eventInfoList.remove(position);
        notifyDataSetChanged();
    }

    void removeAllItems(){
        if (eventInfoList.size() > 0) {
            eventInfoList.clear();
            notifyDataSetChanged();
        }
    }

    List<EventInfo> getEventInfoList() {
        return eventInfoList;
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eventName;
        private ImageView eventIcon;
        private TextView eventDescription;
        private TextView eventTime;
        private TextView eventDate;
        private LinearLayout parent;
        private TextView eventClass;
        private TextView id;
        ViewHolder(View itemView) {
            super(itemView);
            setIsRecyclable(false);
            eventName = (TextView) itemView.findViewById(R.id.eventName);
            eventIcon = (ImageView) itemView.findViewById(R.id.eventIcon);
            eventDescription = (TextView) itemView.findViewById(R.id.eventDescription);
            eventTime = (TextView) itemView.findViewById(R.id.eventTime);
            eventDate = (TextView) itemView.findViewById(R.id.date);
            eventClass = (TextView) itemView.findViewById(R.id.classs);
            id = (TextView) itemView.findViewById(R.id.eventId);
            parent = (LinearLayout) itemView.findViewById(R.id.parent);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditEvent.class);
                    intent.putExtra(EditEvent.ID_ARG, Integer.parseInt(id.getText().toString().trim()));
                    intent.putExtra(EditEvent.CLASS_ARG, eventClass.getText().toString().trim());
                    intent.putExtra(EditEvent.NAME_ARG, eventName.getText().toString().trim());
                    intent.putExtra(EditEvent.DATE_ARG, eventDate.getText().toString().trim());
                    intent.putExtra(EditEvent.TIME_ARG, eventTime.getText().toString().trim());
                    intent.putExtra(EditEvent.DESCRIPTION_ARG, eventDescription.getText().toString().trim());
                    context.startActivity(intent);
                }
            });
        }
    }
}
