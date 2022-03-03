package com.github.foodiestudio.boilerplate.playground;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AIDL 测试专用
 */
public class Haha implements Parcelable {
    private String content;

    public Haha() {

    }

    protected Haha(Parcel in) {
        content = in.readString();
    }

    public static final Creator<Haha> CREATOR = new Creator<Haha>() {
        @Override
        public Haha createFromParcel(Parcel source) {
            return new Haha(source);
        }

        @Override
        public Haha[] newArray(int size) {
            return new Haha[size];
        }
    };

    /**
     * 将数据写入到Parcel
     **/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
