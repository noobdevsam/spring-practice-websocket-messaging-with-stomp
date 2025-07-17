package com.example.springpracticewebsocketmessagingwithstomp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringPracticeWebsocketMessagingWithStompApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPracticeWebsocketMessagingWithStompApplication.class, args);
    }

}


// Add message body class
class HelloMessage {
    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

// Add greeting response message class
class Greeting {
    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}