-- Use the following scripts to create a mysql database called sample with a user called samplemgr
-- Login to mysql  bash>mysql -u root -p
create database autos;
grant all privileges on autos.* to 'autouser'@'localhost' identified by 'autopass';
use autos;
-- ** OPTIONAL for mysql access from other hosts.
-- mysql>grant all privileges on autos.* to 'autouser'@'%' identified by 'autopass';

-- Add the following to $TOMCAT_HOME/conf/context.xml

/*
 * 
 * 
 * 
    <Resource name="jdbc/Autos" auth="Container"
             type="javax.sql.DataSource"
             username="autouser"
             password="autopass"
             driverClassName="com.mysql.jdbc.Driver"
             url="jdbc:mysql://localhost/autos"
             maxActive="10" maxIdle="4"/>

*/
-- download and add latest mysql jdbc jar to $TOMCAT_HOME/lib 
-- ie: mysql-connector-java-3.1.12-bin.jar
-- Login to mysql as autouser (bash>mysql -u autouser -p ) and create the following table.

-- MySQL
CREATE TABLE autos (
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 make varchar(32),
 model varchar(32),
 colour varchar(32),
 cost int
 );
 
 insert into autos values (null,'Volvo','960','Grey',2000000);
 insert into autos values (null,'Hyundai','Elanta','Grey',2000000);
 insert into autos values (null,'Ford','Escort','Red',1200000);
/*
/*
/*
-- MS SQL Server
CREATE TABLE sample_autos (
 id INT IDENTITY(1,1) PRIMARY KEY,
 make varchar(32),
 model varchar(32),
 colour varchar(32),
 cost int
 );
 */