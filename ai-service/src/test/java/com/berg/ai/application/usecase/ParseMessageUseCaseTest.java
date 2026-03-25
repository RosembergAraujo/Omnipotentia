package com.berg.ai.application.usecase;

import com.berg.ai.domain.model.IntentType;
import com.berg.ai.domain.model.ParsedMessage;
import com.berg.ai.domain.port.out.AIProvider;
import com.berg.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParseMessageUseCaseTest {

    @Mock
    private AIProvider aiProvider;

    @InjectMocks
    private ParseMessageUseCase useCase;

    @Test
    void shouldDelegateToProviderAndReturnResult() {
        ParsedMessage expected = ParsedMessage.builder()
                .intent(IntentType.SHOW_LIST)
                .items(List.of())
                .priceInfo(null)
                .build();
        when(aiProvider.parse("mostra minha lista")).thenReturn(expected);

        ParsedMessage result = useCase.execute("mostra minha lista");

        assertThat(result.getIntent()).isEqualTo(IntentType.SHOW_LIST);
        verify(aiProvider).parse("mostra minha lista");
    }

    @Test
    void shouldTrimMessageBeforePassingToProvider() {
        ParsedMessage expected = ParsedMessage.unknown();
        when(aiProvider.parse("olá")).thenReturn(expected);

        useCase.execute("  olá  ");

        verify(aiProvider).parse("olá");
    }

    @Test
    void shouldThrowBusinessExceptionForBlankMessage() {
        assertThatThrownBy(() -> useCase.execute("   "))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void shouldThrowBusinessExceptionForNullMessage() {
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(BusinessException.class);
    }
}
