package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai;

import com.itsallbinary.tutorial.ai.spring_ai_app.common.CommonHelper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.ai.openai.metadata.audio.OpenAiAudioSpeechResponseMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
public class Tutorial_11_0_TextToSpeech {

    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;


    public Tutorial_11_0_TextToSpeech(OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    @GetMapping(CommonHelper.URL_PREFIX_FOR_SPRING + "tutorial/11")
    ResponseEntity<InputStreamResource> generation(String userInput) {

        var speechOptions = OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.CORAL)
                .input(userInput)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f)
                .model("gpt-4o-mini-tts")
                .build();

        var speechPrompt = new SpeechPrompt("Speak like a cheerful and happy host of a concert", speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);

        // Accessing metadata (rate limit info)
        OpenAiAudioSpeechResponseMetadata metadata = response.getMetadata();

        byte[] responseAsBytes = response.getResult().getOutput();

        // Convert byte[] to InputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(responseAsBytes);

        // Return the MP3 byte array as a stream to the client
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"output.mp3\"")
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(new InputStreamResource(inputStream));
    }
}
