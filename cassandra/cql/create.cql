CREATE KEYSPACE collision_db
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};

drop table collisions_by_zip; 

CREATE TABLE  collisions_by_zip(
  zip text,
  col_date date,
  key int,
  col_time text,
  borough text,
  on_street text,
  cross_street text,
  off_street text,
  location text,
  PRIMARY KEY(zip, col_date, key)  
 );
 
select * from collision_db.collisions_by_zip;
 
truncate collision_db.collisions_by_zip;
 
select count(*) from collision_db.collisions_by_zip;
 
insert into collisions_by_zip(zip, col_date, key, borough)
	values('11', '2016-10-14', 1, 'queens');	


insert into collisions_by_zip(zip, col_date, key, borough)
 	values('12', '2016-10-14', 1, 'queens');	
  
insert into collisions_by_zip(zip, col_date, key, borough, col_time)
 	values('12', '2016-10-14', 2, 'queens', '11:10');	