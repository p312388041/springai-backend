package com.chongge.chatbot;

import java.io.IOException;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
// import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
// import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class AudioToText {

    public static void main(String[] args) throws IOException {
        // SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // factory.setConnectTimeout(600000); // 连接超时 10分钟
        // factory.setReadTimeout(600000); // 读取超时 10分钟
        // var builder = RestClient.builder().requestFactory(factory);
        // String baseUrl = "http://localhost:8021";
        // OpenAiAudioApi api = OpenAiAudioApi.builder().baseUrl(baseUrl).apiKey("hehe").restClientBuilder(builder).build();
        // OpenAiAudioTranscriptionModel model = new OpenAiAudioTranscriptionModel(api);
        // String filePaht = "d:/test2.mp3";
        // var prompt = new AudioTranscriptionPrompt(new FileSystemResource(filePaht));
        // var content = model.call(prompt);
        // System.out.println(content.getResult().getOutput());
        //简化调用
        // String filePaht = "d:/test.mp3";
        // System.out.println(model.transcribe(new FileSystemResource(filePaht)));
    }
}
