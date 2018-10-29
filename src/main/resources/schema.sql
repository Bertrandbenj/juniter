DROP DATABASE testdb;

CREATE DATABASE testdb
  WITH OWNER = testuser
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;


-- Table: public.block

-- DROP TABLE public.block;

CREATE TABLE public.block
(
  hash character varying(255) NOT NULL,
  "number" integer NOT NULL,
  currency character varying(255),
  dividend integer,
  inner_hash character varying(64),
  issuer character varying(45),
  issuerscount integer,
  issuersframe integer,
  issuersframevar integer,
  mediantime bigint,
  memberscount integer,
  monetarymass bigint,
  nonce bigint,
  parameters character varying(255),
  powmin integer,
  previous_hash character varying(64),
  previous_issuer character varying(45),
  signature character varying(88),
  "time" bigint,
  unitbase integer,
  version smallint,
  CONSTRAINT block_pkey PRIMARY KEY (hash, number),
  CONSTRAINT block_unitbase_check CHECK (unitbase >= 0 AND unitbase <= 0)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.block
  OWNER TO testuser;

