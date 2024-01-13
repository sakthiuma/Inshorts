# Inshorts: news summary app using Java and Swing GUI

A maven application built using Swing GUI, providing users with summarized information on different news genres using JSoup library for fetching, parsing, and scrapping news articles across the internet.

# Techonologies used: 
  1. Java 8 - for the backend
  2. Swing GUI - for building the interactive UI
  3. SQLite db - Database for storing the tables and records
  4. JSoup - for fetching the news articles from the internet
  5. JFreeChart - for building the graphs for User analytics

# Features in this application:
 1. New users can sign up. The password is hashed and is stored in the database
 2. If an existing user tries to sign up (same user name), the message is shown to prompt the user to log in instead
 3. If a user forgets their password, forgot password option is provided
 4. When creating a new user creds, the password and confirm password fields should match
 5. On successful signup or login, the user is displayed with the UI where he can choose the genre to see the news.
      1. Currently the focus is only on 3 available genres. Initially, it takes a few minutes to crawl the site and hence you will see a delay. The user gets   
          displayed 4 news at a time.
      2. The refresh option allows you to fetch new news articles. If there are no new articles the contents on the page do not change.
      3. Users can choose to mark certain news for reading later or now.
      4. If the user wants to read the news now, a new window opens up to show the entire news article.
      5. In case there are videos in the particular link, rendering this page will take some time
      6. If the user marks an article to read later, it will be shown in his profile dashboard
  6. Each user gets their own profile dashboard, where they can see the articles saved for later and a report showing how many articles they have read in each genre

# Setup:
Eclipse installation:
1. Import the unzipped file as a Maven project
2. We have included all the jars in the libs folder. Add the respective jars to the build path
3. Right-click on each jar file and click on Build Path -> add to path
4. The jars that are included are Jsoup, Sqlite, JFreechart, JCommons
5. The start point of the application is by running either the SignUp.java  or the LogIn.java
6. We are using SQLite db and have included the respective jars.
7. If you have a Mac laptop, please use sqlite-jdbc-3.34.0.jar instead. We identified only this version of SQLite works



