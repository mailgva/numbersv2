package com.gorbatenko.repository;

import com.gorbatenko.model.NumberRequest;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class-repository
 * @autor Vladimir Gorbatenko
 * @version 1.0
 */

@Repository
public class NumberRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Method Save request to database
     * Put entity to cache
     */
    @Transactional
    @CachePut(value="numberrequest")
    public void save(NumberRequest numberRequest) {
        em.persist(numberRequest);
    }

    /**
     * Method Get average latency of the Numbers service for all rows. Use only success requests.
     * @return average value
     */
    public BigDecimal getAvgLatency() {
        Query query = em.createNativeQuery("select avg(latency) from number_requests where success = 1");
        return (BigDecimal) query.getSingleResult();
    }

    /**
     * Method Get success rate of the Numbers service for all rows
     * @return calculated value-percent success request
     */
    public BigDecimal getSuccessRate() {
        Query query = em.createNativeQuery(
                "select sum(success) * 100.00 / count(*) from number_requests");
        return (BigDecimal) query.getSingleResult();
    }

    /**
     * Method Get 10 most popular numbers
     * @return Map<Number, Count of requests>
     */
    public Map<Integer, BigInteger> getMostPopular() {
        Query query = em.createNativeQuery(
                "select number, count(*) cnt " +
                         "from number_requests " +
                         "group by number order by cnt desc limit 10");

        Map<Integer, BigInteger> result = new HashMap<>();

        List<Object[]> list = query.getResultList();

        list.stream().forEach(
                (record) -> {
                    Integer number = (Integer) record[0];
                    BigInteger count = (BigInteger) record[1];
                    result.put(number, count);});

        return result;
    }


}
