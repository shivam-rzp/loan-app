package com.loanapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public  static String convertObjectToString(Object object) {
        String result = null;

        try {
            result = objectMapper.writeValueAsString(object);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static <T> T convertJsonStringToObject(String jsonString, Class<T> clazz)  {

        try {
            return objectMapper.readValue(jsonString, clazz);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
