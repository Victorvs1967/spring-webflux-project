package com.vvs.springwebfluxproject.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DataMapper {
  
  private ModelMapper modelMapper = new ModelMapper();

  public <T, R> R convert(T item, Class<R> typeParameterClass) {
    return modelMapper
      .map(item, typeParameterClass);
  }

  public <T, R> List<R> convertToList(List<T> list, Class<R> typeParameterClass) {
    return list.stream()
      .map(item -> modelMapper
        .map(item, typeParameterClass))
      .collect(Collectors.toList());
  }
}
