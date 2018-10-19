package com.a1104.lighthouse.ORMdb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "items")
public class Item {

    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_TEXT    = "text";
    public static final String FIELD_NAME_DESRIPTION    = "description";
    public static final String FIELD_NAME_REPEAT     = "repeat";
    public static final String FIELD_NAME_DATE     = "date";
    public static final String FIELD_NAME_DONE_DATE     = "doneDate";
    public static final String FIELD_NAME_TIME     = "time";
    public static final String FIELD_NAME_PROGRESS     = "progress";
    public static final String FIELD_NAME_PERSIST_TIL_DONE     = "persistTillDone";
    public static final String FIELD_NAME_DONE     = "done";

    public Item()
    {

    }

    public Item(String text)
    {
        this.done = false;
        this.date = new Date();
        this.description = "Empty";
        this.persistTillDone = false;
        this.progress = 0.0;
        this.repeat = false;
        this.text = text;
        this.time = "00:00";
    }

    // Fields
    @DatabaseField(columnName = FIELD_NAME_ID,generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_TEXT)
    private String text;

    @DatabaseField(columnName = FIELD_NAME_DESRIPTION)
    private String description;

    @DatabaseField(columnName = FIELD_NAME_REPEAT)
    private Boolean repeat;

    @DatabaseField(columnName = FIELD_NAME_DATE)
    private Date date;

    @DatabaseField(columnName = FIELD_NAME_DONE_DATE)
    private Date doneDate;

    @DatabaseField(columnName = FIELD_NAME_TIME)
    private String time;

    @DatabaseField(columnName = FIELD_NAME_PROGRESS)
    private Double progress;

    @DatabaseField(columnName = FIELD_NAME_PERSIST_TIL_DONE)
    private Boolean persistTillDone;

    @DatabaseField(columnName = FIELD_NAME_DONE)
    private Boolean done;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public Boolean getPersistTillDone() {
        return persistTillDone;
    }

    public void setPersistTillDone(Boolean persistTillDone) {
        this.persistTillDone = persistTillDone;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }
}
