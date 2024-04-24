DELETE FROM db_ty.ty_user_app_role where userAlias !='51029652' and userAlias !='51028391';
DELETE FROM db_ty.ty_user_app_role where appAlias != 'ty';
DELETE FROM db_ty.ty_user where id >3;
DELETE FROM db_ty.ty_tenant_app where tenantSerialNumber !='TY-0000002' and tenantSerialNumber !='TY-0000001';
DELETE FROM db_ty.ty_tenant_app where appAlias !='ty';
DELETE FROM db_ty.ty_tenant where id >2;
DELETE FROM db_ty.ty_task where id >3;
DELETE FROM db_ty.ty_supplier_company;
DELETE FROM db_ty.ty_supplier_app;
DELETE FROM db_ty.ty_supplier;
DELETE FROM db_ty.ty_server;
DELETE FROM db_ty.ty_owner ;
DELETE FROM db_ty.ty_module;
DELETE FROM db_ty.ty_app where id >1;
DELETE FROM db_ty.ty_app_role where appAlias !='ty';
DELETE FROM db_ty.ty_ds where id not in (2,3,13,25,43,44,45,46,47,50);
DELETE FROM db_ty.ty_api_type where id not in (1);
DELETE FROM db_ty.ty_api where `version` is null;
DELETE FROM db_ty.ty_output_field;
DELETE FROM db_ty.ty_input_field;

DELETE FROM db_ty.ty_api where `type` not in (select alias from db_ty.ty_api_type) or `type` is null;
DELETE FROM db_ty.ty_oi where dsAlias not in (select alias from db_ty.ty_ds);
DELETE FROM db_ty.ty_field where oiAlias not in (select alias from db_ty.ty_oi);
DELETE FROM db_ty.ty_api_lego where apiAlias not in (select alias from ty_api);
DELETE FROM db_ty.ty_page where appAlias not in (SELECT alias FROM db_ty.ty_app ) or appAlias is null;
DELETE FROM db_ty.ty_page_building_block where pageAlias not in (select alias from ty_page);
DELETE FROM db_ty.ty_page_building_block where pageAlias is null;

DELETE FROM db_ty.ty_file_storage;
DELETE FROM db_ty.ty_api_lego;
DELETE FROM db_ty.ty_api_log;

DELETE FROM db_ty.ty_connector where fromOIAlias not in (select alias from db_ty.ty_oi);

DROP TABLE db_ty.ty_api_lego;