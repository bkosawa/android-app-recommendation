package br.com.kosawalabs.apprecommendation.data;

public class DataError {
    private String cause;

    public DataError(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }
}
