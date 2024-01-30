create database auctions character set utf8mb4;

use auctions;
set character set utf8mb4;

# 用户
create table user
(
    username    BIGINT auto_increment primary key      not null,
    nickname   char(16)                            not null,
    password   char(100)                           not null,
    description varchar(255) default null        ,
    avatar_url varchar(255) default null         ,
    role       int        default 0                not null,
    create_at  bigint   not null comment '秒级',
    deleted    tinyint(1) default 0                not null
)AUTO_INCREMENT = 200000000;


create index idx_username on user (username);
create index idx_time on user (create_at);
