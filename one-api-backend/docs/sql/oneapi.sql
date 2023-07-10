create table oneapi.interface_info
(
    id             bigint auto_increment primary key comment '主键',
    name           varchar(256)                       not null comment '接口名称',
    description    varchar(256)                       null comment '描述',
    url            varchar(512)                       not null comment '接口地址',
    requestHeader  text                               null comment '请求头',
    responseHeader text                               null comment '响应头',
    status         int      default 0                 not null comment '接口状态（0-关闭，1-开启）',
    method         varchar(256)                       not null comment '请求类型',
    userId         bigint                             not null comment '创建人',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)',
    requestParams  text                               null comment '请求参数',
    uri            varchar(50)                        not null
)
    comment '接口信息' charset = utf8mb4;

create table oneapi.user
(
    id           bigint auto_increment primary key comment '主键',
    userName     varchar(256) default '新用户'          null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    accessKey    varchar(512)                           not null comment 'accessKey',
    secretKey    varchar(512)                           not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    signature    varchar(256)                           null comment '签名',
    constraint uni_userAccount
        unique (userAccount)
)
    comment '用户' charset = utf8mb4;

create table oneapi.user_interface_info
(
    id              bigint auto_increment primary key comment '主键',
    userId          bigint                             not null comment '调用用户ID',
    interfaceInfoId bigint                             not null comment '接口信息ID',
    totalNum        int      default 0                 not null comment '总调用次数',
    leftNum         int      default 0                 not null comment '剩余调用次数',
    status          int      default 0                 not null comment '0-正常，1-禁用',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
)
    comment '用户调用接口关系表' charset = utf8mb4;


insert into oneapi.interface_info (id, name, description, url, requestHeader, responseHeader, status, method, userId,
                                   createTime, updateTime, isDelete, requestParams, uri)
values (1, '随机头像', '获取随机头像', '/api/avatar/avatarUrl', '{
  "Content-Type": "application/json"
}', '{
  "Content-Type": "application/json"
}', 1, 'POST', 1, '2022-12-04 00:36:51', '2023-05-09 01:43:33', 0, '{
  "暂无参数":"暂无参数"
}', 'http://localhost:8123/api/avatar/avatarUrl'),
       (2, '获取城市天气接口', '天气查询', '/api/weather/weatherInfo', '{
  "Content-Type": "application/json"
}', '{
  "Content-Type": "application/json"
}', 1, 'POST', 1, '2022-11-28 17:41:15', '2023-05-09 00:41:50', 0, '{
  "city": "城市名/区域名 例如：鄂城区",
  "extensions": "base/all 可选 base:返回实况天气 all:返回预报天气"
}', 'http://localhost:8123/api/weather/weatherInfo'),
       (3, '百度热搜接口', '百度热点数据', '/api/baidu/baiduInfo', '{
  "Content-Type": "application/json"
}', '{
  "Content-Type": "application/json"
}', 1, 'POST', 1, '2022-11-28 17:41:15', '2023-05-02 21:15:36', 0, '{
  "size": "int 非必传，默认10条"
}', 'http://localhost:8123/api/baidu/baiduInfo');

insert into oneapi.user (id, userName, userAccount, userAvatar, gender, userRole, userPassword, accessKey, secretKey,
                         createTime, updateTime, isDelete, signature)
values (4, '心跳', 'oneapi', 'http://yuque.heshuoshi.top/html5/QQ%E5%9B%BE%E7%89%8720230217132131.jpg', null, 'admin',
        '1ed88763c7810ef2b1e68c0c2c5042e7', 'a84b6f645d6b5bc6b66a253545f19eee', '36484fddc396bf8e4cc1769339532196',
        '2023-02-21 15:54:22', '2023-05-06 19:43:01', 0, '泰裤辣！！！'),
       (24, '新用户', 'admin', 'http://yuque.heshuoshi.top/html5/IMG_1364(20230426-213630).JPG', null, 'user',
        'dabefb0d00e9150f7df3e99ad6f1e45b', '7947c27ca461385c52d38c1a696ec196', '2c9bab9f7506e43b58ed68b11d607341',
        '2023-05-09 00:17:25', '2023-05-09 00:17:25', 0, null),
       (25, '新用户', 'lishihao', 'https://tvax3.sinaimg.cn/large/9bd9b167ly1g1p9pj659fj20b40b4dg7.jpg', null, 'user',
        'dabefb0d00e9150f7df3e99ad6f1e45b', '43fe7af8ac5644836199122bfcd41bd4', '56a6ed26feee7857292e30008e25dc81',
        '2023-05-09 01:42:53', '2023-05-09 01:42:53', 0, null),
       (26, '新用户', 'xuyasen', 'http://webimg.maibaapp.com/content/img/avatars/20161512/14/15/23737.jpg', null,
        'user', 'dabefb0d00e9150f7df3e99ad6f1e45b', 'c992d2139ffb9a6cf521c07e58319a37',
        '847ea378c15a67251005657350bf5a97', '2023-05-09 11:32:14', '2023-05-09 11:32:14', 0, null),
       (27, '新用户', 'zhouge', 'http://webimg.maibaapp.com/content/img/avatars/20164025/18/40/53127.jpg', null, 'user',
        'df5750b0af70647a5e6dae9cd09a6ba1', 'f6f3aaa47031c661f767b7e406da49ab', 'a1d998a5314f85eb20f69194ef1349ad',
        '2023-05-09 11:58:22', '2023-05-09 11:58:22', 0, null),
       (28, '新用户', 'jinbao', 'http://webimg.maibaapp.com/content/img/avatars/20174630/12/46/48472.jpg', null, 'user',
        'dabefb0d00e9150f7df3e99ad6f1e45b', '23f63afe1f64c27bfb9fb9308d636c07', '6bbcae09ddc10a9a8a257928aa23d193',
        '2023-05-10 00:18:04', '2023-05-10 00:18:04', 0, null),
       (29, '新用户', 'jinbao1', 'http://webimg.maibaapp.com/content/img/avatars/20162220/21/22/48880.jpg', null,
        'user', 'dabefb0d00e9150f7df3e99ad6f1e45b', 'def4f79096017a22185c34f9e7c9cb07',
        'f290feb8f4b3b0fe74f6627b609cdd9b', '2023-05-10 00:18:25', '2023-05-10 00:18:25', 0, null),
       (30, '新用户', 'jinbao2', 'http://webimg.maibaapp.com/content/img/avatars/20161017/17/10/36591.png', null,
        'user', 'dabefb0d00e9150f7df3e99ad6f1e45b', 'c7a28af87ba45d504c1cf97fafdf7e30',
        '20c00ed9a93f0eedf589c5dd06cf584f', '2023-05-10 23:07:22', '2023-05-10 23:07:22', 0, null),
       (31, '新用户', 'jinbao3', 'http://webimg.maibaapp.com/content/img/avatars/20174525/16/45/31247.jpg', null,
        'user', 'dabefb0d00e9150f7df3e99ad6f1e45b', 'f94fb83f9f3fee503c9b44f0ab97d7f9',
        'a8b9a504e6081ca61fd639775f4b1765', '2023-05-11 19:52:06', '2023-05-11 19:52:06', 0, null);

insert into oneapi.user_interface_info (id, userId, interfaceInfoId, totalNum, leftNum, status, createTime,
                                        updateTime, isDelete)
values (1, 4, 1, 174, 19861, 0, '2022-12-09 14:29:15', '2023-05-20 15:23:22', 0),
       (2, 3, 2, 13, 5, 0, '2023-04-27 09:18:49', '2023-04-27 09:18:49', 0),
       (4, 2, 3, 12, 3, 0, '2023-04-27 09:18:49', '2023-04-27 09:18:49', 0),
       (6, 3, 4, 1, 6, 0, '2023-04-27 09:18:49', '2023-04-27 09:18:49', 0),
       (7, 4, 3, 64, 18926, 0, '2023-04-27 09:18:49', '2023-05-11 15:23:39', 0),
       (8, 4, 2, 237, 9863, 0, '2022-12-09 14:29:15', '2023-05-29 10:30:22', 0),
       (9, 14, 1, 0, 100, 0, '2023-05-08 23:32:11', '2023-05-08 23:32:11', 0),
       (10, 22, 1, 0, 100, 0, '2023-05-09 00:13:30', '2023-05-09 00:13:30', 0),
       (11, 23, 1, 0, 100, 0, '2023-05-09 00:14:28', '2023-05-09 00:14:28', 0),
       (12, 24, 1, 0, 100, 0, '2023-05-09 00:17:28', '2023-05-09 00:17:28', 0),
       (13, 24, 2, 4, 96, 0, '2023-05-09 00:17:28', '2023-05-09 00:18:59', 0),
       (14, 24, 3, 0, 100, 0, '2023-05-09 00:17:28', '2023-05-09 00:17:28', 0),
       (15, 25, 1, 1, 99, 0, '2023-05-09 01:42:53', '2023-05-09 01:43:39', 0),
       (16, 25, 2, 0, 100, 0, '2023-05-09 01:42:53', '2023-05-09 01:42:53', 0),
       (17, 25, 3, 0, 100, 0, '2023-05-09 01:42:53', '2023-05-09 01:42:53', 0),
       (18, 26, 1, 0, 200, 0, '2023-05-09 11:32:14', '2023-05-09 11:32:14', 0),
       (19, 26, 2, 0, 100, 0, '2023-05-09 11:32:14', '2023-05-09 11:32:14', 0),
       (20, 26, 3, 0, 100, 0, '2023-05-09 11:32:15', '2023-05-09 11:32:15', 0),
       (21, 27, 1, 0, 100, 0, '2023-05-09 11:58:22', '2023-05-09 11:58:22', 0),
       (22, 27, 2, 0, 100, 0, '2023-05-09 11:58:22', '2023-05-09 11:58:22', 0),
       (23, 27, 3, 0, 100, 0, '2023-05-09 11:58:22', '2023-05-09 11:58:22', 0),
       (24, 28, 1, 22, 78, 0, '2023-05-10 00:18:04', '2023-05-10 00:21:54', 0),
       (25, 28, 2, 7, 93, 0, '2023-05-10 00:18:04', '2023-05-10 00:22:08', 0),
       (26, 28, 3, 0, 100, 0, '2023-05-10 00:18:04', '2023-05-10 00:18:04', 0),
       (27, 29, 1, 0, 100, 0, '2023-05-10 00:18:25', '2023-05-10 00:18:25', 0),
       (28, 29, 2, 0, 100, 0, '2023-05-10 00:18:25', '2023-05-10 00:18:25', 0),
       (29, 29, 3, 0, 100, 0, '2023-05-10 00:18:25', '2023-05-10 00:18:25', 0),
       (30, 30, 1, 3, 97, 0, '2023-05-10 23:07:22', '2023-05-10 23:08:21', 0),
       (31, 30, 2, 6, 94, 0, '2023-05-10 23:07:22', '2023-05-10 23:08:42', 0),
       (32, 30, 3, 2, 98, 0, '2023-05-10 23:07:22', '2023-05-10 23:08:59', 0),
       (33, 31, 1, 2, 98, 0, '2023-05-11 19:52:06', '2023-05-11 19:53:11', 0),
       (34, 31, 2, 5, 95, 0, '2023-05-11 19:52:06', '2023-05-11 19:53:38', 0),
       (35, 31, 3, 2, 98, 0, '2023-05-11 19:52:06', '2023-05-11 19:53:46', 0);