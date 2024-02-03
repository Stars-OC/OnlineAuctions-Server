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
    role       int        default 0    COMMENT '权限等级' not null,
    create_at  bigint   not null comment '创建时间/秒级',
    deleted    tinyint(1) default 0  comment '是否被删除' not null

)AUTO_INCREMENT = 200000000;

create index idx_username on user (username);
create index idx_time on user (create_at);

# 用户钱包
create table wallet
(

    username BIGINT  primary key      not null,
    money decimal(18,2) DEFAULT '0.00' COMMENT '账户余额',
    fund decimal(18,2) DEFAULT '0.00' COMMENT '拍卖额度',
    password char(64) not null,
    update_at bigint   not null comment '更新时间/秒级'

);

# 用户订单
create table `order`
(
    order_id BIGINT primary key      not null,
    username BIGINT  not null,
    create_at bigint   not null comment '创建时间/秒级',
    deleted    tinyint(1) default 0  comment '是否被删除' not null
);

create index idx_username on `order` (username);

# 订单信息
create table order_info
(
    order_id BIGINT auto_increment primary key      not null,
    title char(64)       comment '订单标题'          not null,
    description text default null comment '订单描述',
    type       int        default 0    COMMENT '订单类型' not null,
    balance   decimal(18,2) not null COMMENT '订单金额',
    create_at bigint   not null comment '成交时间/秒级',
    cargo_id BIGINT   not null comment '物品id',
    status   int        default 0    COMMENT '状态' not null,
    deleted tinyint(1) default 0  comment '是否被删除' not null
);

create index idx_status on order_info (status);
create index idx_time on order_info (create_at);
create index idx_cargo on order_info (cargo_id);

# 物品
create table cargo
(
    cargo_id BIGINT auto_increment primary key      not null,
    name     char(128)                            not null,
    description text default null,
    resource text default null comment  '存储图片/视频资源Json格式',
    type       int        default 0    COMMENT '物品类型' not null,
    seller    BIGINT   not null comment '卖家',
    create_at bigint   not null comment '创建时间/秒级',
    update_at bigint   not null comment '更新时间/秒级',
    status   int        default 0    COMMENT '状态' not null,
    deleted tinyint(1) default 0  comment '是否被删除' not null

);

create index idx_status on cargo (status);


create table auction
(
    auction_id BIGINT auto_increment primary key      not null,
    cargo_id   BIGINT   not null comment '货物id',
    starting_price decimal(18,2) DEFAULT '0.00' COMMENT '起拍价格',
    additional_price decimal(18,2) DEFAULT '0.00' COMMENT '加价幅度',
    hammer_price decimal(18,2) DEFAULT '0.00' COMMENT '最后价格',
    start_time bigint default 0 not null  comment '开始时间/秒级',
    end_time bigint  default 0 not null comment '结束时间/秒级',
    status   int        default 0    COMMENT '状态' not null,
    deleted tinyint(1) default 0  comment '是否被删除' not null
);

create index idx_cargo_id on auction (cargo_id);
create index idx_time on auction (start_time);
create index idx_status on auction (status);

# 拍卖记录
create table auction_log
(
    auction_id BIGINT primary key      not null,
    bidder     BIGINT   not null comment '出价者',
    price      decimal(18,2) DEFAULT '0.00' comment '出价金额',
    create_at  bigint   not null comment '创建时间/秒级'
);


create index idx_time on auction_log (create_at);
create index idx_bidder on auction_log (bidder);