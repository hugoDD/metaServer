# Variant Server
## Backend Server of VDP project

在线演示：http://120.92.142.115:8080/

前端代码仓库：https://github.com/vdpAdmin/VariantAdmin

国内gitee仓库：https://gitee.com/vdpadmin/VariantAdmin

语雀文档：https://www.yuque.com/variantdev/atxy8t

掘金文章汇总：https://juejin.cn/user/2076298067593629/posts

QQ技术交流群号：836657858

#### 1. 依赖环境
```
JDK 1.8+
MySQL 5.5+（推荐5.7）
Tomcat 8.5+（生产环境需要）
```

#### 2. 导入数据库脚本
```
使用数据库管理工具（推荐HeidiSQL）连接到MySQL数据库，导入并运行variantorm_db.sql。
```
注意：数据库仅需导入一次，请勿重复导入数据库，如果导入前已存在同名数据库，则导入后会覆盖原有数据且不可恢复！

#### 3. 参数配置
```
a. 编辑src\main\resources\application.yml文件，修改datasource数据库连接、用户及密码等参数;
b. 图片、文件存储路径请修改application-upload.yml，建议使用绝对路径。
```

#### 4. 导入项目
```
这是一个基于Maven构建的SpringBoot项目，可以用Eclipse或IDEA（推荐）导入后进行开发、调试。
```

#### 5. 构建
```
使用Eclipse、IDEA内置的构建功能生成war包，或者使用maven打包插件生成war包。
```

#### 6. 生产部署
```
将构建好的war文件改名为ROOT.rar，然后扔到Tomcat的webapps目录下，启动Tomcat。
```
注意：为避免中文乱码问题，应事先修改Tomcat配置文件conf\server.xml，在Connector配置加入如下属性：
```
URIEncoding="UTF-8" 
```
