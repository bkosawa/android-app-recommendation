package br.com.kosawalabs.apprecommendation.data.pojo;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App {
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
}