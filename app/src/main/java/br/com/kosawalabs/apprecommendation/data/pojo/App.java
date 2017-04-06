package br.com.kosawalabs.apprecommendation.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category_key")
    @Expose
    private String categoryKey;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("developer_name")
    @Expose
    private String developerName;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("content_rating")
    @Expose
    private String contentRating;
    @SerializedName("size")
    @Expose
    private String size;

    public Integer getId() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

    @NonNull
    public String getName() {
        return name != null ? name : "";
    }

    @NonNull
    public String getIconUrl() {
        return iconUrl != null ? iconUrl : "";
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    @NonNull
    public String getCategoryName() {
        return categoryName != null ? categoryName : "";
    }

    @NonNull
    public String getDeveloperName() {
        return developerName != null ? developerName : "";
    }

    public String getVersion() {
        return version;
    }

    public String getContentRating() {
        return contentRating;
    }

    public String getSize() {
        return size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.packageName);
        dest.writeString(this.name);
        dest.writeString(this.iconUrl);
        dest.writeString(this.description);
        dest.writeString(this.categoryKey);
        dest.writeString(this.categoryName);
        dest.writeString(this.developerName);
        dest.writeString(this.version);
        dest.writeString(this.contentRating);
        dest.writeString(this.size);
    }

    public App() {
    }

    protected App(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.packageName = in.readString();
        this.name = in.readString();
        this.iconUrl = in.readString();
        this.description = in.readString();
        this.categoryKey = in.readString();
        this.categoryName = in.readString();
        this.developerName = in.readString();
        this.version = in.readString();
        this.contentRating = in.readString();
        this.size = in.readString();
    }

    public static final Parcelable.Creator<App> CREATOR = new Parcelable.Creator<App>() {
        @Override
        public App createFromParcel(Parcel source) {
            return new App(source);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };
}