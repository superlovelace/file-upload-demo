# 文件上传
支持大文件分片上传，断点续传和秒传。

## 核心思想

分片上传主要是前端对文件进行sha256指纹校验并进行分片。通过多线程并发上传分片实现快速上传。断点续传主要是由服务器记录已上传的分片信息，前端上传前先通过进度状态接口查询是否有已经上传一部分的文件，然后返回已上传的分片序号，从未上传的地方开始继续上传，从而达到跨终端上传进度共享。

## 使用方法
1. 手动创建数据库（file_uploads_db）
```mysql
CREATE DATABASE IF NOT EXISTS file_uploads_db
    DEFAULT CHARSET utf8mb4  COLLATE utf8mb4_general_ci ;
```
2. 修改数据源配置(application.yml)
3. 启动
4. 访问localhost:8080
5. 上传文件

