package com.yupi.autoreply.answerer;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.yupi.autoreply.api.openai.OpenAiApi;
import com.yupi.autoreply.api.openai.model.CreateCompletionRequest;
import com.yupi.autoreply.api.openai.model.CreateCompletionResponse;
import com.yupi.autoreply.common.ErrorCode;
import com.yupi.autoreply.config.OpenAiConfig;
import com.yupi.autoreply.config.ZsxqConfig;
import com.yupi.autoreply.exception.BusinessException;
import com.yupi.autoreply.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class OpenAiAnswerer implements Answerer {

    private final OpenAiConfig openAiConfig = SpringContextUtils.getBean(OpenAiConfig.class);

    @Override
    public String doAnswer(String prompt) {
        CreateCompletionRequest request = new CreateCompletionRequest();
        request.setPrompt(prompt);
        request.setModel(openAiConfig.getModel());
        request.setTemperature(0);
        request.setMax_tokens(1024);
        CreateCompletionResponse response = createCompletion(request, openAiConfig.getApiKey());
//        CreateCompletionResponse response = openAiApi.createCompletion(request, openAiConfig.getApiKey());
        List<CreateCompletionResponse.ChoicesItem> choicesItemList = response.getChoices();
        String answer = choicesItemList.stream()
                .map(CreateCompletionResponse.ChoicesItem::getText)
                .collect(Collectors.joining());
        log.info("OpenAiAnswerer 回答成功 \n 答案：{}", answer);
        return answer;
    }
    public CreateCompletionResponse createCompletion(CreateCompletionRequest request, String openAiApiKey) {
        if (StringUtils.isBlank(openAiApiKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未传 openAiApiKey");
        }
        String url = "https://api.openai.com/v1/completions";
        String json = JSONUtil.toJsonStr(request);
        String result = HttpRequest.post(url)
                .header("Authorization", "Bearer " + openAiApiKey)
                .body(json)
                .execute()
                .body();
        return JSONUtil.toBean(result, CreateCompletionResponse.class);
    }
}
