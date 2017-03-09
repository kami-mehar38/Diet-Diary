package a098.ramzan.kamran.paleodietdiary;

/**
 * This project PaleoDietDiary is created by Kamran Ramzan on 05-Mar-17.
 */

class EventInfo {

    private String eventName;
    private int eventIcon;
    private String eventDate;
    private String eventTime;
    private String eventDescription;
    private String eventClass;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getEventClass() {
        return eventClass;
    }

    void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }



    String getEventName() {
        return eventName;
    }

    void setEventName(String eventName) {
        this.eventName = eventName;
    }

    int getEventIcon() {
        return eventIcon;
    }

    void setEventIcon(int eventIcon) {
        this.eventIcon = eventIcon;
    }

    String getEventDate() {
        return eventDate;
    }

    void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    String getEventTime() {
        return eventTime;
    }

    void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    String getEventDescription() {
        return eventDescription;
    }

    void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
