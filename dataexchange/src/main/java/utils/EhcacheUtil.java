package utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;

public class EhcacheUtil {
    private static final String path = "../ehcache.xml";
    private URL url;
    private CacheManager manager;
    private static EhcacheUtil ehCache;

    private EhcacheUtil(String path) {
        url = getClass().getResource(path);
        manager = CacheManager.create(url);

    }

    public static EhcacheUtil getInstance() {
        if (ehCache == null) {
            ehCache = new EhcacheUtil(path);
        }
        return ehCache;
    }

    /**
     * 存储信息到缓存
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public void put(String cacheName, String key, Object value) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    /**
     * 得到相应位置的缓存信息
     *
     * @param cacheName
     * @param key
     * @return
     */
    public Object get(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        if (cache == null) {
            return null;
        }
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * 得到cache对象
     *
     * @param cacheName
     * @return
     */
    public Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }

    /**
     * 删除指定位置的缓存
     *
     * @param cacheName
     * @param key
     */
    public void remove(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        cache.remove(key);
    }

    public CacheManager getManager() {
        return manager;
    }

    public void clodeManager() {
        manager.shutdown();
    }
}

