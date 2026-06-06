# WO 项目入门指南 —— 从零开始理解整个项目

> 本文写给编程零基础的同学。不要求你懂任何技术术语，我会用**生活类比**解释每个概念。

---

## 目录

1. [这个项目是什么](#1-这个项目是什么)
2. [你需要装什么软件](#2-你需要装什么软件)
3. [项目长什么样（目录结构）](#3-项目长什么样)
4. [前端是什么（用户能看到的部分）](#4-前端是什么)
5. [后端是什么（用户看不到的部分）](#5-后端是什么)
6. [数据库是什么（数据存在哪）](#6-数据库是什么)
7. [一个功能怎么从头跑到尾](#7-一个功能怎么从头跑到尾)
8. [每个文件是干什么的](#8-每个文件是干什么的)
9. [怎么启动项目](#9-怎么启动项目)

---

## 1. 这个项目是什么

**WO** 是一个游戏玩家交流社区。你可以：

- 注册账号 / 登录
- 发帖子（文字 + 图片）
- 看别人发的帖子，点赞、收藏、评论
- 关注其他玩家
- 给关注的人发私信聊天
- 看游戏攻略和资讯

就像一个小型的"贴吧 + 微博 + 微信"的结合体，专为游戏玩家设计。

### 项目分成三大部分

```
┌─────────────────┐
│   前端（你看到的） │  ← 网页/手机App，负责显示界面
├─────────────────┤
│   后端（你看不到） │  ← 服务器程序，负责处理数据
├─────────────────┤
│   数据库（隐藏的） │  ← 硬盘上的表格，负责存储信息
└─────────────────┘
```

**类比**：就像餐厅——
- 前端 = 菜单和餐桌（你看得见摸得着）
- 后端 = 厨房和厨师（你看不见，但做的菜从这来）
- 数据库 = 冰箱和仓库（存食材的地方）

---

## 2. 你需要装什么软件

### 2.1 JDK 17 —— Java 运行环境

**它是啥**：Java 语言的"翻译官"。后端代码是用 Java 写的，需要 JDK 来"翻译"成电脑能执行的指令。

**怎么装**：
1. 去 https://jdk.java.net/17/ 下载 Windows 版
2. 解压到 `C:\Program Files\Java\jdk-17`
3. 设置环境变量（具体搜"JDK 17 Windows 安装教程"）

**验证装好了没**：
```bash
java -version
# 应该显示 "17.0.x"
```

### 2.2 Maven —— 项目管家

**它是啥**：自动帮你下载和管理 Java 项目需要的各种"零件"（这些零件叫"依赖"或"库"）。你不用手动去找几百个文件，Maven 一键全搞定。

**怎么装**：
1. 下载 https://maven.apache.org/download.cgi
2. 解压到任意目录（比如 `D:\maven`）
3. 设置环境变量 `MAVEN_HOME` 指向它

**验证**：
```bash
mvn --version
```

### 2.3 MySQL 8.0 —— 数据库

**它是啥**：一个专门存表格数据的软件。就像 Excel，但能存几百万行数据，而且查找速度极快。

**怎么装**：
1. 下载 MySQL 8.0 社区版 https://dev.mysql.com/downloads/mysql/
2. 安装时记住 root 密码（本项目用 `root123`）
3. 安装后 MySQL 会在后台自动运行（开机自启）

**验证**：
```bash
mysql -u root -proot123 -e "SELECT 1"
# 应该显示 "1"
```

### 2.4 Node.js —— 前端运行环境

**它是啥**：前端的"翻译官"。前端 Vue 代码需要 Node.js 来运行和编译。

**怎么装**：
1. 去 https://nodejs.org 下载 LTS 版本
2. 一路点"下一步"安装

**验证**：
```bash
node --version   # 应该 >= 20.0
npm --version    # 应该 >= 10.0
```

### 2.5 把这些装好后

进入项目目录，运行：

```bash
# 安装前端依赖（只需要做一次）
cd wo
npm install

# 启动前端开发服务器
npm run dev:h5
```

另开一个终端：

```bash
# 启动后端
cd wo/server
mvn spring-boot:run
```

浏览器打开 `http://localhost:5173`，搞定。

---

## 3. 项目长什么样

```
wo/                              ←  项目根目录（你下载代码的地方）
│
├── package.json                 ←  前端依赖清单（需要哪些零件，npm install 时读它）
├── vite.config.js               ←  vite 配置文件（告诉 vite 怎么编译前端代码）
├── index.html                   ←  H5 网页的入口文件（浏览器先打开它）
│
├── src/                         ←  前端代码（所有页面、组件都在里面）
│   ├── main.js                  ←  Vue 应用的人口（"程序从这里开始"）
│   ├── App.vue                  ←  Vue 根组件（所有页面的框架）
│   ├── pages.json               ←  页面路由（哪个 URL 对应哪个页面）
│   ├── manifest.json            ←  应用配置（App名称/图标/权限等）
│   ├── uni.scss                 ←  uni-app 内置样式变量
│   │
│   ├── pages/                   ←  页面（每一个 .vue 文件就是一个页面）
│   │   ├── index/index.vue      ← 首页/发现页
│   │   ├── post/list.vue        ← 帖子列表页
│   │   ├── post/detail.vue      ← 帖子详情页
│   │   ├── post/publish.vue     ← 发布帖子页
│   │   ├── user/login.vue       ← 登录页
│   │   ├── user/profile.vue     ← 个人主页（"我的"Tab）
│   │   ├── user/view.vue        ← 他人主页
│   │   ├── user/settings.vue    ← 设置页（改头像/昵称）
│   │   ├── message/list.vue     ← 消息列表（会话列表）
│   │   ├── message/chat.vue     ← 聊天页
│   │   └── ...更多页面
│   │
│   ├── components/              ←  组件（页面的"零件"，可重复使用）
│   │   ├── post/PostCard.vue    ← 帖子卡片组件
│   │   ├── comment/CommentList.vue ← 评论列表组件
│   │   └── ...更多组件
│   │
│   ├── api/                     ←  API 请求（告诉前端怎么跟后端"说话"）
│   │   ├── post.js              ← 帖子相关API
│   │   ├── user.js              ← 用户相关API
│   │   └── ...
│   │
│   ├── stores/                  ←  状态管理（全局数据仓库）
│   │   ├── user.js              ← 用户信息仓库
│   │   ├── post.js              ← 帖子数据仓库
│   │   └── ...
│   │
│   ├── utils/                   ←  工具函数
│   │   ├── request.js           ← 网络请求封装（核心！前后端通信靠它）
│   │   ├── auth.js              ← 登录/登出工具
│   │   ├── date.js              ← 日期格式化工具
│   │   └── ...
│   │
│   └── styles/                  ←  样式
│       ├── variables.scss       ← 颜色/字体/间距变量定义
│       ├── mixins.scss          ← 可复用的样式"配方"
│       └── global.scss          ← 全局基础样式
│
├── server/                      ←  后端代码（Java Spring Boot）
│   ├── pom.xml                  ←  后端依赖清单（需要哪些 Java 零件，Maven 读它）
│   ├── Dockerfile               ←  Docker 镜像打包文件（生产环境用）
│   ├── docker-compose.yml       ←  Docker 一键部署（启动全部服务）
│   ├── nginx.conf               ←  Nginx 配置（前端网页托管+代理后端）
│   │
│   └── src/
│       ├── main/java/com/wo/    ←  核心 Java 代码
│       │   ├── WoApplication.java     ← 程序的入口（main方法就在这）
│       │   │
│       │   ├── config/           ← 各种配置类
│       │   │   ├── SaTokenConfig.java        ← 登录权限配置
│       │   │   ├── CorsConfig.java           ← 跨域配置（允许前端访问后端）
│       │   │   ├── MyBatisPlusConfig.java    ← 数据库连接配置
│       │   │   ├── MetaObjectHandlerConfig.java ← 自动填充时间字段
│       │   │   └── WebMvcConfig.java         ← Web 配置（静态文件映射）
│       │   │
│       │   ├── controller/       ← 控制器（接收前端请求的"前台"）
│       │   │   ├── AuthController.java       ← 登录/注册/退出
│       │   │   ├── UserController.java       ← 用户信息/修改资料
│       │   │   ├── PostController.java       ← 帖子增删查改
│       │   │   ├── CommentController.java    ← 评论增删查+点赞
│       │   │   ├── GuideController.java      ← 攻略增删查+收藏
│       │   │   ├── NewsController.java       ← 资讯列表/详情
│       │   │   ├── SocialController.java     ← 关注/粉丝/私信
│       │   │   ├── NotificationController.java ← 通知
│       │   │   ├── GameController.java       ← 游戏分类/标签
│       │   │   ├── ReportController.java     ← 举报
│       │   │   └── UploadController.java     ← 图片上传
│       │   │
│       │   ├── service/          ← 服务接口（定义"做什么"）
│       │   │   ├── UserService.java          ← 用户服务接口
│       │   │   ├── PostService.java          ← 帖子服务接口
│       │   │   └── ...
│       │   │
│       │   ├── service/impl/     ← 服务实现（具体"怎么做"）
│       │   │   ├── UserServiceImpl.java      ← 用户服务的具体实现
│       │   │   ├── PostServiceImpl.java      ← 帖子服务的具体实现（300+行核心逻辑）
│       │   │   ├── CommentServiceImpl.java   ← 评论服务实现
│       │   │   ├── SocialServiceImpl.java    ← 关注/私信服务实现
│       │   │   ├── FileUploadService.java    ← 图片上传服务
│       │   │   └── ...
│       │   │
│       │   ├── mapper/           ← 数据访问器（操作数据库的"遥控器"）
│       │   │   ├── UserMapper.java           ← 操作用户表
│       │   │   ├── PostMapper.java           ← 操作帖子表（含原子计数SQL）
│       │   │   └── ...
│       │   │
│       │   ├── entity/           ← 实体类（对应数据库表，一行数据 = 一个对象）
│       │   │   ├── User.java                  ← 用户实体
│       │   │   ├── Post.java                  ← 帖子实体
│       │   │   └── ...
│       │   │
│       │   ├── dto/              ← 数据传输对象（前后端之间的"包裹"）
│       │   │   ├── request/      ←  请求对象（前端发给后端的格式）
│       │   │   └── response/     ←  响应对象（后端返回给前端的格式）
│       │   │
│       │   └── common/           ← 通用工具
│       │       ├── Result.java               ← 统一响应格式
│       │       ├── PageReq.java              ← 分页请求基类
│       │       └── exception/
│       │           ├── BusinessException.java    ← 业务异常类
│       │           └── GlobalExceptionHandler.java ← 全局异常捕获
│       │
│       ├── main/resources/       ← 资源文件
│       │   ├── application.yml          ← 主配置
│       │   ├── application-dev.yml      ← 开发环境配置（数据库连接信息）
│       │   └── db/                      ← SQL脚本（建表语句）
│       │
│       └── test/                 ← 单元测试代码
│           ├── java/com/wo/service/
│           │   ├── UserServiceTest.java      ← 测试用户功能
│           │   ├── PostServiceTest.java      ← 测试帖子功能
│           │   └── ...
│           └── resources/
│               ├── application.yml         ← 测试用配置（用H2内存数据库）
│               └── test-schema.sql         ← 测试用建表语句
│
└── README.md                    ←  项目文档（面向开发者）
```

---

## 4. 前端是什么（用户能看到的部分）

前端是用 `uni-app`（一个基于 Vue 3 的框架）写的。它负责所有你能看到和点击的部分——页面布局、按钮、输入框、颜色、动画、弹窗等。

### 4.1 核心概念解释

#### 页面（.vue 文件）

每个 `.vue` 文件就是一个页面。它包含三个部分：

```vue
<template>         ←  页面长什么样（HTML 结构）
<script setup>     ←  页面做什么（JavaScript 逻辑）
<style scoped>     ←  页面什么颜色/大小（CSS 样式）
```

#### 组件（Components）

组件就像"乐高积木"——你可以把小积木拼成一个大城堡。比如 `PostCard.vue` 是一个"帖子卡片积木"，哪里需要显示帖子就把它放过去。好处是相同的东西不用重复写。

#### 路由（pages.json）

路由就是"地址簿"——告诉程序：URL 是 `/pages/post/detail` 时显示帖子详情页，URL 是 `/pages/user/login` 时显示登录页。

#### 生命周期函数

- `onLoad` —— 页面第一次打开时执行（只执行一次）
- `onShow` —— 每次页面显示时执行（切换回来也执行）
- `onMounted` —— 组件挂载到视图后执行

Tab 页（底部的按钮页面）用 `onShow`，因为切换 Tab 不会重新创建页面，只会触发 `onShow`。

#### 状态管理（Pinia Store）

就像一个"全局变量仓库"。比如用户登录后信息存在 `user.js` Store 里，任何页面都能读取。不用每个页面单独请求一遍后端。

### 4.2 request.js —— 最重要的工具函数

```javascript
// 位置: src/utils/request.js

// 这个文件就像邮递员。页面想跟后端"说话"，
// 不需要知道后端地址/加密/错误处理这些细节，
// 只需要调用 get() / post() / put() / del()

// 例子：页面想获取帖子列表
// 只需要写一行：
const res = await get('/posts', { page: 1, size: 20 })
// request.js 会：
// 1. 拼出完整URL: http://localhost:9090/api/v1/posts?page=1&size=20
// 2. 自动附上登录 token
// 3. 处理网络错误（弹 toast 提示）
// 4. 处理 401（自动跳登录页）
// 5. 返回解析好的 JSON 数据
```

### 4.3 关键前端文件逐个解释

| 文件 | 作用（大白话） |
|------|---------------|
| `main.js` | 程序入口，初始化 Pinia，启动 App |
| `App.vue` | 最外层框架，所有页面的"外壳" |
| `pages.json` | URL → 页面的映射表 + 底部 Tab 配置 |
| `stores/user.js` | 用户信息仓库：登录/注册/退出/获取个人信息 |
| `stores/post.js` | 帖子数据仓库：列表/详情/创建/删除/点赞/收藏 |
| `utils/request.js` | 网络请求封装：get/post/put/del + 自动Token + 错误处理 |
| `utils/auth.js` | 登录/退出工具：存取 token 到本地存储 |
| `utils/date.js` | 日期工具：格式化时间、"3分钟前"这种相对时间 |
| `utils/storage.js` | 本地存储封装：就像浏览器的"抽屉"，可以存东西下次用 |
| `utils/validate.js` | 表单验证：检查手机号格式、长度限制等 |
| `api/user.js` | 用户相关 API：注册/登录/获取信息/修改资料 |
| `api/post.js` | 帖子相关 API：列表/详情/创建/删除/点赞/收藏 |
| `api/upload.js` | 图片上传 API |
| `api/game.js` | 游戏分类 API |
| `api/news.js` | 资讯 API |
| `api/guide.js` | 攻略 API |
| `api/comment.js` | 评论 API |
| `api/report.js` | 举报 API |
| `styles/variables.scss` | 设计变量：颜色/字体/间距/圆角全在这里统一定 |
| `styles/mixins.scss` | 样式混入：可复用的 CSS "配方" |

---

## 5. 后端是什么（用户看不到的部分）

后端用 `Spring Boot`（一个 Java Web 框架）写。它就像一个**总服务台**：

- 前端（客户）说："我要看第 1 页的帖子"
- 后端（前台）说："好的请稍等"，然后去数据库（仓库）取数据，包装好，返回给前端

### 5.1 后端分层架构（为什么要分这么多层）

```
Controller（前台接待）
   ↓  收请求、验身份、转发
Service 接口（任务清单）
   ↓  定义"做什么"
ServiceImpl（执行者）
   ↓  具体"怎么做"，加事务管理
Mapper（仓库管理员）
   ↓  从数据库取/存数据
Entity（货物的形状）
   ↓  每张表对应一个类
```

**为什么分这么多层？**

**类比开餐馆**：
- 老板说"我要一份红烧肉" → Controller
- 厨师长看菜单："红烧肉，步骤是..." → Service（接口）
- 厨师动手做 → ServiceImpl（实现）
- 帮厨去冰柜拿五花肉 → Mapper
- 肉本身 → Entity（数据）

如果有一天你想换冰柜品牌（换数据库），只改 Mapper 就行，厨师长的菜单不用动。**各管各的，互不影响**。

### 5.2 关键后端文件逐个解释

#### Controller（控制器）

| 文件 | 作用（大白话） |
|------|---------------|
| `AuthController.java` | 处理"登录、注册、退出、发验证码"这些请求 |
| `UserController.java` | 处理"查个人信息、改资料、查某人主页"请求 |
| `PostController.java` | 处理帖子的增删查改+点赞收藏请求 |
| `CommentController.java` | 处理评论相关请求 |
| `GuideController.java` | 处理攻略相关请求 |
| `NewsController.java` | 处理资讯相关请求 |
| `SocialController.java` | 处理关注/取关/私信请求 |
| `NotificationController.java` | 处理通知请求 |
| `GameController.java` | 返回游戏分类列表和标签列表 |
| `ReportController.java` | 接收举报 |
| `UploadController.java` | 接收上传的图片文件 |

#### Service（业务逻辑层）

| 文件 | 作用 |
|------|------|
| `AuthServiceImpl.java` | 注册（检查用户名重复+加密密码）、登录（验证密码+生成token）、发短信验证码 |
| `UserServiceImpl.java` | 查用户、创建用户（第一次登录自动创建）、修改资料 |
| `PostServiceImpl.java` | **最大最复杂的文件（330行）**：帖子列表（含按分类/关键字/排序/分页）、帖子详情（含作者/分类/图片/标签/点赞收藏状态）、创建帖子（含标签处理+图片关联+分类计数+用户发帖数）、编辑/删除帖子、点赞/收藏切换（原子计数） |
| `CommentServiceImpl.java` | 评论列表（含楼中楼嵌套+子回复+点赞状态）、创建评论、删除评论、评论点赞 |
| `GuideServiceImpl.java` | 攻略CRUD + 收藏 |
| `NewsServiceImpl.java` | 资讯列表/详情 |
| `SocialServiceImpl.java` | 关注（检查自关注+重复关注+更新双向计数）、取关、粉丝列表/关注列表（含是否已关注状态）、私信（发送+聊天记录+会话聚合+未读计数+标记已读） |
| `FileUploadService.java` | 接收上传图片→检查格式/大小→生成唯一文件名→保存到 `uploads/` 目录 |

#### Mapper（数据库操作）

| 文件 | 作用 |
|------|------|
| `UserMapper.java` | 操作用户表 + 原子更新发帖数/粉丝数/关注数 |
| `PostMapper.java` | 操作帖子表 + 原子更新浏览量/点赞数/收藏数/评论数（6 个自定义方法） |
| `CommentMapper.java` | 操作评论表 + 原子更新点赞数/回复数 |
| 其他 15 个 Mapper | 各管各的表 |

#### Entity（数据实体）

每个 `Entity` 就是一个**数据库表的"模板"**。比如 `User.java` 定义了用户表有哪些列（id、用户名、密码、昵称等），MyBatis-Plus 会根据这个模板自动生成 SQL 语句。

#### DTO（数据传输对象）

```java
// 比如前端要创建帖子，会发一个 JSON：
{"title": "我的帖子", "content": "<p>内容</p>", "categoryId": 2}

// 后端需要一个"信封"来接收这个 JSON
// PostCreateReq.java 就是这个信封的"规格"
// 它规定：title 必填（@NotBlank）、最多 50 字（@Size(max=50)）

// 后端返回数据时也需要信封
// PostDetailResp.java 规定返回给前端的帖子详情包含哪些字段
```

#### Common（通用工具）

| 文件 | 作用 |
|------|------|
| `Result.java` | 统一响应格式：`{code:200, message:"成功", data:数据}` |
| `PageReq.java` | 分页请求基类：`{page:1, size:20}` |
| `BusinessException.java` | 抛异常类：出错了就"举手" |
| `GlobalExceptionHandler.java` | 全局异常"灭火器"：哪里的代码出错它都自动兜底，返回友好提示 |

---

## 6. 数据库是什么（数据存在哪）

数据库就是一堆**Excel 表格**，但是比 Excel 快几千倍。

### 6.1 本项目有哪些表（18 张）

| 表名 | 存什么 | 类比 |
|------|--------|------|
| `users` | 用户账号信息 | 通讯录 |
| `game_categories` | 游戏分类 | 商品分类标签 |
| `posts` | 帖子内容 | 朋友圈动态 |
| `post_images` | 帖子配图 | 相册 |
| `post_tags` | 帖子标签 | 话题标签 |
| `tags` | 标签库 | 标签列表 |
| `post_likes` | 谁点赞了哪个帖子 | 点赞记录本 |
| `post_favorites` | 谁收藏了哪个帖子 | 收藏记录本 |
| `comments` | 评论内容 | 留言板 |
| `comment_likes` | 谁点赞了哪个评论 | 评论点赞记录 |
| `guides` | 攻略 | 教程目录 |
| `guide_sections` | 攻略分步内容 | 教程章节 |
| `guide_favorites` | 攻略收藏 | 攻略收藏记录 |
| `news` | 资讯文章 | 新闻列表 |
| `user_follows` | 关注关系 | 关注列表 |
| `messages` | 私信消息 | 聊天记录 |
| `notifications` | 系统通知 | 通知中心 |
| `reports` | 举报记录 | 举报箱 |

### 6.2 为什么计数器不用"读+1+存"的方式

传统写法：
```java
// 坏写法（并发失败）
int count = post.getLikeCount();  // 读到 100
count = count + 1;                 // 算成 101
post.setLikeCount(count);          // 存回去 101
```

问题：如果两个人同时点赞（都读到 100，都算成 101，都存 101），结果是 101，但应该是 102。

本项目写法：
```sql
UPDATE posts SET like_count = like_count + 1 WHERE id = 3
-- 数据库自己加，保证不出错
```

这是面试可以重点讲的**亮点**。

---

## 7. 一个功能怎么从头跑到尾

以"用户点开一个帖子"为例，追踪整个过程：

```
Step 1: 用户在手机上点帖子
         ↓

Step 2: 前端 PostCard.vue 触发 click 事件
         → 调用 uni.navigateTo({ url: '/pages/post/detail?id=3' })
         ↓

Step 3: 打开 post/detail.vue 页面
         → onLoad 函数执行
         → 调用 postStore.fetchPostDetail(3)
         ↓

Step 4: Store 调用 API 层
         → postApi.getDetail(3)
         → 实际执行: get('/posts/3')
         ↓

Step 5: request.js 发出 HTTP 请求（网络请求）
         → GET http://localhost:9090/api/v1/posts/3
         → 自动在请求头加上 Authorization token
         ↓

(网络传输)

Step 6: 后端 PostController.java 收到请求
         → @GetMapping("/{id}") 方法被调用
         → 调用 postService.detail(3, userId)
         ↓

Step 7: PostServiceImpl.java 执行 detail 方法
         → ① postMapper.selectById(3)        -- 查帖子
         → ② postMapper.incrementViewCount(3) -- 浏览数+1（原子SQL）
         → ③ userMapper.selectById(authorId) -- 查作者
         → ④ gameCategoryMapper.selectById(catId) -- 查分类
         → ⑤ postImageMapper.selectList(...) -- 查图片
         → ⑥ postLikeMapper.exists(...)       -- 判断当前用户是否已点赞
         → ⑦ 组装 PostDetailResp 对象
         ↓

Step 8: Controller 将结果包装成 Result.ok(resp)
         → 返回 JSON: {code:200, data:{...}, message:"success"}
         ↓

(网络传输)

Step 9: request.js 收到响应
         → 解析 JSON
         → 检查 code === 200 → 成功，resolve(data)
         → 如果是 401 → 自动跳登录
         ↓

Step 10: 前端 post/detail.vue 收到数据
         → article.value = 返回的帖子数据
         → Vue 自动把数据显示在页面上
         ↓

Step 11: 用户看到完整的帖子内容！
```

**整个流程的参与者**：前端页面 → Store → API 模块 → request.js → 网络 → Controller → Service → Mapper → 数据库 → 原路返回。

---

## 8. 每个文件的简单解释（快速查阅）

### 前端文件速查

| 文件路径 | 一句话作用 |
|----------|-----------|
| `package.json` | 前端用的零件清单，`npm install` 按它下载 |
| `vite.config.js` | Vite 编译器配置 |
| `src/main.js` | 前端程序的入口，初始化 Pinia |
| `src/App.vue` | 所有页面的外壳 |
| `src/pages.json` | 页面地址簿 + 底部按钮配置 |
| `src/pages/index/index.vue` | 首页：信息流 + 分类Tab + 排序栏 |
| `src/pages/post/list.vue` | 帖子广场：帖子列表 |
| `src/pages/post/detail.vue` | 帖子详情：全内容 + 关注按钮 + 评论 + 固定底部评论框 |
| `src/pages/post/publish.vue` | 发布帖子：标题 + 文字 + 图片上传 + 游戏分类 |
| `src/pages/user/login.vue` | 登录页：账号密码/注册/快速体验 |
| `src/pages/user/profile.vue` | "我的"页：头像/数据/菜单（Tab页） |
| `src/pages/user/view.vue` | 他人主页：关注/私信按钮（可跳转传参） |
| `src/pages/user/follow-list.vue` | 粉丝/关注列表：Tab切换+关注/取关 |
| `src/pages/user/settings.vue` | 设置页：换头像/改昵称/改简介 |
| `src/pages/message/list.vue` | 消息列表：会话列表+未读红点 |
| `src/pages/message/chat.vue` | 聊天页：气泡消息+固定底部输入框 |
| `src/pages/news/list.vue` | 资讯列表：分类筛选 |
| `src/pages/search/index.vue` | 搜索页:关键词搜索帖子 |
| `src/pages/report/index.vue` | 举报页：选择类型+描述+提交 |
| `src/components/post/PostCard.vue` | 帖子卡片组件（列表里每个帖子就是这个） |
| `src/components/post/PostActions.vue` | 帖子操作栏（点赞/评论/收藏图标） |
| `src/components/comment/CommentList.vue` | 评论列表组件 |
| `src/components/comment/CommentInput.vue` | 评论输入框组件 |
| `src/components/common/Loading.vue` | 加载动画（转圈圈） |
| `src/components/common/Empty.vue` | 空状态（没数据时显示） |
| `src/components/feed/CategoryTabs.vue` | 游戏分类横向滑动Tab |
| `src/components/feed/SortBar.vue` | 最新/最热切换栏 |
| `src/components/upload/ImageUpload.vue` | 图片上传组件（多图+删除） |
| `src/stores/user.js` | 用户数据仓库：登录/注册/资料 |
| `src/stores/post.js` | 帖子数据仓库：列表/详情/创建/点赞 |
| `src/utils/request.js` | 网络请求封装（最核心的工具文件） |
| `src/api/post.js` | 帖子相关的前端 API（给 request.js 传地址） |

### 后端文件速查

| 文件路径 | 一句话作用 |
|----------|-----------|
| `pom.xml` | 后端零件清单，Maven 按它下载 |
| `WoApplication.java` | 程序的入口 main 方法在这 |
| `config/SaTokenConfig.java` | 登录权限配置（游客可读/登录才可写） |
| `config/CorsConfig.java` | 允许前端跨域访问后端 |
| `config/MyBatisPlusConfig.java` | 数据库连接 + 分页插件配置 |
| `config/MetaObjectHandlerConfig.java` | 插入/更新数据时自动填时间和更新时间 |
| `config/WebMvcConfig.java` | 上传的图片可以通过 URL 访问 |
| `controller/AuthController.java` | 登录/注册/退出/验证码的"前台" |
| `controller/PostController.java` | 帖子增删查改+点赞+收藏的"前台" |
| `controller/CommentController.java` | 评论+点赞的"前台" |
| `controller/SocialController.java` | 关注/粉丝/私信/会话的"前台" |
| `service/impl/PostServiceImpl.java` | **核心文件**：帖子所有业务逻辑在这 |
| `service/impl/CommentServiceImpl.java` | 评论所有业务逻辑（含楼中楼嵌套） |
| `service/impl/SocialServiceImpl.java` | 关注/私信所有业务逻辑 |
| `service/impl/AuthServiceImpl.java` | 注册(BCrypt加密)+登录(Sa-Token生成token) |
| `service/impl/UserServiceImpl.java` | 用户查询/创建/修改资料 |
| `service/impl/FileUploadService.java` | 图片上传到服务器磁盘 |
| `mapper/PostMapper.java` | 操作帖子表格 + 6 个原子计数 SQL |
| `mapper/UserMapper.java` | 操作用户表格 + 6 个原子计数 SQL |
| `mapper/CommentMapper.java` | 操作评论表格 + 3 个原子计数 SQL |
| `common/Result.java` | 统一返回格式：{code, message, data} |
| `common/GlobalExceptionHandler.java` | 全局错误"灭火器" |
| `db/001_create_base.sql` | 建用户表 + 游戏分类表 + 初始数据 |
| `db/002_create_posts.sql` | 建帖子/图片/标签/点赞/收藏表 |
| `db/003_create_comments.sql` | 建评论/评论点赞表 |
| `db/004_create_guides.sql` | 建攻略/章节/收藏表 |
| `db/005_create_news.sql` | 建资讯表 |
| `db/006_create_reports.sql` | 建举报/通知表 |

### 测试文件

| 文件 | 测试什么 | 用例数 |
|------|---------|--------|
| `UserServiceTest.java` | 查用户/创建用户/修改资料/异常情况 | 6 |
| `PostServiceTest.java` | 创建帖子/列表/分类筛选/搜索/点赞收藏/删除权限 | 9 |
| `CommentServiceTest.java` | 创建评论/楼中楼回复/点赞/软删除 | 4 |
| `SocialServiceTest.java` | 关注/取关/粉丝列表/私信/会话/防自关注 | 7 |

---

## 9. 怎么启动项目

### 第一步：确保所有软件已安装

```bash
java -version     # JDK 17
mvn --version     # Maven
mysql -u root -proot123 -e "SELECT 1"   # MySQL
node --version    # Node.js
```

### 第二步：创建数据库

```bash
mysql -u root -proot123 -e "CREATE DATABASE IF NOT EXISTS wo_dev CHARACTER SET utf8mb4"
```

如果 root 密码不是 `root123`，改 `server/src/main/resources/application-dev.yml` 里 `password` 那一行。

### 第三步：启动后端

```bash
cd wo/server

# 第一次或改了代码后：
mvn clean compile spring-boot:run

# 以后直接：
mvn spring-boot:run
```

看到 `Started WoApplication in X seconds` 说明后端启动了。

### 第四步：启动前端

```bash
cd wo

# 第一次需要安装依赖：
npm install

# 启动：
npm run dev:h5
```

看到 `Local: http://localhost:5173` 说明前端启动了。

### 第五步：打开浏览器

访问 `http://localhost:5173`，开始使用。

### 运行测试

```bash
cd wo/server
mvn test
```

看到 `Tests run: 26, Failures: 0` 说明所有测试通过。

### Docker 一键部署

如果你装了 Docker：

```bash
cd wo
npm run build:h5          # 先编译前端

cd server
docker-compose up -d       # 一键启动 MySQL+Redis+MinIO+后端+Nginx
```

访问 `http://localhost` 即可。

---

## 总结

这个项目就像一个三层楼的建筑：

| 楼层 | 职责 | 技术 |
|------|------|------|
| 三楼（展示层） | 用户看到的界面、点击的按钮 | Vue 3 + uni-app |
| 二楼（业务层） | 处理请求、计算数据、执行逻辑 | Spring Boot |
| 一楼（数据层） | 存储所有信息、提供查询 | MySQL |

三层之间通过"API"（接口）通信——前端不再直接访问数据库，必须通过后端中转。这样做的好处是**安全**（前端拿不到数据库密码）和**灵活**（换前端不改后端，换数据库不改业务逻辑）。

如果你想深入了解任何部分，从 `README.md` 开始看，然后对照这份文档找到对应的文件去看代码。
