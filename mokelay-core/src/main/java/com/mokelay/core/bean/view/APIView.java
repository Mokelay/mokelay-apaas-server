package com.mokelay.core.bean.view;

import com.mokelay.core.bean.auth.AuthType;
import com.mokelay.core.bean.server.API;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/7
 */
public class APIView {
    public APIView() {
    }

    private API api;

    private List<AuthType> authTypes;

    private List<APILegoView> apiLegoViews;

    public API getApi() {
        return api;
    }

    public List<APILegoView> getApiLegoViews() {
        return apiLegoViews;
    }

    public void setApi(API api) {
        this.api = api;
    }

    public void setApiLegoViews(List<APILegoView> apiLegoViews) {
        this.apiLegoViews = apiLegoViews;
    }

    public List<AuthType> getAuthTypes() {
        return authTypes;
    }

    public void setAuthTypes(List<AuthType> authTypes) {
        this.authTypes = authTypes;
    }
}
