package com.yupi.autoreply.answerer;

import com.yupi.autoreply.api.openai.OpenAiApi;
import com.yupi.autoreply.api.openai.model.CreateCompletionRequest;
import com.yupi.autoreply.api.openai.model.CreateCompletionResponse;
import com.yupi.autoreply.config.OpenAiConfig;
import com.yupi.autoreply.config.ZsxqConfig;
import com.yupi.autoreply.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OpenAi 回答者
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Slf4j
@Service
public class OpenAiAnswerer implements Answerer {
    @Autowired
    private  OpenAiApi openAiApi;

    private final OpenAiConfig openAiConfig = SpringContextUtils.getBean(OpenAiConfig.class);

    @Override
    public String doAnswer(String prompt) {
        CreateCompletionRequest request = new CreateCompletionRequest();
        request.setPrompt(prompt);
        request.setModel(openAiConfig.getModel());
        request.setTemperature(0);
        request.setMax_tokens(1024);
        CreateCompletionResponse response = openAiApi.createCompletion(request, openAiConfig.getApiKey());
        System.out.println(response);
        List<CreateCompletionResponse.ChoicesItem> choicesItemList = response.getChoices();

        System.out.println(openAiConfig.getApiKey());
        System.out.println(openAiConfig.getModel());
        System.out.println(choicesItemList.size());

        for (CreateCompletionResponse.ChoicesItem a:choicesItemList
             ) {
            log.info(a.getText());
        }
        String answer = choicesItemList.stream()
                .map(CreateCompletionResponse.ChoicesItem::getText)
                .collect(Collectors.joining());
        log.info("OpenAiAnswerer 回答成功 \n 答案：{}", answer);
        return answer;
    }
}
