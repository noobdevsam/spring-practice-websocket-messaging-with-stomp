package com.example.springpracticewebsocketmessagingwithstomp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

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

// Add controller class
@Controller
class GreetingController {

    @MessageMapping("/hello") //ensures that, if a message is sent to the /hello destination
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage helloMessage) throws InterruptedException {
        Thread.sleep(2000); // Simulate a delay

        return new Greeting(
                "Hello, " + HtmlUtils.htmlEscape(helloMessage.getName()) + " !"
        );
    }
}