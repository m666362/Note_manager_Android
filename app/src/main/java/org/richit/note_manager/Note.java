package org.richit.note_manager;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    String title;
    String description;
    String alarm;

    public Note(String title, String description, String alarm) {
        this.title = title;
        this.description = description;
        this.alarm = alarm;
    }

    protected Note(Parcel in) {
        title = in.readString();
        description = in.readString();
        alarm = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note( in );
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( title );
        parcel.writeString( description );
        parcel.writeString( alarm );
    }
}
