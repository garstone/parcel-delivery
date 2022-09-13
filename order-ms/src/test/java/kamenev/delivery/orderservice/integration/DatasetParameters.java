package kamenev.delivery.orderservice.integration;

import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.*;

public final class DatasetParameters {
    public final UUID id1 = UUID.fromString("89d77319-a775-457f-b447-76bfa62ef502");
    public final UUID id2 = UUID.fromString("aaf63d4e-4d81-4621-b66e-de730f921453");

    public static Map<String, String> extractDataSetSubstitutions() {
        DatasetParameters p = new DatasetParameters();
        ImmutableMap.Builder<String, String> b = ImmutableMap.builder();

        Arrays.stream(DatasetParameters.class.getDeclaredFields()).forEach(
                f -> {
                    try {
                        f.setAccessible(true);
                        String name = f.getName();
                        Object value = f.get(p);
                        if (List.class.isAssignableFrom(f.getType())) {
                            @SuppressWarnings("unchecked")
                            List<Object> values = (List<Object>) f.get(p);
                            Iterator<Object> it = values.iterator();
                            for (int i = 1; i <= values.size(); i++) {
                                b.put(name + "." + i, it.next().toString());
                            }
                        } else {
                            b.put(name, value.toString());
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error get dataset field parameter value for field " + f, e);
                    }
                }
        );
        return b.build();
    }
}
