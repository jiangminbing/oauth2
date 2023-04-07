package com.example;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangmb
 * @version 1.0.0
 * @date 2023-04-01 12:27
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Test test = new Test();
        test.completion("你是谁");
    }
    private final static String API_KEY = "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";
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build();
    public void completion(String prompt) throws IOException {
        CompletionRequest completionRequest = new CompletionRequest();
        completionRequest.setPrompt(prompt);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        String reqJson = JSON.toJSONString(completionRequest);
        System.out.println("reqJson: " + reqJson);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                // 将 API_KEY 替换成你自己的 API_KEY
                .header("Authorization", "Bearer " + API_KEY)
                .post(RequestBody.create(mediaType, reqJson))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        }
    }
}
