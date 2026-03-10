package com.berg.shopping.list.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateListRequest(
        @NotBlank @Size(min = 1, max = 255) String name) {}
