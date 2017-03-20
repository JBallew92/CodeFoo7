/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import entities.Thumbnail;
import entities.Article;
import entities.Video;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author James Ballew
 */
public class GenerateDB {

    static URL url;
    static String username;
    static String password;
    static ArrayList<Article> articles;
    static ArrayList<Video> videos;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws MalformedURLException, IOException, SQLException {
        //get user input for mysql username and pw for future connection
        System.out.println("Enter Mysql username: ");
        username = scanner.nextLine();
        System.out.println("Enter Mysql password: ");
        password = scanner.nextLine();
        System.out.println("Loading in articles...");
        getArticles();
        System.out.println("Loading in videos...");
        getVideos();
        createDatabase();
        initializeDB();
    }

    public static void getArticles() throws MalformedURLException, IOException {
        int index = getIndex();
        int count = getCount("articles", index);
        
        //create url connection to API
        url = new URL("http://ign-apis.herokuapp.com/articles?startIndex=" + index + "\\u0026count=" + count + "");
        URLConnection urlc = url.openConnection();

        try { //get result    
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc
                    .getInputStream()));
            String temp = null;
            String jsonContent = "";
            while ((temp = br.readLine()) != null) {
                jsonContent += temp;
            }
            //parse the JSON response using json-simple.jar
            Object obj = JSONValue.parse(jsonContent);
            JSONObject jsonObject = (JSONObject) obj;
            Object data = jsonObject.get("data");
            JSONArray array = (JSONArray) data;
            articles = new ArrayList<>(array.size());
            for (int i = 0; i < array.size(); i++) {
                JSONObject subobject = (JSONObject) array.get(i);
                Object metadata = subobject.get("metadata");
                JSONObject mdData = (JSONObject) metadata;
                String articleType = mdData.get("articleType").toString();
                String headline = mdData.get("headline").toString();
                String subHeadline = mdData.get("subHeadline").toString();
                String publishDate = mdData.get("publishDate").toString();
                String state = mdData.get("state").toString();
                String slug = mdData.get("slug").toString();

                //determine all networks
                JSONArray networksArray = (JSONArray) mdData.get("networks");
                ArrayList<String> networks = new ArrayList<>();
                for (int j = 0; j < networksArray.size(); j++) {
                    networks.add(networksArray.get(j).toString());
                }

                //Read in tags
                Object tObject = subobject.get("tags");
                JSONArray tArray = (JSONArray) tObject;
                ArrayList<String> tags = new ArrayList<>();
                for (int j = 0; j < tArray.size(); j++) {
                    tags.add(tArray.get(j).toString());
                }

                //Read in thumbnails
                Object tnObject = subobject.get("thumbnails");
                JSONArray tnArray = (JSONArray) tnObject;
                ArrayList<Thumbnail> thumbnails = new ArrayList<>();
                for (int j = 0; j < tnArray.size(); j++) {
                    JSONObject thumbnail = (JSONObject) tnArray.get(j);
                    String size = thumbnail.get("size").toString();
                    long width = (long) thumbnail.get("width");
                    long height = (long) thumbnail.get("height");
                    URL tempUrl = new URL(thumbnail.get("url").toString());
                    thumbnails.add(new Thumbnail(size, tempUrl, width, height));
                }

                //add article to articles
                articles.add(new Article(articleType, headline, subHeadline, publishDate, state,
                        slug, networks, tags, thumbnails));
            }
            System.out.println("Articles complete!");
            System.out.println("TOTAL ARTICLES = " + articles.size());

        } catch (IOException ex) {
        }
    }

    public static void getVideos() throws MalformedURLException, IOException {

        int index = getIndex();
        int count = getCount("videos", index);

        url = new URL("http://ign-apis.herokuapp.com/videos?startIndex=" + index + "\\u0026count=" + count + "");
        URLConnection urlc = url.openConnection();

        try { //get result    
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc
                    .getInputStream()));
            String temp = null;
            String jsonContent = "";
            while ((temp = br.readLine()) != null) {
                jsonContent += temp;
            }
            //parse the JSON response
            Object obj = JSONValue.parse(jsonContent);
            JSONObject jsonObject = (JSONObject) obj;
            Object data = jsonObject.get("data");
            JSONArray array = (JSONArray) data;
            videos = new ArrayList<>(array.size());
            for (int i = 0; i < array.size(); i++) {
                JSONObject subobject = (JSONObject) array.get(i);
                Object metadata = subobject.get("metadata");
                JSONObject mdData = (JSONObject) metadata;
                String name = mdData.get("name").toString();
                String description = mdData.get("description").toString();
                String publishDate = mdData.get("publishDate").toString();
                System.out.println(mdData);
                String longTitle;
                if (mdData.get("longTitle") == null) {
                    longTitle = description;
                } else {
                    longTitle = mdData.get("longTitle").toString();
                }
                long duration = (long) mdData.get("duration");
                URL videoUrl = new URL(mdData.get("url").toString());
                String state = mdData.get("state").toString();
                String slug = mdData.get("slug").toString();

                //determine all networks
                JSONArray networksArray = (JSONArray) mdData.get("networks");
                ArrayList<String> networks = new ArrayList<>();
                for (int j = 0; j < networksArray.size(); j++) {
                    networks.add(networksArray.get(j).toString());
                }

                //Read in tags
                Object tObject = subobject.get("tags");
                JSONArray tArray = (JSONArray) tObject;
                ArrayList<String> tags = new ArrayList<>();
                for (int j = 0; j < tArray.size(); j++) {
                    tags.add(tArray.get(j).toString());
                }

                //Read in thumbnails
                Object tnObject = subobject.get("thumbnails");
                JSONArray tnArray = (JSONArray) tnObject;
                ArrayList<Thumbnail> thumbnails = new ArrayList<>();
                for (int j = 0; j < tnArray.size(); j++) {
                    JSONObject thumbnail = (JSONObject) tnArray.get(j);
                    String size = thumbnail.get("size").toString();
                    long width = (long) thumbnail.get("width");
                    long height = (long) thumbnail.get("height");
                    URL tempUrl = new URL(thumbnail.get("url").toString());
                    thumbnails.add(new Thumbnail(size, tempUrl, width, height));
                }

                //add video to videos
                videos.add(new Video(name, description, publishDate, longTitle, duration, videoUrl,
                        state, slug, networks, tags, thumbnails));
            }
            System.out.println("Videos complete!");
            System.out.println("TOTAL VIDEOS = " + videos.size());
        } catch (IOException ex) {
        }
    }

    //gets user input for starting index used in api
    public static int getIndex() {
        System.out.println("What is the starting index?");
        scanner = new Scanner(System.in);
        int index = -1;
        //index must be positive
        while (index < 0 && scanner.hasNextInt()) {
                index = scanner.nextInt();
                if (index < 0)
                System.out.println("Please enter a positive whole number: ");
           
        }
        return index;
    }

    //gets user input for amount of information to me requested from api. Must be between 1 and 20 
    //index + count can not exceed maximum index of 300
    public static int getCount(String type, int index) {
        System.out.println("How many " + type + " would you like to load (between 1 and 20)?");
        scanner = new Scanner(System.in);
        int count = 0;
            while ((count < 1 || count > 20) && scanner.hasNextInt()) {
                count = scanner.nextInt();
                if ((index + count) > 300) {
                    System.out.println("Count can not exceed " + (300 - index));
                    count = 0;
                    System.out.println("Please enter an integer less than " + (300 - index));
                }
            }
        return count;
    }
    
    //initialize database
    public static void initializeDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/igncodefoo", username, password);
            System.out.println("Database connected");

            //insert articles
            PreparedStatement pstmtInput = conn.prepareStatement("insert into Article "
                    + "(articleType, headline, subHeadline, publishDate, state, slug)"
                    + " values (?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < articles.size(); i++) {
                pstmtInput.setString(1, articles.get(i).getArticleType());
                pstmtInput.setString(2, articles.get(i).getHeadline());
                pstmtInput.setString(3, articles.get(i).getSubHeadline());
                pstmtInput.setString(4, articles.get(i).getPubDate());
                pstmtInput.setString(5, articles.get(i).getState());
                pstmtInput.setString(6, articles.get(i).getSlug());
                try {
                    pstmtInput.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //Insert all thumbnails for article
                insertThumbnails(conn, articles.get(i).getHeadline(), articles.get(i).getThumbnails());
                //Insert all networks for article
                insertNetworks(conn, articles.get(i).getHeadline(), articles.get(i).getNetworks());

                //Insert all tags for article
                insertTags(conn, articles.get(i).getHeadline(), articles.get(i).getTags());
            }
            System.out.println("Articles inserted!");

            //insert videos
            pstmtInput = conn.prepareStatement("insert into Video "
                    + "(name, description, publishDate, longTitle, duration,"
                    + "url, slug, state)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < videos.size(); i++) {
                pstmtInput.setString(1, videos.get(i).getName());
                pstmtInput.setString(2, videos.get(i).getDescription());
                pstmtInput.setString(3, videos.get(i).getPubDate());
                pstmtInput.setString(4, videos.get(i).getLongTitle());
                pstmtInput.setLong(5, videos.get(i).getDuration());
                pstmtInput.setString(6, videos.get(i).getUrl().toString());
                pstmtInput.setString(7, videos.get(i).getSlug());
                pstmtInput.setString(8, videos.get(i).getState());
                try {
                    pstmtInput.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //Insert all thumbnails for video
                insertThumbnails(conn, videos.get(i).getLongTitle(), videos.get(i).getThumbnails());
                //Insert all networks for video
                insertNetworks(conn, videos.get(i).getLongTitle(), videos.get(i).getNetworks());

                //Insert all tags for video
                insertTags(conn, videos.get(i).getLongTitle(), videos.get(i).getTags());

            }
            System.out.println("Videos inserted!");
            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed");
        }
    }
    
    //insert thumbnail to thumbnail table, shared method by videos and articles
    public static void insertThumbnails(Connection conn, String title, ArrayList<Thumbnail> thumbnails) throws SQLException {
        PreparedStatement pstmtThumbnails = conn.prepareStatement("insert into Thumbnail "
                + "(title, url, size, width, height) values (?, ?, ?, ?, ?)");
        pstmtThumbnails.setString(1, title);
        for (int j = 0; j < thumbnails.size(); j++) {
            pstmtThumbnails.setString(2, thumbnails.get(j).getURL().toString());
            pstmtThumbnails.setString(3, thumbnails.get(j).getSize());
            pstmtThumbnails.setLong(4, thumbnails.get(j).getWidth());
            pstmtThumbnails.setLong(5, thumbnails.get(j).getHeight());
            try {
                pstmtThumbnails.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    //insert networks to network table, shared method by videos and articles
    public static void insertNetworks(Connection conn, String title, ArrayList<String> networks) throws SQLException {
        PreparedStatement pstmtNetworks = conn.prepareStatement("insert into Network "
                + "(title, network) values (?, ?)");
        pstmtNetworks.setString(1, title);
        for (int j = 0; j < networks.size(); j++) {
            pstmtNetworks.setString(2, networks.get(j));
            try {
                pstmtNetworks.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    //insert tags into tags table, shared method by videos and articles.
    public static void insertTags(Connection conn, String title, ArrayList<String> tags) throws SQLException {
        PreparedStatement pstmtTags = conn.prepareStatement("insert into Tag "
                + "(title, tag) values (?, ?)");
        pstmtTags.setString(1, title);
        for (int j = 0; j < tags.size(); j++) {
            pstmtTags.setString(2, tags.get(j));
            try {
                pstmtTags.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    //create database and tables if they don't exists.
    //Not sure if IGN wants .sql file for database so just building db in java
    //.sql file will be posted to GitHub
    private static void createDatabase() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/", username, password);
            Statement stmt = conn.createStatement();
            String checkDB = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'igncodefoo'";
            ResultSet rset = stmt.executeQuery(checkDB);
            if (!rset.next()) {
                String dataBase = "CREATE DATABASE igncodefoo;";
                stmt.executeUpdate(dataBase);
                dataBase = "USE igncodefoo;";
                stmt.executeUpdate(dataBase);
                dataBase = "DROP table if exists Article;";
                stmt.executeUpdate(dataBase);
                dataBase = "DROP table if exists Video;";
                stmt.executeUpdate(dataBase);
                dataBase = "DROP table if exists Thumbnail;";
                stmt.executeUpdate(dataBase);
                dataBase = "DROP table if exists Network;";
                stmt.executeUpdate(dataBase);
                dataBase = "DROP table if exists Tag;";
                stmt.executeUpdate(dataBase);
                dataBase = "CREATE table Article ("
                        + "	articleType varchar(100),"
                        + "	headline varchar(255) unique not null,"
                        + "	subHeadline text,"
                        + "	publishDate text,"
                        + "	state varchar(50),"
                        + "	slug text,"
                        + "	constraint pkArticle primary key (headline)"
                        + ");";
                stmt.executeUpdate(dataBase);
                dataBase = "CREATE table Video ("
                        + "	name text,"
                        + "	description text,"
                        + "	publishDate text,"
                        + "	longTitle varchar(255) unique not null,"
                        + "	duration bigint,"
                        + "	url text,"
                        + "	slug text,"
                        + "	state varchar(50),"
                        + "	constraint pkVideo primary key (longTitle)"
                        + ");";
                stmt.executeUpdate(dataBase);
                //wanted url to be type Text but this causes issues since you can not set "not null" constraint on text
                //and data type must be not null to be eligible for primary key
                dataBase = "CREATE table Thumbnail ("
                        + "	title varchar(255) not null,"
                        + "	url varchar(255) not null,"
                        + "        size varchar(20),"
                        + "        width bigint,"
                        + "        height bigint,"
                        + "	constraint pkThumbnail primary key (title, url)"
                        + ");";
                stmt.executeUpdate(dataBase);
                dataBase = "CREATE table Network ("
                        + "	title varchar(255) not null,"
                        + "	network varchar(50) not null,"
                        + "	constraint pkNetwork primary key (title, network)"
                        + ");";
                stmt.executeUpdate(dataBase);
                dataBase = "CREATE table Tag ("
                        + "	title varchar(255) not null,"
                        + "	tag varchar(50) not null,"
                        + "	constraint pkTag primary key (title, tag)"
                        + ");";
                stmt.executeUpdate(dataBase);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenerateDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
