package com.berg.ai.application.usecase;

import org.springframework.stereotype.Service;

import com.berg.ai.domain.model.ParsedMessage;
import com.berg.ai.domain.port.in.ParseMessagePort;
import com.berg.ai.domain.port.out.AIProvider;
import com.berg.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParseMessageUseCase implements ParseMessagePort {

    private final AIProvider aiProvider;

    @Override
    public ParsedMessage execute(String message) {
        if (message == null || message.isBlank()) {
            throw new BusinessException("Message must not be blank");
        }
        return aiProvider.parse(message.trim());
    }
}
