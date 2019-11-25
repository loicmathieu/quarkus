package io.quarkus.hibernate.orm.panache;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import io.quarkus.panache.common.Page;

/**
 * <p>
 * Interface representing an entity query, which abstracts the use of paging, getting the number of results, and
 * operating on {@link List} or {@link Stream}.
 * </p>
 * <p>
 * Instances of this interface cannot mutate the query itself or its parameters: only paging information can be
 * modified, and instances of this interface can be reused to obtain multiple pages of results.
 * </p>
 *
 * @author Stéphane Épardaud
 * @param <Entity> The entity type being queried
 */
public interface PanacheQuery<Entity> extends Findable<Entity> {

    // Builder

    /**
     * Sets the current page.
     * 
     * @param page the new page
     * @return this query, modified
     * @see #page(int, int)
     * @see #page()
     */
    public <T extends Entity> PanacheQuery<T> page(Page page);

    /**
     * Sets the current page.
     * 
     * @param pageIndex the page index
     * @param pageSize the page size
     * @return this query, modified
     * @see #page(Page)
     * @see #page()
     */
    public <T extends Entity> PanacheQuery<T> page(int pageIndex, int pageSize);

    /**
     * Sets the current page to the next page
     * 
     * @return this query, modified
     * @see #previousPage()
     */
    public <T extends Entity> PanacheQuery<T> nextPage();

    /**
     * Sets the current page to the previous page (or the first page if there is no previous page)
     * 
     * @return this query, modified
     * @see #nextPage()
     */
    public <T extends Entity> PanacheQuery<T> previousPage();

    /**
     * Sets the current page to the first page
     * 
     * @return this query, modified
     * @see #lastPage()
     */
    public <T extends Entity> PanacheQuery<T> firstPage();

    /**
     * Sets the current page to the last page. This will cause reading of the entity count.
     * 
     * @return this query, modified
     * @see #firstPage()
     * @see #count()
     */
    public <T extends Entity> PanacheQuery<T> lastPage();

    /**
     * Returns true if there is another page to read after the current one.
     * This will cause reading of the entity count.
     * 
     * @return true if there is another page to read
     * @see #hasPreviousPage()
     * @see #count()
     */
    public boolean hasNextPage();

    /**
     * Returns true if there is a page to read before the current one.
     * 
     * @return true if there is a previous page to read
     * @see #hasNextPage()
     */
    public boolean hasPreviousPage();

    /**
     * Returns the total number of pages to be read using the current page size.
     * This will cause reading of the entity count.
     * 
     * @return the total number of pages to be read using the current page size.
     */
    public int pageCount();

    /**
     * Returns the current page.
     * 
     * @return the current page
     * @see #page(Page)
     * @see #page(int,int)
     */
    public Page page();

    /**
     * TODO
     * 
     * @param startIdx
     * @param lastIdx
     * @param <T>
     * @return
     */
    public <T extends Entity> Findable<T> range(int startIdx, int lastIdx);

    public <T extends Entity> PanacheQuery<T> withLock(LockModeType lockModeType);

    public <T extends Entity> PanacheQuery<T> withHint(String hintName, Object value);
}
