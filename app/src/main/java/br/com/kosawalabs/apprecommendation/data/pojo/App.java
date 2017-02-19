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
    @SerializedName("category_key")
    @Expose
    private String categoryKey;
    @SerializedName("developer_name")
    @Expose
    private String developerName;

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

    public String getCategoryKey() {
        return categoryKey;
    }

    public String getDeveloperName() {
        return developerName;
    }
}