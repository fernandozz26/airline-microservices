package com.apirest.apirest.service;

import java.util.*;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class MessageError {
    String code;
    List<Map<String,String>> messages;
}
