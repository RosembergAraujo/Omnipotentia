package com.berg.ai.domain.port.out;

import com.berg.ai.domain.model.ParsedMessage;

public interface AIProvider {
    ParsedMessage parse(String message);
}
