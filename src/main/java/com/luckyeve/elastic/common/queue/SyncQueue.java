package com.luckyeve.elastic.common.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lixy on 2016/11/11.
 */
public class SyncQueue<E> {

    private int capacity;

    private List<E> items;

    public SyncQueue(int capacity) {
        this.capacity = capacity;
        items = Collections.synchronizedList(new ArrayList<E>());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    public int free() {
        int free = capacity - items.size();
        return free < 0? 0: free;
    }

    public boolean isFull() {
        if (items.size() < capacity) {
            return false;
        }
        return true;
    }

    public void addNowait(E e) {
        items.add(e);
    }

    public void addAllNowait(List<E> list) {
        items.addAll(list);
    }

    public void addWait(E e) {
        while (isFull()) {
            try {
                Thread.sleep(5L);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        items.add(e);
    }

    public E pull() {
        if (items.isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }
        return items.remove(0);
    }

    public List<E> pullAll() {
        List<E> list = new ArrayList<E>();
        int size = items.size();
        for(int i = 0; i < size && items.size() > 0; i++) {
            list.add(items.remove(0));
        }
        return list;
    }

    public List<E> pullAll(int size) {
        List<E> list = new ArrayList<E>();
        for(int i = 0; i < size && items.size() > 0; i++) {
            list.add(items.remove(0));
        }
        return list;
    }
}
