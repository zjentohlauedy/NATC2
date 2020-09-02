package org.natc.app.service.search;

import java.util.List;

public interface NATCSearchService<T, E> {
    List<T> fetchAll(E request);
}
