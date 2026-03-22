package com.berg.shopping.list.domain.port.in;

import com.berg.shopping.list.application.command.CheckItemCommand;
import com.berg.shopping.list.domain.model.ShoppingListItem;

public interface CheckItemPort {

    ShoppingListItem execute(CheckItemCommand command);
}
