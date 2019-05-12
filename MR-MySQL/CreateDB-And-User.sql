create database testdb;
CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'test';
USE testdb;
GRANT ALL ON testdb.* TO 'testuser'@'localhost' IDENTIFIED BY 'test123';