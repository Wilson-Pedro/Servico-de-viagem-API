package com.wamk.uber.dtos.records;

import com.wamk.uber.enums.roles.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
