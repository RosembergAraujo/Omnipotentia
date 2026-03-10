package com.berg.shopping.list.domain.port.in;

import com.berg.shopping.list.application.command.AddItemCommand;
import com.berg.shopping.list.domain.model.ShoppingListItem;

public interface AddItemToListPort {

    ShoppingListItem execute(AddItemCommand command);
}
