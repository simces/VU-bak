package com.photo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryWithCountDTO {

    private String name;
    private int photoCount;
}


