package org.natc.natc.service;

import java.util.List;

public interface NATCService<T, E> {
    T fetch(E request);
    List<T> fetchAll(E request);
}
