package com.envite.utils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by banda on 4/26/2017.
 */
public class EVUtils {
    static final Logger logger = LoggerFactory.getLogger(EVUtils.class);
    public static String convertObjectTOJSONString(Object object){
        if(object == null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonGenerationException jgexp) {
            logger.error(" JsonGenerationException ::: " + jgexp.getMessage() + " {} ", jgexp);
        } catch (JsonMappingException jmexp) {
            logger.error(" JsonMappingException ::: " + jmexp.getMessage() + " {} ", jmexp);
        } catch (IOException ioexp) {
            logger.error(" IOException ::: " + ioexp.getMessage() + " {} ", ioexp);
        }
        return null;
    }
}
