package server;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 4:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cache {
    HashMap<String, byte[]> c;
    int size;
    int max_size;

    public Cache(int max_size) {
        this.c = new HashMap<String, byte[]>();
        this.max_size = max_size;
        this.size = 0;
    }

    public byte[] get(String key) {
        if (c.containsKey(key)) {
            return c.get(key);
        } else {
            return null;
        }
    }

    public void put(String key, byte[] value) {
        if (value.length + size <= max_size) {
            c.put(key, value);
            size += value.length;
            System.err.println("Put something in the cache");
        }
        else {
            System.err.println("Cache was full! " + size + " " + value.length + " " + max_size);
        }
    }
}
