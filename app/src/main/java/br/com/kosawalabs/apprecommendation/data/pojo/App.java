package br.com.kosawalabs.apprecommendation.data.pojo;

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

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDeveloperName() {
        return developerName;
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