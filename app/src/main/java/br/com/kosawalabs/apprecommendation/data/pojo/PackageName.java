package br.com.kosawalabs.apprecommendation.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageName {
    @SerializedName("package_name")
    @Expose
    private String packageName;

    public PackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
}
