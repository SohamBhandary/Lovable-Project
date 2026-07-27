package com.Soham.Intelligence_Service.LLms;


import com.Soham.Common_Lib.Enums.ChatEventStatus;
import com.Soham.Common_Lib.Enums.ChatEventType;
import com.Soham.Intelligence_Service.Entities.ChatEvent;
import com.Soham.Intelligence_Service.Entities.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class LlmResponseParser {

    // Relaxes closing constraint using an optional lookahead or direct match to handle streaming cutoffs
    private static final Pattern GENERIC_TAG_PATTERN = Pattern.compile(
            "(<(message|file|tool)([^>]*)>)([\\s\\S]*?)(</\\2>|$)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile(
            "(path|args)=\"([^\"]+)\""
    );

    public List<ChatEvent> parseChatEvents(String fullResponse, ChatMessage parentMessage) {
        List<ChatEvent> events = new ArrayList<>();
        if (fullResponse == null || fullResponse.isBlank()) return events;

        int orderCounter = 1;
        Matcher matcher = GENERIC_TAG_PATTERN.matcher(fullResponse);

        while (matcher.find()) {
            String tagName = matcher.group(2).toLowerCase();
            String attributes = matcher.group(3);
            String content = matcher.group(4).trim();

            Map<String, String> attrMap = extractAttributes(attributes);

            ChatEvent.ChatEventBuilder builder = ChatEvent.builder()
                    .status(ChatEventStatus.CONFIRMED)
                    .chatMessage(parentMessage)
                    .content(content)
                    .sequenceOrder(orderCounter++);

            switch (tagName) {
                case "message" -> builder.type(ChatEventType.MESSAGE);
                case "file" -> {
                    builder.type(ChatEventType.FILE_EDIT);
                    builder.filePath(attrMap.get("path"));
                }
                case "tool" -> {
                    builder.type(ChatEventType.TOOL_LOG);
                    builder.status(ChatEventStatus.PENDING);
                    builder.metadata(attrMap.get("args"));
                }
                default -> {
                    continue;
                }
            }

            events.add(builder.build());
        }

        return events;
    }

    private Map<String, String> extractAttributes(String attributeString) {
        Map<String, String> attributes = new HashMap<>();
        if (attributeString == null) return attributes;

        Matcher matcher = ATTRIBUTE_PATTERN.matcher(attributeString);
        while (matcher.find()) {
            attributes.put(matcher.group(1), matcher.group(2));
        }
        return attributes;
    }
}