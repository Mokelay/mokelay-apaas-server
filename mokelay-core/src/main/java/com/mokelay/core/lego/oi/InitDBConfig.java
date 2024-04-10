package com.mokelay.core.lego.oi;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.db.bean.constant.DBMT;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.db.bean.view.DSView;
import com.mokelay.db.bean.view.OIView;
import com.mokelay.db.database.RelationalDataManager;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.db.manager.DSManager;
import com.mokelay.db.manager.FieldManager;
import com.mokelay.db.manager.OIManager;
import com.mokelay.api.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化数据库配置
 * <p/>
 * 输入
 * 1. ds的alias，保存DSView到数据库中
 * 2. 是否commit
 * <p/>
 * 输出
 * 1. 插入多少条OI，每个OI又插入多少条Field
 * <p/>
 * Author :CarlChen
 * Date:17/7/10
 */
@Component("init_db_config")
public class InitDBConfig implements Lego ,ExceptionCode {
    private static final String INPUT_DS_ALIAS_NAME = "ds_alias";
    private static final String INPUT_DS_INIT_COMMIT = "init_commit";

    private static final String OUTPUT_DS_COMMIT_REPORT = "commit_report";

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InitDBConfig.class);


    @Autowired
    private DSManager dsManager;
    @Autowired
    private OIManager oiManager;
    @Autowired
    private FieldManager fieldManager;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String dsAlias = input.getInputValue(INPUT_DS_ALIAS_NAME);
        boolean commit = DataUtil.getBoolean(input.getInputValue(INPUT_DS_INIT_COMMIT), false);

        List<CommitReport> commitReports = new ArrayList<CommitReport>();

        try {
            DS ds = dsManager.getDSByAlias(dsAlias);

            DBMT dbmt = DBMT.getDBMT(ds.getDst());
            if (dbmt == null) {
                throw new LegoException(ds.getDst() + "数据源不支持", ERROR_DB_DST_NOT_SUPPORT);
            }
            RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getApplicationContext().getBean(dbmt.getType());

            DSView dsView = dataManager.exportFromPhysicsDS(ds);
            // 保存DSView到数据库中
            if (dsView != null) {
                List<OIView> oiViews = dsView.getOiViews();
                for (OIView oiView : oiViews) {
                    CommitReport commitReport = new CommitReport();

                    OI oi = oiView.getOi();
                    //OI的alias应该全局OI唯一
                    //根据table Name 去获取OI
                    OI oi2 = oiManager.getOIByResource(dsAlias, oi.getResource());
                    if (oi2 == null && commit) {
                        //说明这个OI不存在，oi插入数据库
                        oiManager.add(oi);
                    }

                    commitReport.setResource(oi.getResource());
                    commitReport.setResourceAdd(oi2 == null);

                    List<String> addFields = new ArrayList<String>();

                    //判断数据库中的字段是否有多的，如果有多的，则插入
                    List<Field> fields = oiView.getFields();
                    for (Field field : fields) {
                        List<Field> fs = fieldManager.getFields(oi.getAlias(), field.getFieldName());
                        if (CollectionUtil.isValid(fs)) {
                            //说明已经有这个字段了
                            continue;
                        }
                        addFields.add(field.getFieldName());
                        if (commit) {
                            fieldManager.add(field);
                        }
                    }


                    commitReport.setAddFields(addFields);

                    if (CollectionUtil.isValid(commitReport.getAddFields())) {
                        commitReports.add(commitReport);
                    }
                }
            }

            output.setOutputValue(OUTPUT_DS_COMMIT_REPORT, commitReports);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new LegoException("初始化DB错误,"+e.getMessage(), ERROR_LEGO_INIT_DB_ERROR);
        }
    }
}

class CommitReport {
    private String resource;
    private boolean resourceAdd;

    private List<String> addFields;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean isResourceAdd() {
        return resourceAdd;
    }

    public void setResourceAdd(boolean resourceAdd) {
        this.resourceAdd = resourceAdd;
    }

    public List<String> getAddFields() {
        return addFields;
    }

    public void setAddFields(List<String> addFields) {
        this.addFields = addFields;
    }
}