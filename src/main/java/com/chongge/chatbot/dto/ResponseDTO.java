package com.chongge.chatbot.dto;

import java.util.List;
import java.util.Map;

public record ResponseDTO(String logId, Result result, Integer errorCode, String errorMsg) {
    public record Result(List<LayoutParsingResult> layoutParsingResults, DataInfo dataInfo) {}

    public record LayoutParsingResult(Map<String, Object> prunedResult, Markdown markdown, Map<String, String> outputImages, String inputImage) {}

    public record Markdown(String text, Map<String, String> images) {}

    public record DataInfo(Integer width, Integer height, String type) {}
}
