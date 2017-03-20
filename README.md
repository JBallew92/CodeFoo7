# CodeFoo7-Introduction Video
<a href="https://www.youtube.com/watch?v=xI2GDiLRjyA">A Brief Introduction</a>

# CodeFoo7-Lincoln Log Problem
<a href="https://www.youtube.com/watch?v=CIfKVJgA_pc&t=1s">Lincoln Log Problem(Video)</a>

I started by gathering information about the Empire State Building and determining what I did and didn't know.

What we know:
- Width of the building's base = 187ft
- Lenght of the building's base = 424ft
- Height of building up to last floor = 1250ft
- Height of building's antenna = 204ft

What we don't know:
- Change in building's dimensions as it rises
- any other dimensions regarding the antenna

The fact that we don't know the change in dimension is very important because it means the best we can do is find an at most amount of Lincoln logs needed to fill the volume of the dimensions given. This will lead to an excess in Lincoln logs.

I then sat down with some Lincoln logs that I had avaliable and determined a interlocking pattern that could be repeated to fill the width, length, and height of the Empire State Building. This pattern is examined in the video. Furthermore, this pattern used 3 small logs and 3 large logs, and stretched a lenght of 13.625inches and stood 2.625 inches talls.

After deciding on my pattern I then found out how many patterns I would need to fill the rectangular prism formed by the dimensions of the Empire State Building there were avaliable. I started by converting the building's dimensions into inches.

Conversion to Inches:
- W = 187ft x 12in = 2244in
- L = 424ft x 12in = 5088in
- H(building) = 1250ft x 12in = 15,000in
- H(antenna) = 204ft x 12in = 2448in

I then converted the dimensions to patterns by dividing the width and length by my pattern's length, and the height by my pattern's height.
- W = 2244in / 13.625in = 165 patterns
- L = 5088in / 13.625in = 374 patterns
- H(building) = 15,000in / 2.625in = 5715 patterns

I then used the volume formulate (L X W X H) for a rectangular prism to find out how many patterns would be needed to fill the volume of the dimensions given for the Empire State Building.
- 374 x 165 x 5715 = 352,672,650 patterns total

I then converted my total patterns back to logs using the fact that my pattern consisted of 3 large logs and 3 small logs.
- 352,672,650 x 3 large logs = 1,058,017,950 large logs
- 352,672,650 x 3 small logs = 1,058,017,950 small logs

I then found how many large logs would be needed to build the antenna. For this step I simply assumed large logs would be stack vertically on each other to reach the antenna's height since no other dimensions were given for the antenna. The height of one large Lincoln log is 7.625in.
- H(antenna) = 2448in / 7.625in = 322 large logs

Finally, I am able to determine the maximum amount of logs needed to make a to-scale model of the Empire State Building.
- Large logs for building + large logs for antenna = 1,058,018,272
- Small logs for building = 1,058,017,950
- GRAND TOTAL: 2,116,036,222 Lincoln Logs

Once again, this measure fails to take into account the change in dimensions as the building rises. Therefore you will have an excess amount of logs and probably don't even need to account for the antenna. You could just use leftovers from the building. And don't forget to use Gorilla Glue to protect from wild Kongs.

# CodeFoo7-BackEnd
BackEnd program submission for IGN's CodeFoo 2017 application

GenerateDB.java pulls data from api, parses the JSON, creates the respective objects, and populates the mysql database with the objects. JSON is parsed using json-simple library, documentation can be found <a href="https://github.com/fangyidong/json-simple">here</a>. If database does not exist, one is created. Once the program is ran, the user is asked for the starting index and the count of the articles and videos being requested. The index can range from 0-300 and the count can range from 1-20. However, the count+index !> 300. Its also important to note that while this allows for the count to be greater than or less than 10, the api will always return 10 items. Whether this is intended or not is up to IGN.

There are 3 seperate classes; Article, Video, and Thumbnail. Both Article and Video contain an arraylist of Thumbnails. Creating custom classes for these 3 objects allows for increased modularity of the program. Also, the Article and Video class contain an arraylist of networks even though the data provided only seemed to contain one network (IGN). However, the data was provided as a JSON Array so I assumed that future data may contain more networks.

MRSSFeed.java pulls data from mysql database, creates the respective objects, and generates an xml file that fits RSS 2.0 and MRSS 1.5.1 specifications. File saved at user home directory using the filename inputed by user.

Mysql version 5.6.35 used in development of project. Mysql connection uses user inputed USERNAME and PASSWORD.

# CodeFoo7-GridChainSolver
Please refer to its respective repository <a href="https://github.com/JBallew92/CodeFoo7-GridChainSolver/edit/master/README.md">here</a>

