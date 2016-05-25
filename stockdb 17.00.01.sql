DROP TABLE stockuser;
DROP TABLE transactions;
DROP TABLE portfolio;

CREATE TABLE stockuser
  (user_nick VARCHAR(10),
  cash DOUBLE PRECISION,
  psw CHAR(4) NOT NULL,
  CHECK (cash >=0),
  CONSTRAINT user_key PRIMARY KEY (user_nick) );

CREATE TABLE transactions
  (user_nick VARCHAR(10),
  trans_type VARCHAR(4),
  stock VARCHAR(7),
  price DOUBLE PRECISION,
  units INTEGER,
  time_stamp TIMESTAMP,
  CONSTRAINT transaction_KEY PRIMARY KEY (time_stamp,user_nick) );

CREATE TABLE portfolio
  (user_nick VARCHAR(10),
  stock VARCHAR(7),
  currentprice DOUBLE PRECISION,
  units INTEGER,
  CHECK (units >=0),
  CONSTRAINT portfoliokey PRIMARY KEY (stock,user_nick) );

INSERT INTO stockuser VALUES('herrmalte',1000000,'0115');
INSERT INTO transactions VALUES('herrmalte', 'Buy', 'goog', 10.3, 5, '2011-05-16 15:36:38');
INSERT INTO portfolio VALUES('herrmalte', 'goog', 10.3, 5);
