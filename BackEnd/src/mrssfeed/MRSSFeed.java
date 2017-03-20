/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrssfeed;

import entities.Article;
import entities.Thumbnail;
import entities.Video;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author James Ballew
 */
public class MRSSFeed {

    static ArrayList<Article> articles = new ArrayList<>();
    static ArrayList<Video> videos = new ArrayList<>();
    static DocumentBuilderFactory docFactory;
    static DocumentBuilder docBuilder;
    static Document doc;

    public static void main(String[] args) throws MalformedURLException, IOException, ParseException, ParserConfigurationException, TransformerException {
        initializeDB();
        generateFeed();
    }

    public static void generateFeed() throws MalformedURLException, ParseException, ParserConfigurationException, TransformerException {
        String filename = System.getProperty("user.home");
        System.out.println("Name the output file: ");
        Scanner scanner = new Scanner(System.in);
        filename += "\\" + scanner.nextLine() + ".xml";
        try {
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            //rss is root element
            Element rss = doc.createElement("rss");
            rss.setAttribute("version", "2.0");
            rss.setAttribute("xmlns:media", "http://search.yahoo.com/mrss/");
            doc.appendChild(rss);

            //channel is child to rss
            Element channel = doc.createElement("channel");
            rss.appendChild(channel);

            //title is child to channel
            Element title = doc.createElement("title");
            title.appendChild(doc.createTextNode("IGN"));
            channel.appendChild(title);

            //link is child to channel
            Element link = doc.createElement("link");
            link.appendChild(doc.createTextNode("https://www.ign.com"));
            channel.appendChild(link);

            //description is child to channel
            Element description = doc.createElement("description");
            description.appendChild(doc.createTextNode("IGN is your site for Xbox One, PS4, "
                    + "PC, Wii-U, Xbox 360, PS3, Wii, 3DS, PS Vita &amp; iPhone games with expert reviews, news, previews, trailers, cheat codes, wiki guides &amp; walkthroughs"));
            channel.appendChild(description);

            //image is child to channel
            Element image = doc.createElement("image");
            channel.appendChild(image);

            //add image children. Title and link should match channel's title and link
            Element url = doc.createElement("url");
            url.appendChild(doc.createTextNode("http://oystatic.ignimgs.com/src/core/img/widgets/global/page/ign-logo-100x100.jpg"));
            image.appendChild(url);

            Element imageTitle = doc.createElement("title");
            imageTitle.appendChild(doc.createTextNode("IGN"));
            image.appendChild(imageTitle);

            Element imageLink = doc.createElement("link");
            imageLink.appendChild(doc.createTextNode("https://www.ign.com"));
            image.appendChild(imageLink);

            Element width = doc.createElement("width");
            width.appendChild(doc.createTextNode("100"));
            image.appendChild(width);

            Element height = doc.createElement("height");
            height.appendChild(doc.createTextNode("100"));
            image.appendChild(height);

            //write out article items
            for (int i = 0; i < articles.size(); i++) {
                Element item = doc.createElement("item");
                channel.appendChild(item);
                getItem(articles.get(i), item);
            }

            //write out video items
            for (int i = 0; i < videos.size(); i++) {
                Element item = doc.createElement("item");
                channel.appendChild(item);
                getItem(videos.get(i), item);
            }

            //now output .xml file to user home directory
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);
            System.out.println("File saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //use of polymorphism due to different MRSS requirtements for video and article
    public static void getItem(Article article, Element item) throws MalformedURLException, ParseException {
        String parsedDate = ISO8601DateParser.parse(articles.get(0).getPubDate()).toString();
        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode(article.getHeadline()));
        item.appendChild(title);

        Element description = doc.createElement("description");
        description.appendChild(doc.createTextNode(article.getSubHeadline()));
        item.appendChild(description);

        Element pubDate = doc.createElement("pubDate");
        pubDate.appendChild(doc.createTextNode(parsedDate));
        item.appendChild(pubDate);

        Element mediaContent = doc.createElement("media:content");
        mediaContent.setAttribute("url", article.getUrl().toString());
        mediaContent.setAttribute("type", "text/html");
        mediaContent.setAttribute("medium", article.getArticleType());
        item.appendChild(mediaContent);

        for (int i = 0; i < article.getThumbnails().size(); i++) {
            Element mediaThumbnail = doc.createElement("media:thumbnail");
            mediaThumbnail.setAttribute("url", article.getThumbnails().get(i).getURL().toString());
            mediaThumbnail.setAttribute("height", String.valueOf(article.getThumbnails().get(i).getHeight()));
            mediaThumbnail.setAttribute("width", String.valueOf(article.getThumbnails().get(i).getWidth()));
            mediaContent.appendChild(mediaThumbnail);
        }
    }

    public static void getItem(Video video, Element item) throws MalformedURLException, ParseException {
        String parsedDate = ISO8601DateParser.parse(video.getPubDate()).toString();
        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode(video.getLongTitle()));
        item.appendChild(title);

        Element description = doc.createElement("description");
        description.appendChild(doc.createTextNode(video.getDescription()));
        item.appendChild(description);

        Element pubDate = doc.createElement("pubDate");
        pubDate.appendChild(doc.createTextNode(parsedDate));
        item.appendChild(pubDate);

        Element mediaContent = doc.createElement("media:content");
        mediaContent.setAttribute("url", video.getUrl().toString());
        mediaContent.setAttribute("type", "video/mp4");
        mediaContent.setAttribute("medium", "video");
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(video.getDuration());
        String time = timeOfDay.toString();
        mediaContent.setAttribute("duration", time);
        item.appendChild(mediaContent);

        Element mediaDescription = doc.createElement("media:description");
        mediaDescription.appendChild(doc.createTextNode(video.getName()));
        mediaContent.appendChild(mediaDescription);

        for (int i = 0; i < video.getThumbnails().size(); i++) {
            Element mediaThumbnail = doc.createElement("media:thumbnail");
            mediaThumbnail.setAttribute("url", video.getThumbnails().get(i).getURL().toString());
            mediaThumbnail.setAttribute("height", String.valueOf(video.getThumbnails().get(i).getHeight()));
            mediaThumbnail.setAttribute("width", String.valueOf(video.getThumbnails().get(i).getWidth()));
            mediaContent.appendChild(mediaThumbnail);
        }
    }

    public static void initializeDB() throws MalformedURLException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Mysql username: ");
            String username = scanner.nextLine();
            System.out.println("Enter Mysql password: ");
            String password = scanner.nextLine();
            ArrayList<Thumbnail> thumbnails;
            ArrayList<String> tags;
            ArrayList<String> networks;
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/igncodefoo", username, password);
            System.out.println("Database connected");
            Statement stmnt = conn.createStatement();

            //query articles and store data
            String query = "Select * from article";
            ResultSet rset = stmnt.executeQuery(query);
            while (rset.next()) {
                String articleType = String.valueOf(rset.getObject(1));
                String headline = String.valueOf(rset.getObject(2));
                String subHeadline = String.valueOf(rset.getObject(3));
                String publishDate = String.valueOf(rset.getObject(4));
                String state = String.valueOf(rset.getObject(5));
                String slug = String.valueOf(rset.getObject(6));

                //get networks
                networks = getNetworks(conn, headline);
                //get tags
                tags = getTags(conn, headline);
                //get thumbnails
                thumbnails = getThumbnails(conn, headline);
                articles.add(new Article(articleType, headline, subHeadline, publishDate, state,
                        slug, networks, tags, thumbnails));
            }

            //query videos and store data
            query = "Select * from video";
            rset = stmnt.executeQuery(query);
            while (rset.next()) {
                String name = String.valueOf(rset.getObject(1));
                String description = String.valueOf(rset.getObject(2));
                String publishDate = String.valueOf(rset.getObject(3));
                String longTitle = String.valueOf(rset.getObject(4));
                long duration = (long) rset.getObject(5);
                URL videoUrl = new URL(String.valueOf(rset.getObject(6)));
                String slug = String.valueOf(rset.getObject(7));
                String state = rset.getString(8);
                //get networks
                networks = getNetworks(conn, longTitle);
                //get tags
                tags = getTags(conn, longTitle);
                //get thumbnails
                thumbnails = getThumbnails(conn, longTitle);
                //add video to videos
                videos.add(new Video(name, description, publishDate, longTitle, duration, videoUrl,
                        state, slug, networks, tags, thumbnails));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed");
        }
    }

    public static ArrayList<Thumbnail> getThumbnails(Connection conn, String title) throws SQLException, MalformedURLException {
        ArrayList<Thumbnail> thumbnails = new ArrayList<>();
        PreparedStatement thumbQuery = conn.prepareStatement("Select * from thumbnail where title = ?");
        thumbQuery.setString(1, title);
        ResultSet rSet = thumbQuery.executeQuery();
        while (rSet.next()) {
            URL thumbUrl = new URL(String.valueOf(rSet.getObject(2)));
            String size = String.valueOf(rSet.getObject(3));
            long width = (long) rSet.getObject(4);
            long height = (long) rSet.getObject(5);
            thumbnails.add(new Thumbnail(size, thumbUrl, width, height));
        }
        return thumbnails;
    }

    public static ArrayList<String> getNetworks(Connection conn, String title) throws SQLException {
        ArrayList<String> networks = new ArrayList<>();
        PreparedStatement networkQuery = conn.prepareStatement("Select * from network where title = ?");
        networkQuery.setString(1, title);
        ResultSet rSet = networkQuery.executeQuery();
        while (rSet.next()) {
            networks.add(String.valueOf(rSet.getObject(2)));
        }
        return networks;
    }

    public static ArrayList<String> getTags(Connection conn, String title) throws SQLException {
        ArrayList<String> tags = new ArrayList<>();
        PreparedStatement tagQuery = conn.prepareStatement("Select * from tag where title = ?");
        tagQuery.setString(1, title);
        ResultSet rSet = tagQuery.executeQuery();
        while (rSet.next()) {
            tags.add(String.valueOf(rSet.getObject(2)));
        }
        return tags;
    }
}
