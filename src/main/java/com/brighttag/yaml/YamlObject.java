package com.brighttag.yaml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * An unordered collection of name/value pairs whose external form
 * is a string conforming to the Yaml specification.
 *
 * @author codyaray
 * @since 7/13/12
 */
public class YamlObject extends AbstractHeterogeneousMap {

  public YamlObject() {
    this(ImmutableMap.<String, Object>of());
  }

  @SuppressWarnings("unchecked")
  public YamlObject(InputStream inputStream) {
    this(Objects.firstNonNull(
        (Map<String, Object>) new Yaml().load(inputStream),
        ImmutableMap.<String, Object>of()));
  }

  YamlObject(Map<String, Object> object) {
    super(object);
  }

  private YamlObject(Builder builder) {
    super(builder);
  }

  @Override
  public String toString() {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    Yaml yaml = new Yaml(options);
    StringWriter sw = new StringWriter();
    yaml.dump(asMap(), sw);
    return sw.toString();
  }

  /*
   * Override methods to narrow the exception and return types for Yaml.
   * If Java supported multiple inheritance, could push up to a superclass.
   */

  @Override
  public Object get(String key) throws YamlException {
    try {
      return super.get(key);
    } catch (HeterogeneousException e) {
      throw new YamlException(e);
    }
  }

  @Override
  public String getString(String key) throws YamlException {
    try {
      return super.getString(key);
    } catch (HeterogeneousException e) {
      throw new YamlException(e);
    }
  }

  @Override
  public Boolean getBoolean(String key) throws YamlException {
    try {
      return super.getBoolean(key);
    } catch (HeterogeneousException e) {
      throw new YamlException(e);
    }
  }

  @Override
  public Integer getInt(String key) throws YamlException {
    try {
      return super.getInt(key);
    } catch (HeterogeneousException e) {
      throw new YamlException(e);
    }
  }

  @Override
  public Double getDouble(String key) throws YamlException {
    try {
      return super.getDouble(key);
    } catch (HeterogeneousException e) {
      throw new YamlException(e);
    }
  }

  @Override
  public Long getLong(String key) throws YamlException {
    try {
      return super.getLong(key);
    } catch (HeterogeneousException e) {
      throw new YamlException(e);
    }
  }

  @Override
  public YamlArray getArray(String key) throws YamlException {
    try {
      return new YamlArray(super.getArray(key).asList());
    } catch (HeterogeneousException e) {
      throw new YamlException(e);
    }
  }

  @Override
  public @Nullable YamlArray optArray(String key, @Nullable HeterogeneousArray defaultValue) {
    HeterogeneousArray array = super.optArray(key, defaultValue);
    return (array == null) ? null : new YamlArray(array.asList());
  }

  @Override
  public @Nullable YamlArray optArray(String key) {
    HeterogeneousArray array = super.optArray(key);
    return (array == null) ? null : new YamlArray(array.asList());
  }

  @Override
  public YamlObject getMap(String key) throws YamlException {
    try {
      return new YamlObject(super.getMap(key).asMap());
    } catch (HeterogeneousException e) {
      throw new YamlException(e);
    }
  }

  @Override
  public @Nullable YamlObject optMap(String key, @Nullable HeterogeneousMap defaultValue) {
    HeterogeneousMap object = super.optMap(key, defaultValue);
    return (object == null) ? null : new YamlObject(object.asMap());
  }

  @Override
  public @Nullable YamlObject optMap(String key) {
    HeterogeneousMap object = super.optMap(key);
    return (object == null) ? null : new YamlObject(object.asMap());
  }

  /**
   * @author codyaray
   * @since 7/22/12
   */
  public static class Builder extends AbstractHeterogeneousMap.Builder<Builder> {

    public Builder() {
      super(Builder.class);
    }

    public YamlObject build() {
      return new YamlObject(this);
    }

  }

}
