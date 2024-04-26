package com.mokelay.db.lego;

import com.mokelay.MokelayBaseTest;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class MokelayDataTransferTest extends MokelayBaseTest {

    public void testExecute() throws LegoException {
        List<InputField> inputFields = new ArrayList<>();
        InputField inputField = new InputField();
        inputField.setFieldName(MokelayDataTransfer.Input_Key_Ignore_Transfer_OI);
        inputField.setFieldValue("ty_api,ty_api_lego,ty_auth_type,ty_ds,ty_oi,ty_field,ty_connector,ty_task");

        inputFields.add(inputField);

        Input input = new Input();
        input.setInputFields(inputFields);

        MokelayDataTransfer mokelayDataTransfer = (MokelayDataTransfer) context.getBean("mokelayDataTransfer");
        mokelayDataTransfer.execute(input, null);
    }
}