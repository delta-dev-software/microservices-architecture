/*package com.example.notificationservice.openia;

package com.example.notificationservice.openia;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    private final OpenAiService openAiService;

    public OpenAIService(String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    }

    public CompletionResult generateNotificationMessage(String prompt) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .maxTokens(100)
                .build();

        return openAiService.createCompletion(completionRequest);
    }
}*/

