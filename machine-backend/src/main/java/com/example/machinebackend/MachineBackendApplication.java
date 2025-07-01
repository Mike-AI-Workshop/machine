package com.example.machinebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MachineBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MachineBackendApplication.class, args);
        System.out.println("启动成功");
    }

}
