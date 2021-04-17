package org.richit.note_manager;

public class Note {
    String name;
    String description;

    public Note(String name, String note) {
        this.name = name;
        this.description = note;
    }

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


}
