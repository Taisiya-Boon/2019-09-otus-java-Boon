package ru.otus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

  private final WeakHashMap<K, V> cache = new WeakHashMap<>();
  private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

  @Override
  public void put(K key, V value) {
    cache.put(key, value);
    observer(key, value, "put");
  }

  @Override
  public void remove(K key) {
    observer(key, cache.get(key), "remove");
    cache.remove(key);
  }

  @Override
  public V get(K key) {
    return cache.get(key);
  }

  public ArrayList<K> getKeys() {
    return new ArrayList(cache.keySet());
  }

  public long getIn() {
    return cache.size();
  }

  private void observer(K key, V value, String action) {
    for (WeakReference<HwListener<K, V>> listener : listeners) {
      var ref = listener.get();
      if (ref != null) {
        ref.notify(key, value, action);
      }
    }
  }

  @Override
  public void addListener(HwListener<K, V> listener) {
    listeners.add(new WeakReference<>(listener));
  }

  @Override
  public void removeListener(HwListener<K, V> listener) {
    listeners.removeIf(listenerWeakReference -> listenerWeakReference.get() == listener);
    listeners.removeIf(listenerWeakReference -> listenerWeakReference.get() == null);
  }
}
