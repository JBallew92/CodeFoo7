# CodeFoo7-BackEnd
BackEnd program submission for IGN's CodeFoo 2017 application

GenerateDB.java pulls data from api and populates mysql database. If database does not exist, one is created. Mysql connection uses USERNAME = "root" and PASSWORD = "" please alter to fit your mysql database. 

MRSSFeed.java pulls data from mysql database and generates an xml file that fits RSS 2.0 and MRSS 1.5.1 specifications. File saved at user home directory.

Mysql version 5.6.35 used in development of project.
