package org.natc.app.service;

import java.util.List;

public interface NATCService<T, E> {
    List<T> fetchAll(E request);
}
