DROP DATABASE testdb;

CREATE DATABASE testdb
  WITH OWNER = testuser
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;




CREATE VIEW accounts as
SELECT conditions, sum(case WHEN consumed THEN 0-amount ELSE amount end )  bSum
  FROM sindex
 -- where consumed = false
 GROUP BY conditions
 --HAVING sum(case WHEN consumed THEN 0-amount ELSE amount end ) < 100
 ORDER by conditions
  ;
