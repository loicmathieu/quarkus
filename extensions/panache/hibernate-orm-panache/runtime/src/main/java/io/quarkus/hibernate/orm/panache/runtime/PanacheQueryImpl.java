package io.quarkus.hibernate.orm.panache.runtime;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import io.quarkus.hibernate.orm.panache.Findable;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

public class PanacheQueryImpl<Entity> implements PanacheQuery<Entity> {

    private Query jpaQuery;
    private Object paramsArrayOrMap;
    private String query;
    private EntityManager em;

    /*
     * We store the pageSize and apply it for each request because getFirstResult()
     * sets the page size to 1
     */
    private Page page;
    private Long count;

    private Range range;

    PanacheQueryImpl(EntityManager em, javax.persistence.Query jpaQuery, String query, Object paramsArrayOrMap) {
        this.em = em;
        this.jpaQuery = jpaQuery;
        this.query = query;
        this.paramsArrayOrMap = paramsArrayOrMap;
        page = new Page(0, Integer.MAX_VALUE);
    }

    // Builder

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> PanacheQuery<T> page(Page page) {
        this.page = page;
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> page(int pageIndex, int pageSize) {
        return page(Page.of(pageIndex, pageSize));
    }

    @Override
    public <T extends Entity> PanacheQuery<T> nextPage() {
        return page(page.next());
    }

    @Override
    public <T extends Entity> PanacheQuery<T> previousPage() {
        return page(page.previous());
    }

    @Override
    public <T extends Entity> PanacheQuery<T> firstPage() {
        return page(page.first());
    }

    @Override
    public <T extends Entity> PanacheQuery<T> lastPage() {
        return page(page.index(pageCount() - 1));
    }

    @Override
    public boolean hasNextPage() {
        return page.index < (pageCount() - 1);
    }

    @Override
    public boolean hasPreviousPage() {
        return page.index > 0;
    }

    @Override
    public int pageCount() {
        long count = count();
        if (count == 0)
            return 1; // a single page of zero results
        return (int) Math.ceil((double) count / (double) page.size);
    }

    @Override
    public Page page() {
        return page;
    }

    @Override
    public <T extends Entity> Findable<T> range(int startIdx, int lastIdx) {
        this.range = Range.of(startIdx, lastIdx);
        return (Findable<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> withLock(LockModeType lockModeType) {
        // TODO maybe enfore that this method should be used before calling the first page
        jpaQuery.setLockMode(lockModeType);
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> withHint(String hintName, Object value) {
        // TODO maybe enfore that this method should be used before calling the first page
        jpaQuery.setHint(hintName, value);
        return (PanacheQuery<T>) this;
    }

    // Results

    @Override
    @SuppressWarnings("unchecked")
    public long count() {
        if (count == null) {
            // FIXME: this is crude but good enough for a first version
            String lcQuery = query.toLowerCase();
            int orderByIndex = lcQuery.lastIndexOf(" order by ");
            if (orderByIndex != -1)
                query = query.substring(0, orderByIndex);
            Query countQuery = em.createQuery("SELECT COUNT(*) " + query);
            if (paramsArrayOrMap instanceof Map)
                JpaOperations.bindParameters(countQuery, (Map<String, Object>) paramsArrayOrMap);
            else
                JpaOperations.bindParameters(countQuery, (Object[]) paramsArrayOrMap);
            count = (Long) countQuery.getSingleResult();
        }
        return count;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> List<T> list() {
        manageOffsets();
        return jpaQuery.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> Stream<T> stream() {
        manageOffsets();
        return jpaQuery.getResultStream();
    }



    @Override
    public <T extends Entity> T firstResult() {
        manageOffsets(1);
        List<T> list = list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T singleResult() {
        manageOffsets(page.size);
        return (T) jpaQuery.getSingleResult();
    }

    private void manageOffsets() {
        if(range != null){
            jpaQuery.setFirstResult(range.startIdx);
            jpaQuery.setMaxResults(range.lastIdx - range.startIdx);
        }
        else {
            jpaQuery.setFirstResult(page.index * page.size);
            jpaQuery.setMaxResults(page.size);
        }
    }

    private void manageOffsets(int maxResults) {
        if(range != null){
            jpaQuery.setFirstResult(range.startIdx);
            jpaQuery.setMaxResults(maxResults);
        }
        else {
            jpaQuery.setFirstResult(page.index * page.size);
            jpaQuery.setMaxResults(maxResults);
        }
    }


    private static class Range {
        private int startIdx;
        private int lastIdx;

        private Range(int startIdx, int lastIdx){
            this.startIdx = startIdx;
            this.lastIdx = lastIdx;
        }

        private static Range of(int startIdx, int lastIdx){
            return new Range(startIdx, lastIdx);
        }
    }
}
