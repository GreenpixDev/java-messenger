package ru.greenpix.messenger.friends.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageDto<T> implements Serializable {
    private final int pageNumber;
    private final int pageSize;
    private final int numberOfElements;
    private final int totalPages;
    private final long totalElements;
    private final List<T> content;
}
