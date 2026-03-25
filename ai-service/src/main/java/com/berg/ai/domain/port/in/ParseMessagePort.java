package com.berg.ai.domain.port.in;

import com.berg.ai.domain.model.ParsedMessage;

public interface ParseMessagePort {
    ParsedMessage execute(String message);
}
