# CodeFoo7-Introduction Video
<a href="https://www.youtube.com/watch?v=xI2GDiLRjyA">A Brief Introduction</a>

# CodeFoo7-Lincoln Log Problem
<a href="https://www.youtube.com/watch?v=CIfKVJgA_pc&t=1s">Lincoln Log Problem</a>

# CodeFoo7-BackEnd
BackEnd program submission for IGN's CodeFoo 2017 application

GenerateDB.java pulls data from api, parses the JSON, creates the respective objects, and populates the mysql database with the objects. JSON is parsed using json-simple library, documentation can be found <a href="https://github.com/fangyidong/json-simple">here</a>. If database does not exist, one is created. Once the program is ran, the user is asked for the starting index and the count of the articles and videos being requested. The index can range from 0-300 and the count can range from 1-20. However, the count+index !> 300. Its also important to note that while this allows for the count to be greater than or less than 10, the api will always return 10 items. Whether this is intended or not is up to IGN.

There are 3 seperate classes; Article, Video, and Thumbnail. Both Article and Video contain an arraylist of Thumbnails. Creating custom classes for these 3 objects allows for increased modularity of the program. Also, the Article and Video class contain an arraylist of networks even though the data provided only seemed to contain one network (IGN). However, the data was provided as a JSON Array so I assumed that future data may contain more networks.

MRSSFeed.java pulls data from mysql database, creates the respective objects, and generates an xml file that fits RSS 2.0 and MRSS 1.5.1 specifications. File saved at user home directory using the filename inputed by user.

Mysql version 5.6.35 used in development of project. Mysql connection uses user inputed USERNAME and PASSWORD.

# CodeFoo7-GridChainSolver
Please refer to its respective repository <a href="https://github.com/JBallew92/CodeFoo7-GridChainSolver/edit/master/README.md">here</a>

