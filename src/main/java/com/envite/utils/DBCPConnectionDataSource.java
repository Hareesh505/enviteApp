package com.envite.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class DBCPConnectionDataSource extends org.apache.tomcat.jdbc.pool.DataSource{

    final Logger logger = LoggerFactory.getLogger(DBCPConnectionDataSource.class);

    public static final String MYSQL_ADDRESS_PROP_NAME ="JDBC_CONNECTION_STRING";
    public static final String JDBC_MAX_ACTIVE_CONNECTIONS ="JDBC_MAX_ACTIVE_CONNECTIONS";

    private static final String URL = "jdbc:mysql://172.16.11.247:3306/envite?user=adminHF3HitN&password=bhWDcIIxajAQ&useUnicode=true&characterEncoding=utf-8";

    public void configureUrl() {
        super.setUrl(URL);
        String configUrl = System.getProperty(MYSQL_ADDRESS_PROP_NAME);
        if(!StringUtils.isEmpty(configUrl)){
            super.setUrl(configUrl);
        }
        String maxActiveConnections = System.getProperty(JDBC_MAX_ACTIVE_CONNECTIONS);
        if(!StringUtils.isEmpty(maxActiveConnections)){
            try{
                logger.info("Setting mysql max active connections to "+maxActiveConnections);
                super.setMaxActive(new Integer(maxActiveConnections).intValue());
            } catch (NumberFormatException e){
                logger.error("Could not parse JDBC_MAX_ACTIVE_CONNECTIONS property with value : \""+maxActiveConnections+"\"", e);
                super.setMaxActive(100);
                logger.info("Setting mysql max active connections to default value.");
            }
        } else {
            super.setMaxActive(100);
        }
    }
}

