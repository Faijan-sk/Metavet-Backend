package com.example.demo.Config;
import java.util.Base64;
import java.security.SecureRandom;

public class SecretKeyGenerator {
    
    public static void main(String[] args) {
        // Generate Access Token Secret (256-bit)
        String accessSecret = generateSecretKey(32); // 32 bytes = 256 bits
        System.out.println("Access Token Secret Key:");
        System.out.println(accessSecret);
        System.out.println();
        
        // Generate Refresh Token Secret (256-bit)
        String refreshSecret = generateSecretKey(32); // 32 bytes = 256 bits
        System.out.println("Refresh Token Secret Key:");
        System.out.println(refreshSecret);
        System.out.println();
        
        System.out.println("Copy these to your application.properties:");
        System.out.println("security.jwt.secret-key=" + accessSecret);
        System.out.println("security.jwt.refresh-secret-key=" + refreshSecret);
    }
    
    private static String generateSecretKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[length];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}