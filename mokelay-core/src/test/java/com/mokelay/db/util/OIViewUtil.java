package com.mokelay.db.util;

import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.view.OIView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by usagizhang on 17/12/28.
 */
public class OIViewUtil {
    public static Map<String, Field> getOIField(OIView oiView) {
        Map<String, Field> queryField = new HashMap<String, Field>();
        List<Field> fields = oiView.getFields();
        for (Field field : fields) {
            queryField.put(field.getFieldName(), field);
        }
        return queryField;
    }
}
