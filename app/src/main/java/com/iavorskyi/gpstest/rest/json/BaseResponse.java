package com.iavorskyi.gpstest.rest.json;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("Success")
    private boolean success = false;
    @SerializedName("DeprecatedVersion")
    private boolean deprecatedVersion = false;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isDeprecatedVersion() {
        return deprecatedVersion;
    }

    public void setDeprecatedVersion(boolean deprecatedVersion) {
        this.deprecatedVersion = deprecatedVersion;
    }
}
