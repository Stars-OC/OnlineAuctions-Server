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
    money decimal(18,2) DEFAULT '0.00' COMMENT '账户余额',
    fund decimal(18,2) DEFAULT '0.00' COMMENT '拍卖额度',
    role       int        default 0    COMMENT '权限等级' not null,
    create_at  bigint   not null comment '创建时间/秒级',
    deleted    tinyint(1) default 0  comment '是否被删除' not null

)AUTO_INCREMENT = 200000000;

create index idx_username on user (username);
create index idx_time on user (create_at);

# 物品
create table cargo
(
    cargo_id BIGINT auto_increment primary key      not null,
    name     char(128)                            not null,
    description text default null,
    resource text default null comment  '存储图片/视频资源Json格式',
    type       int        default 0    COMMENT '物品类型' not null,
    seller    BIGINT   not null comment '卖家',
    starting_price decimal(18,2) DEFAULT '0.00' COMMENT '起拍价格',
    additional_price decimal(18,2) DEFAULT '0.00' COMMENT '加价幅度',
    hammer_price decimal(18,2) DEFAULT '0.00' COMMENT '最后价格',
    start_time bigint   comment '开始时间/秒级',
    end_time bigint   comment '结束时间/秒级',
    create_at bigint   not null comment '创建时间/秒级',
    update_at bigint   not null comment '更新时间/秒级',
    status   int        default 0    COMMENT '状态' not null,
    deleted tinyint(1) default 0  comment '是否被删除' not null

);

create index idx_cargo_id on cargo (cargo_id);
create index idx_status on cargo (status);
create index idx_time on cargo (start_time);

# 拍卖记录
create table auction
(
    auction_id BIGINT auto_increment primary key      not null,
    cargo_id   BIGINT   not null comment '货物id',
    bidder     BIGINT   not null comment '出价者',
    price      decimal(18,2) DEFAULT '0.00' comment '出价金额',
    create_at  bigint   not null comment '创建时间/秒级'
);

create index idx_auction_id on auction (auction_id);
create index idx_time on auction (create_at);
create index idx_bidder on auction (bidder);