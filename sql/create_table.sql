# 数据库初始化

-- 创建库
create database if not exists naerso;

-- 切换库
use naerso;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;


INSERT INTO post (title, content, tags, thumbNum, favourNum, userId) VALUES
('如何学习编程', '学习编程需要掌握基础语法，多做项目练习。', '["编程", "学习"]', 10, 5, 1),
('健康饮食指南', '均衡饮食，多吃蔬菜水果，少吃油腻食物。', '["健康", "饮食"]', 20, 15, 2),
('旅行必备物品清单', '旅行时带上身份证、充电宝、常用药品等。', '["旅行", "清单"]', 30, 10, 3),
('电影推荐：2023年最佳影片', '推荐几部2023年值得一看的电影。', '["电影", "推荐"]', 50, 25, 4),
('如何提高工作效率', '合理安排时间，使用工具提高工作效率。', '["工作", "效率"]', 40, 20, 5),
('健身计划：从零开始', '每周三次力量训练，两次有氧运动。', '["健身", "计划"]', 60, 30, 6),
('如何选择适合自己的笔记本电脑', '根据需求选择配置，注意性价比。', '["电脑", "选购"]', 25, 12, 7),
('摄影技巧：如何拍出好照片', '掌握光线、构图和后期处理技巧。', '["摄影", "技巧"]', 70, 35, 8),
('如何在家自学英语', '利用在线资源，坚持每天学习。', '["英语", "学习"]', 15, 8, 9),
('宠物护理指南', '定期给宠物洗澡、剪指甲，注意饮食健康。', '["宠物", "护理"]', 45, 22, 10);
-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';


INSERT INTO user (
    userAccount, userPassword, unionId, mpOpenId,
    userName, userAvatar, userProfile, userRole, isDelete
) VALUES
      ('john_doe', 'e10adc3949ba59abbe56e057f20f883e', 'wx123456789', 'mp_0001',
       'John', 'https://example.com/avatar1.jpg', '热爱技术的开发者', 'user', 0),

      ('admin_alice', 'd8578edf8458ce06fbc5bb76a58c5ca4', NULL, 'mp_0002',
       'Alice', 'https://example.com/avatar2.png', '系统管理员', 'admin', 0),

      ('banned_user', '5f4dcc3b5aa765d61d8327deb882cf99', 'wx987654321', NULL,
       'Banned', 'https://example.com/avatar3.jpg', '已被封禁用户', 'ban', 0),

      ('tech_guy', '96e79218965eb72c92a549dd5a330112', NULL, NULL,
       'TechG', NULL, NULL, 'user', 1),

      ('lisa_smith', 'e99a18c428cb38d5f260853678922e03', 'wx654321987', 'mp_0003',
       'Lisa', 'https://example.com/avatar4.jpg', '前端开发工程师', 'user', 0),

      ('demo_user', '5f4dcc3b5aa765d61d8327deb882cf99', NULL, 'mp_0004',
       'Demo', 'https://example.com/avatar5.png', '演示账号', 'user', 0);
