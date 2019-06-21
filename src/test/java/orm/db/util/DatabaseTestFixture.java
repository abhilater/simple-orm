package orm.db.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by abhishek on 21/06/19.
 */
public class DatabaseTestFixture {
    private static Logger LOG = LoggerFactory.getLogger(DatabaseTestFixture.class);

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            LOG.info("JDBC Driver loaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFixture(String queryFilePath) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement st = conn.createStatement()) {

            for (String sql : getSQLQueries(queryFilePath)) {
                if (StringUtils.isNotBlank(sql.trim())) {
                    st.executeUpdate(sql);
                    LOG.debug("Executed >> " + sql);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String[] getSQLQueries(String queryFilePath) throws Exception {
        String str;
        StringBuilder sb = new StringBuilder();
        ClassLoader classLoader = DatabaseTestFixture.class.getClassLoader();
        File file = new File(classLoader.getResource(queryFilePath).getFile());

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        }

        // here is our splitter ! We use ";" as a delimiter for each request
        return sb.toString().split(";");
    }
}