package com.chongge.chatbot.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

@Component
public class StreamAdvisor implements BaseAdvisor {

    private final ChatMemory chatMemory;

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
            StreamAdvisorChain streamAdvisorChain) {
        String requestContent = chatClientRequest.prompt().getContents();

        StringBuilder content = new StringBuilder();
        String conversationId = chatClientRequest.context().get(ChatMemory.CONVERSATION_ID).toString();

        chatMemory.add(conversationId, new AssistantMessage(requestContent));
        return streamAdvisorChain.nextStream(chatClientRequest)
                .doOnNext(response -> {
                    String chunk = response.chatResponse().getResult().getOutput().getText();
                    content.append(chunk);
                })
                .doOnError(error -> {
                    // 处理流中的错误，例如记录错误日志
                    System.err.println("Error in stream: " + error.getMessage());
                })
                .doOnComplete(() -> {
                    chatMemory.add(conversationId, new AssistantMessage(content.toString()));
                });

    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    public StreamAdvisor(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }
}
