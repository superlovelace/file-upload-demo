# 文件上传
支持大文件分片上传，断点续传和秒传。

## 核心思想

分片上传主要是前端对文件进行sha256指纹校验并进行分片。通过多线程并发上传分片实现快速上传。断点续传主要是由服务器记录已上传的分片信息，前端上传前先通过进度状态接口查询是否有已经上传一部分的文件，然后返回已上传的分片序号，从未上传的地方开始继续上传，从而达到跨终端上传进度共享。

## 后端实现
### 1. 通过ConcurrentHashMap记录已上传的分片信息(目标文件：IndexController)
这种实现方式简单，适合单机架构。但是若上传中断，且后续不再上传，会一直占用内存，需根据需要制定策略删除过期的分片信息
### 2. 通过Redis记录已上传的分片信息(目标文件：IndexController2)
这种适合分布式架构，若redis使用集群，单个主库可以不变，多主库的情况下，需要考虑使用分布式联锁确保分片信息一致性
## 使用方法
### 1. 手动创建数据库（file_uploads_db）
```mysql
CREATE DATABASE IF NOT EXISTS file_uploads_db
    DEFAULT CHARSET utf8mb4  COLLATE utf8mb4_general_ci ;
```
### 2. 修改数据源配置(application.yml)
```yaml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql:///file_uploads_db?useUnicode=true&characterEncoding=utf-8&serverTimeZone=UTC&useSSL=false
    driver-class-name: com.mysql.cj.jdbc.Driver

  redis:
    redisson: # 若需要分布式锁，则配置redisson.yaml文件
      file: classpath:redisson.yaml
    host: localhost
    port: 6379
    timeout: 10000
    database: 0
    lettuce:
      pool:
        max-idle: 8
        max-active: 8
        max-wait: 100ms
        min-idle: 0
        enabled: true
```
### 3. 启动
启动类：`Main.java`
### 4. 访问localhost:8080
### 5. 上传文件

### 6. 文件下载
上传可以通过断点续传的方式，下载也可以通过断点续传的方式。下载时的断点续传无需额外配置，浏览器会自动记录断点续传信息。
需要说明的是， 文件下载时，浏览器的断点续传信息是存储在cookie中的，因此，若浏览器关闭，则断点续传信息将丢失。
文件下载将中断失效，需要从头开始重新下载。
为了避免这种情况，可使用IDM或者 迅雷等专用下载软件进行下载，这类软件会记录断点续传信息到本地，这样即使中途有事主动退出软件或者关闭电脑，断点续传信息也不会丢失。
还需注意的是，迅雷在创建下载时，使用的是默认的文件名（基本上是根据下载链接后缀获取的，但下载完成后，文件名会改回服务器响应的文件名）

界面示例：

![文件上传示例图片](https://github.com/superlovelace/file-upload-demo/blob/master/src/main/resources/static/%E6%96%87%E4%BB%B6%E4%B8%8A%E4%BC%A0%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87.png)
