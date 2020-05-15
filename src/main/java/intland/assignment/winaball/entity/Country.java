package intland.assignment.winaball.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public enum Country {
  GERMANY, HUNGARY;

  private static final Map<String, Country> NAME_MAP;

  static {
    Map<String, Country> nameMap = new HashMap<>();
    for (Country value: values()) {
      nameMap.put(value.name(), value);
    }
    NAME_MAP = Collections.unmodifiableMap(nameMap);
  }

  public static Country byName(String name) {
    return NAME_MAP.get(StringUtils.capitalize(name));
  }
}
