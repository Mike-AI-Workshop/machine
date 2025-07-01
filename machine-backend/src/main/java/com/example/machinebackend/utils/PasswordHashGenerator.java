package com.example.machinebackend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        // 1. 创建一个 BCryptPasswordEncoder 实例
        // 默认的强度是 10，对于大多数场景已经足够安全
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 2. 定义你的管理员明文密码
        // 【重要】请在生产环境中使用更复杂的密码
        String plaintextPassword = "admin123";

        // 3. 对明文密码进行哈希加密
        String hashedPassword = passwordEncoder.encode(plaintextPassword);

        // 4. 在控制台打印出生成的哈希值
        System.out.println("Plaintext Password: " + plaintextPassword);
        System.out.println("Hashed Password (for database): " + hashedPassword);

        // 示例输出:
        // Hashed Password (for database): $2a$10$3zZ.N.E.W.u7e5gYp8hG.f7bEwN9q/3iJjX.bC4K/n
    }
}