# mokelay-apaas-server Mokelay Apaas 服务端核心库
- 核心提供模型，逻辑，UI三块可视化配置引擎，并且可导出交付物
- 配置引擎本身也是配置出来的，其配置物料内置在核心库中
- 导出物料只需要导出跟业务相关物料以及渲染引擎即可

# 配置能力说明 

## 配置数据模型
1. 支持数据库：Mysql,Sqlserver,Oracle
2. 

## 配置业务逻辑
1. 配置HTTP API
2. 配置 RPC Service
3. 配置 Job

## 配置UI
1. 支持终端：PC,H5,PAD
2. 

## 配置导出说明

# 本地安装
## 本地安装环境
1. JDK Version
2. OJDBC install
```shell
mvn install:install-file -Dfile=./ojdbc11.jar -DgroupId=com.oracle -DartifactId=ojdbc11 -Dversion=11.0.0 -Dpackaging=jar
```
- [Install Document](http://www.mokelay.com)

# Demo
[Demo](http://www.mokelay.com)