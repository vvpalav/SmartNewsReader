drop table cron_job;
drop table news_item_info;
drop table user_subscription_details;
drop table news_sites_info;
drop table user_info;
drop table user_auth_codes;

create table user_info (
    telephone   numeric(11)     primary key,
    name        varchar(30)     not null,
    email_id    varchar(50)     not null,
    gcm_id      long varchar    not null
);

create table user_auth_codes (
    telephone   numeric(13)     not null,
    code        integer         not null
);

create table news_sites_info (
	source_id	integer,
	title		varchar(20),
	apikey		varchar(100),
	apilink		text,
	primary key (source_id, title) 
);

create table cron_job (
	source_id	integer		not null,
	time		numeric(4)  not null,
	primary key (time, source_id),
	foreign key (source_id) references news_sites_info(source_id)
);

create table news_item_info (
	item_id		integer,
	source_id	integer,
	title		varchar(100),
	abstract	text,
	text		text,
	datetime	date,
	url		    text,
	primary key (item_id, source_id),
	foreign key (source_id) references news_sites_info (source_id) 
);

create table user_subscription_details (
    telephone   numeric(11)     not null,
    source_id   integer         not null,
    sub_date    date		    not null,
    primary key (telephone, source_id),
    foreign key (telephone) references user_info(telephone),
    foreign key (source_id) references news_sites_info(source_id)
);