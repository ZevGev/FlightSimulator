package server_side;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


public class FileCacheManager<Problem, Solution> implements CacheManager<Problem, Solution> {

    HashMap<Problem, Solution> disc;
    Properties properties;

    @SuppressWarnings("unchecked")
    public FileCacheManager() {
        properties = new Properties();
        String name = "hash2.properties";

        try {
            properties.load(new FileInputStream(name));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.disc = new HashMap<>();
        if (properties != null) {
            Enumeration<?> E = properties.propertyNames();

            while (E.hasMoreElements()) {
                Problem key = (Problem) E.nextElement();
                if (key != null)
                    this.disc.put(key, (Solution) properties.get(key));
            }
        }

    }

    @Override
    public Boolean Check(final Problem in) {
        if (disc.isEmpty()) {
            return false;
        }
        return disc.containsKey(in);
    }

    @Override
    public Solution Extract(final Problem in) {
        return disc.get(in);
    }

    @Override
    public void Save(final Problem in, final Solution out) {
        disc.put(in, out);
        properties.putAll(this.disc);
        String name = "hash2.properties";

        try {
            properties.store(new FileOutputStream(name), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
