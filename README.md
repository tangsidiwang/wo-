# WO — 游戏交流社区

基于 **Spring Boot 3 + uni-app Vue 3** 的跨平台游戏内容社区。一套代码发布到 H5、微信小程序、iOS/Android App。

---

## 项目概览

| 维度 | 数据 |
|------|------|
| 后端 | Java 17 / Spring Boot 3.2 / MyBatis-Plus 3.5 / MySQL 8.0 / Sa-Token |
| 前端 | uni-app Vue 3 + Vite + Pinia + SCSS |
| 平台 | H5 / 微信小程序 / App（一套代码多端编译） |
| 后端文件 | 96 个 Java 类 |
| 前端文件 | 22 个页面 + 21 个组件 |
| API 端点 | 45 个 RESTful 接口 |
| 数据库表 | 18 张 |
| 单元测试 | 26 个，全部通过 |

## 功能模块

### 用户系统
- 账号密码注册/登录、手机验证码登录、微信一键登录
- 个人主页（头像/昵称/简介/帖子数/获赞数/关注粉丝数）
- 头像上传、资料编辑
- **关注/粉丝**：社交图谱双向计数，粉丝列表/关注列表

### 内容系统
- **帖子**：富文本发布（文本 + 图片混排）、列表（最新/最热排序）、分类筛选、关键词搜索
- **攻略**：分章节结构化编辑、游戏分类浏览、收藏
- **资讯**：游戏更新/电竞赛事/行业动态分类

### 社交互动
- **点赞/收藏**：帖子、攻略、评论均可点赞
- **评论**：一级评论 + 楼中楼回复（二级），评论点赞
- **即时私信**：会话列表 + 聊天记录 + 未读红点 + 自动标记已读
- **通知系统**：通知列表、未读计数、标记已读/全部已读

### 审核与安全
- 举报系统（帖子/评论/攻略）
- 游客权限分级（读操作开放、写操作需登录）
- 401 自动跳转登录

## 快速启动

### 开发环境

```bash
# 1. 确保 MySQL 8.0 运行中，创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS wo_dev DEFAULT CHARSET utf8mb4"

# 2. 启动后端
cd server
mvn spring-boot:run

# 3. 启动前端（新终端）
npm run dev:h5
```

打开 `http://localhost:5173` 即可使用。

### 运行测试

```bash
cd server
mvn test
```

### Docker 一键部署

```bash
cd server
docker-compose up -d
```

自动启动 MySQL + Redis + MinIO + Spring Boot + Nginx。访问 `http://localhost`。

## 项目结构

```
wo/
├── src/                    # uni-app 前端
│   ├── pages/              # 22 个页面
│   │   ├── index/          # 首页信息流
│   │   ├── post/           # 帖子（列表/详情/发布）
│   │   ├── guide/          # 攻略（列表/详情/发布）
│   │   ├── news/           # 资讯（列表/详情）
│   │   ├── message/        # 消息（会话列表/聊天）
│   │   ├── user/           # 个人主页/设置/关注粉丝/登录
│   │   ├── search/         # 搜索
│   │   └── report/         # 举报
│   ├── components/         # 21 个可复用组件
│   ├── stores/             # Pinia 状态管理
│   ├── api/                # API 请求层
│   ├── utils/              # 工具函数
│   └── styles/             # 设计系统
├── server/                 # Spring Boot 后端
│   ├── src/main/java/com/wo/
│   │   ├── controller/     # 9 个 REST Controller
│   │   ├── service/impl/   # 10 个 Service 实现
│   │   ├── mapper/         # 18 个 MyBatis Mapper（含原子 SQL）
│   │   ├── entity/         # 17 个实体类
│   │   ├── dto/            # 请求/响应 DTO
│   │   └── config/         # 配置类
│   ├── src/main/resources/db/  # 6 个 SQL 迁移脚本
│   ├── src/test/           # 4 个测试类 / 26 个测试用例
│   ├── docker-compose.yml  # 一键部署（MySQL+Redis+MinIO+后端+Nginx）
│   ├── nginx.conf          # Nginx 反向代理
│   └── Dockerfile
├── package.json
└── vite.config.js
```

## 技术亮点

1. **原子计数** — 所有点赞/收藏/浏览/关注数使用 `UPDATE SET count = count + 1` SQL 原子操作，高并发安全
2. **批量查询优化** — 帖子列表单接口一次性组装作者信息、分类名、图片缩略图、点赞收藏状态，避免 N+1 查询
3. **社交图谱** — `user_follows` 双向关系 + 计数同步更新 + 粉丝/关注列表联动
4. **楼中楼评论** — 一级评论嵌套子回复，`parent_id` + `reply_count` 计数
5. **私信系统** — 会话聚合 + 未读计数 + 自动已读标记 + 5 秒轮询刷新
6. **分层架构** — Controller → Service(接口) → ServiceImpl → Mapper → Entity，接口与实现分离
7. **游客模式** — 读接口无需登录，写接口 `StpUtil.checkLogin()` 逐方法校验
8. **统一设计系统** — SCSS 变量/Mixin/全局样式，渐变色卡片式 UI，适配安全区域

## API 完整清单

### 认证 `/api/v1/auth`
| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| POST | `/auth/register` | 无需 | 账号密码注册 |
| POST | `/auth/login` | 无需 | 密码登录 / 短信登录 |
| POST | `/auth/wechat-login` | 无需 | 微信 code 登录 |
| POST | `/auth/dev-login` | 无需 | 昵称快速登录（开发用） |
| POST | `/auth/send-sms` | 无需 | 发送短信验证码 |
| POST | `/auth/logout` | 需 | 退出登录 |

### 帖子 `/api/v1/posts`
| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/posts` | 无需 | 列表（分页/排序/分类/关键字搜索） |
| GET | `/posts/{id}` | 无需 | 详情（自动加浏览数，含点赞收藏状态） |
| POST | `/posts` | 需 | 发布帖子 |
| PUT | `/posts/{id}` | 需 | 编辑帖子 |
| DELETE | `/posts/{id}` | 需 | 删除（软删除） |
| POST | `/posts/{id}/like` | 需 | 点赞 |
| DELETE | `/posts/{id}/like` | 需 | 取消点赞 |
| POST | `/posts/{id}/favorite` | 需 | 收藏 |
| DELETE | `/posts/{id}/favorite` | 需 | 取消收藏 |

### 评论 `/api/v1`
| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/posts/{id}/comments` | 无需 | 评论列表（含楼中楼嵌套） |
| POST | `/posts/{id}/comments` | 需 | 发表评论（支持 parentId） |
| DELETE | `/comments/{id}` | 需 | 删除评论（软删除） |
| POST | `/comments/{id}/like` | 需 | 评论点赞 |
| DELETE | `/comments/{id}/like` | 需 | 取消评论点赞 |

### 攻略 `/api/v1/guides`
| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/guides` | 无需 | 列表 |
| GET | `/guides/{id}` | 无需 | 详情（含章节） |
| POST | `/guides` | 需 | 发布攻略 |
| DELETE | `/guides/{id}` | 需 | 删除 |
| POST | `/guides/{id}/favorite` | 需 | 收藏 |
| DELETE | `/guides/{id}/favorite` | 需 | 取消收藏 |

### 用户 `/api/v1/users`
| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/users/me` | 需 | 我的信息 |
| GET | `/users/me/posts` | 需 | 我的帖子 |
| GET | `/users/me/favorites` | 需 | 我的收藏 |
| PUT | `/users/me` | 需 | 修改资料 |
| GET | `/users/{id}` | 无需 | 用户公开主页 |
| GET | `/users/{id}/posts` | 无需 | 用户帖子列表 |
| GET | `/users/{id}/followers` | 无需 | 粉丝列表 |
| GET | `/users/{id}/following` | 无需 | 关注列表 |

### 社交 `/api/v1`
| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| POST | `/follow/{id}` | 需 | 关注 |
| DELETE | `/follow/{id}` | 需 | 取消关注 |
| POST | `/messages` | 需 | 发送私信 |
| GET | `/messages/conversations` | 需 | 会话列表 |
| GET | `/messages/chat/{peerId}` | 需 | 聊天记录 |

### 通知 `/api/v1`
| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/notifications` | 需 | 通知列表 |
| GET | `/notifications/unread-count` | 需 | 未读计数 |
| PUT | `/notifications/{id}/read` | 需 | 标记已读 |
| PUT | `/notifications/read-all` | 需 | 全部已读 |

### 其他 `/api/v1`
| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/games` | 无需 | 游戏分类 |
| GET | `/tags` | 无需 | 热门标签 |
| GET | `/news` | 无需 | 资讯列表 |
| GET | `/news/{id}` | 无需 | 资讯详情 |
| POST | `/upload/image` | 需 | 上传图片 |
| POST | `/reports` | 需 | 提交举报 |

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "meta": { "page": 1, "size": 20, "total": 100 },
  "timestamp": 1717584000
}
```

## 数据库设计

| 表名 | 说明 | 核心索引 |
|------|------|---------|
| `users` | 用户 | username(UNIQUE), openid |
| `game_categories` | 游戏分类 | sort_order |
| `posts` | 帖子 | category_id+status+created_at, user_id, FULLTEXT(content_text) |
| `post_images` | 帖子图片 | post_id |
| `post_tags` | 帖子标签 | post_id+tag_id(UNIQUE) |
| `tags` | 标签 | name(UNIQUE) |
| `post_likes` | 点赞 | user_id+post_id(UNIQUE) |
| `post_favorites` | 收藏 | user_id+post_id(UNIQUE) |
| `comments` | 评论 | post_id+parent_id+created_at |
| `comment_likes` | 评论点赞 | user_id+comment_id(UNIQUE) |
| `guides` | 攻略 | game_id+status+created_at, FULLTEXT(title,summary) |
| `guide_sections` | 攻略章节 | guide_id+sort_order |
| `guide_favorites` | 攻略收藏 | user_id+guide_id(UNIQUE) |
| `news` | 资讯 | category+published_at |
| `user_follows` | 关注 | follower_id+followee_id(UNIQUE) |
| `messages` | 私信 | from_uid+to_uid+created_at, to_uid+is_read |
| `notifications` | 通知 | user_id+is_read+created_at |
| `reports` | 举报 | target_type+target_id, status |

## 测试

```bash
cd server && mvn test
```

```
Tests run: 26, Failures: 0, Errors: 0, Skipped: 0
├── UserServiceTest       (6)  查询/创建/更新/异常/幂等
├── PostServiceTest       (9)  CRUD/分类过滤/搜索/点赞收藏/删除权限
├── CommentServiceTest    (4)  评论/楼中楼回复/点赞/软删除
└── SocialServiceTest     (7)  关注/取关/粉丝列表/私信/会话/防重复
```
