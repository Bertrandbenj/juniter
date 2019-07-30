--DROP DATABASE testdb;
--
--CREATE DATABASE testdb
--  WITH OWNER = testuser
--       ENCODING = 'UTF8'
--       TABLESPACE = pg_default
--       LC_COLLATE = 'en_US.UTF-8'
--       LC_CTYPE = 'en_US.UTF-8'
--       CONNECTION LIMIT = -1;
--
--
--
--
CREATE VIEW accounts as
SELECT conditions, sum(case WHEN consumed THEN 0-amount ELSE amount end )  bSum
  FROM sindex
 -- where consumed = false
 GROUP BY conditions
 --HAVING sum(case WHEN consumed THEN 0-amount ELSE amount end ) < 100
 ORDER by conditions
  ;
--

drop table if exists spittle;
drop table if exists spitter;

create table spitter (
  id identity,
  username varchar(25) not null,
  password varchar(25) not null,
  fullname varchar(100) not null,
  email varchar(50) not null,
  update_by_email boolean not null
);

create table spittle (
  id integer identity primary key,
  spitter_id integer not null,
  spittleText varchar(2000) not null,
  postedTime date not null,
--  foreign key (spitter_id) references spitter(id)
);