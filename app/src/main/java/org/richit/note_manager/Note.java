package org.richit.note_manager;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    String name;
    String description;

    public Note(String name, String note) {
        this.name = name;
        this.description = note;
    }

    protected Note(Parcel in) {
        name = in.readString();
        description = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( name );
        parcel.writeString( description );
    }
}
