package com.yupi.autoreply.controller;

import com.yupi.autoreply.answerer.Answerer;
import com.yupi.autoreply.factory.AnswererFactory;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 开放接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/")
@Slf4j
public class MainController {

    @ApiOperation(value="读取写操作记录接口", notes="读取写操作记录接口")
    @PostMapping(value = "answer")
    public String answer(String question,HttpServletRequest req) throws Exception {
        Answerer answerer = AnswererFactory.createAnswerer("openai");
        log.info("收到新提问 \n 问题：{}", question);
        // 2. 获取回答
        String answer = answerer.doAnswer(question);
        return answer;
    }
}
