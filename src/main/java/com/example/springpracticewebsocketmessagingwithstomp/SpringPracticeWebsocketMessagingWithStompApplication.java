package com.example.springpracticewebsocketmessagingwithstomp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
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
    @SendTo("/topic/greetings") // it will be broadcast to all subscribers of the /topic/greetings destination
    public Greeting greeting(HelloMessage helloMessage) throws InterruptedException {
        Thread.sleep(2000); // Simulate a delay

        return new Greeting(
                "Hello, " + HtmlUtils.htmlEscape(helloMessage.getName()) + " !"
        );
    }
}

// Add WebSocket configuration class
@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker
class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple in-memory message broker
        registry.enableSimpleBroker("/topic");

        // Set the application destination prefix
        // for messages that are bound for methods annotated with @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the /ws endpoint, enabling WebSocket connections
        registry.addEndpoint("/ws");
    }
}