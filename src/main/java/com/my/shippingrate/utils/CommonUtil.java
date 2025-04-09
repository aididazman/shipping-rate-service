package com.my.shippingrate.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtil {

    public static String generateCacheKey(Object request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Serialize the object to JSON (sorted keys)
            objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

            String jsonString = objectMapper.writeValueAsString(request);

            // Return the hashed value of the serialized string as the cache key
            return hashString(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Error generating cache key", e);
        }
    }

    private static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing string", e);
        }
    }

}
