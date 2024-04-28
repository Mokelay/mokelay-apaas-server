package com.mokelay.db.lego;

import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.DataList;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.core.manager.YamlDSViewManager;
import com.mokelay.db.bean.view.DSView;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.database.mysql.manager.MysqlDataManager;
import com.mokelay.db.database.yaml.YamlDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mokelay Data Transfer
 * <p>
 * 1. 获取所有DS配置信息，读取OI列表
 * 2. 根据OI列表，读取DB，取出原数据
 * 3. 写入对应的OI存储中去
 */
@Component("mokelayDataTransfer")
public class MokelayDataTransfer implements Lego {
    @Autowired
    private YamlDSViewManager dsViewManager;
    @Autowired
    private MysqlDataManager mysqlDataManager;
    @Autowired
    private YamlDataManager yamlDataManager;

    public static final String Input_Key_Ignore_Transfer_OI = "ignore_transfer_oi";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String ignoreStr = (String) input.getInputObjectValue(Input_Key_Ignore_Transfer_OI);
        if (StringUtil.isInvalid(ignoreStr)) {
            ignoreStr = "";
        }
        String[] ignoreOIs = ignoreStr.split(",");

        try {
            List<DSView> dsViewList = dsViewManager.list();
            if (CollectionUtil.isValid(dsViewList)) {
//                List<String> result = new ArrayList<>();
                for (DSView dsView : dsViewList) {
                    List<OIView> oiViews = dsView.getOiViews();
                    for (OIView oiView : oiViews) {
                        boolean ignore = false;
                        for (String i : ignoreOIs) {
                            if (i.equalsIgnoreCase(oiView.getOi().getAlias())) {
                                ignore = true;
                                break;
                            }
                        }
                        if (!ignore) {
                            DataList dataList = mysqlDataManager.list(oiView.getOi(), oiView.getFields(), null);
                            System.out.println(oiView.getOi().getResource() + " Data Size:" + dataList.getTotalRecords());

                            yamlDataManager.batchCreate(oiView.getOi(), dataList.getList());
                        } else {
                            System.out.println(oiView.getOi().getResource() + " Ignore....");
                        }
                    }
                }

//                System.out.println(result);
            }
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }
}
