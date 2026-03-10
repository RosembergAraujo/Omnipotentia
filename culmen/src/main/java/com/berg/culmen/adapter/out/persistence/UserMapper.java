package com.berg.culmen.adapter.out.persistence;

import com.berg.culmen.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain(UserJpaEntity entity);

    UserJpaEntity toEntity(User user);
}
