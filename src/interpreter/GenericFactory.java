package interpreter;

import java.util.*;

public class GenericFactory<Product> {

    private interface Creator<Product> {
        default Product create() {
            return null;
        }
    }

    Map<String, Creator<Product>> map;

    public GenericFactory() {
        map = new HashMap<String, Creator<Product>>();
    }

    public void insertProduct(final String key, final Class<? extends Product> c) {
        map.put(key, new Creator<Product>() {
            @Override
            public Product create() {
                try {
                    return c.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public Product getNewProduct(String key) {
        if (map.containsKey(key))
            return map.get(key).create();
        return null;
    }
}
