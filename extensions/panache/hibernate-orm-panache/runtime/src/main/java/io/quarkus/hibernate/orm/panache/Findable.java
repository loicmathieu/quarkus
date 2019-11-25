package io.quarkus.hibernate.orm.panache;

import io.quarkus.panache.common.Page;

import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.stream.Stream;

public interface Findable<Entity> {
    // Results

    /**
     * Reads and caches the total number of entities this query operates on. This causes a database
     * query with <code>SELECT COUNT(*)</code> and a query equivalent to the current query, minus
     * ordering.
     *
     * @return the total number of entities this query operates on, cached.
     */
    public long count();

    /**
     * Returns the current page of results as a {@link List}.
     *
     * @return the current page of results as a {@link List}.
     * @see #stream()
     * @see #page(Page)
     * @see #page()
     */
    public <T extends Entity> List<T> list();

    /**
     * Returns the current page of results as a {@link Stream}.
     *
     * @return the current page of results as a {@link Stream}.
     * @see #list()
     * @see #page(Page)
     * @see #page()
     */
    public <T extends Entity> Stream<T> stream();

    /**
     * Returns the first result of the current page index. This ignores the current page size to fetch
     * a single result.
     *
     * @return the first result of the current page index, or null if there are no results.
     * @see #singleResult()
     */
    public <T extends Entity> T firstResult();

    /**
     * Executes this query for the current page and return a single result.
     *
     * @return the single result (throws if there is not exactly one)
     * @throws NoResultException if there is no result
     * @throws NonUniqueResultException if there are more than one result
     * @see #firstResult()
     */
    public <T extends Entity> T singleResult();
}
