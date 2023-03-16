package com.jsonpath.example.transform;

import com.jsonpath.example.mapper.JsonSmartMappingProvider;
import com.jsonpath.example.mapper.MappingProvider;
import com.jsonpath.example.transform.Configuration.Defaults;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;

import java.util.EnumSet;
import java.util.Set;

public final class DefaultsImpl implements Defaults {

  public static final DefaultsImpl INSTANCE = new DefaultsImpl();

  private final MappingProvider mappingProvider = new JsonSmartMappingProvider();

  @Override
  public JsonProvider jsonProvider() {
    return new JsonSmartJsonProvider();
  }

  @Override
  public Set<Option> options() {
    return EnumSet.noneOf(Option.class);
  }

  @Override
  public MappingProvider mappingProvider() {
    return mappingProvider;
  }

  @Override
  public TransformationProvider transformationProvider() {
    return new JsonPathTransformationProvider();
  }

  private DefaultsImpl() {
  }
}
