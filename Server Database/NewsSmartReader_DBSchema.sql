create table user_info (
    telephone   numeric(13)     primary key,
    name        varchar(30)     not null,
    email_id    varchar(50)     not null,
    gcm_id      long varchar    not null
);

create table user_auth_codes (
    telephone   numeric(13)     not null,
    code        integer         not null
);

create table cron_job (
	week_day 	smallint 	not null,
	time		numeric(4)  not null,
	source_id	integer		not null,
	primary key (week_day,  time, source_id),
	foreign key (source_id) references news_sites_info(source_id)
);

create table news_sites_info (
	source_id	integer,
	title		varchar(20),
	apikey		varchar(100),
	primary key (source_id, title) 
);

create table news_item_info (
	item_id		integer,
	source_id	integer,
	title		varchar(100),
	abstract	text,
	text		text,
	datetime	date,
	primary key (item_id, source_id),
	foreign key (source_id) references news_sites_info (source_id) 
);