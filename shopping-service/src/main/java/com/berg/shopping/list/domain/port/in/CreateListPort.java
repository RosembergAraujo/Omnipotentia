package com.berg.shopping.list.domain.port.in;

import com.berg.shopping.list.application.command.CreateListCommand;
import com.berg.shopping.list.domain.model.ShoppingList;

public interface CreateListPort {

    ShoppingList execute(CreateListCommand command);
}
