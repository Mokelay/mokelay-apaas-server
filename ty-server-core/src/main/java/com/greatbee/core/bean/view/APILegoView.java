package com.greatbee.core.bean.view;

import com.greatbee.core.bean.server.APILego;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.bean.server.OutputField;

import java.util.List;

/**
 * APILegoView
 * <p/>
 * Created by ty on 2017/7/8.
 */
public class APILegoView {
    private APILego apiLego;
    private List<InputField> inputFields;
    private List<OutputField> outputFields;

    public APILego getApiLego() {
        return apiLego;
    }

    public List<InputField> getInputFields() {
        return inputFields;
    }

    public List<OutputField> getOutputFields() {
        return outputFields;
    }

    public void setApiLego(APILego apiLego) {
        this.apiLego = apiLego;
    }

    public void setInputFields(List<InputField> inputFields) {
        this.inputFields = inputFields;
    }

    public void setOutputFields(List<OutputField> outputFields) {
        this.outputFields = outputFields;
    }
}
