package org.example;

import java.sql.*;

public class Database
{
    String url;
    String user;
    String password;

    public Database(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public ResultSet getData(String sql)
    {
        ResultSet rs = null;

        try
        {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            st.close();
        }
        catch (Exception e)
        {
            System.out.println("Error connecting to the database");
            System.out.println(e.getMessage());
        }

        return rs;
    }

    public int executeQuery(String sql)
    {
        int result = 0;

        try
        {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
            Statement st = conn.createStatement();
            result = st.executeUpdate(sql);
            st.close();
        }

        catch (Exception e)
        {
            System.out.println("Error connecting to the database");
            System.out.println(e.getMessage());
        }
        return result;
    }
}

