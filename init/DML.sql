drop table googleevent;
create table googleevent (
  id nvarchar(100),
  ical_uid nvarchar(100),
  etag nvarchar(20),
  start datetime not null,
  end datetime not null,
  kind nvarchar(50),
  creator_email nvarchar(4000),
  summary nvarchar(4000),
  description text,
  location nvarchar(4000),
  html_link varchar(4000),
  created datetime not null,
  updated datetime not null,
  primary key (id, ical_uid)
) engine=InnoDB default charset=utf8;

drop table myevent;
create table myevent (
  id nvarchar(100),
  ical_uid nvarchar(100),
  keyword nvarchar(1000),
  category1 nvarchar(1000),
  category2 nvarchar(1000),
  category3 nvarchar(1000),
  requester nvarchar(100),
  content text,
  sr_id nvarchar(20),
  subsystem nvarchar(100),
  created_date datetime not null default now(),
  modified_date datetime not null default now(),
  primary key (id, ical_uid)
) engine=InnoDB default charset=utf8;

drop table category;
create table category (
  id MEDIUMINT not null auto_increment,
  name nvarchar(100) not null,
  level INTEGER not null default 0,
  sort_no integer default 10,
  parent_id MEDIUMINT not null default 0,
  primary key (id)
) engine=InnoDB default charset=utf8;

drop table category_keyword;
create table category_keyword (
  name nvarchar(100) not null UNIQUE,
  category_id MEDIUMINT not null default 0,
  primary key (name)
) engine=InnoDB default charset=utf8;