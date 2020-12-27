package com.gorbatenko.web;

import com.gorbatenko.model.NumberRequest;
import com.gorbatenko.repository.NumberRepository;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Rest-Controller
 * @autor Vladimir Gorbatenko
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/api/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
public class NumberController {

    /** List of available types */
    private static List<String> types = List.of("trivia", "math", "date", "year");

    private NumberRepository numberRepository;

    @Value("${numbers.api.url}")
    private String numberServiceUrl;

    public NumberController(NumberRepository numberRepository) {
        this.numberRepository = numberRepository;
    }

    @Cacheable(value="request")
    @GetMapping("cachefact/{number}")
    public Map<String, String> getCacheNumberFact(@PathVariable int number) throws IOException {
        return getNumberFact(number);
    }

    @Cacheable(value="request")
    @GetMapping("cachefact/{number}/{type}")
    public Map<String, String> getCacheNumberFactByType(@PathVariable int number,
                                                        @PathVariable String type) throws IOException {
        return getNumberFactByType(number, type);
    }

    @GetMapping("fact/{number}")
    public Map<String, String> getNumberFact(@PathVariable int number) throws IOException {
        String fact = getFact(number, null);
        return wrap("fact", fact);
    }

    @GetMapping("fact/{number}/{type}")
    public Map<String, String> getNumberFactByType(@PathVariable int number,
                                                   @PathVariable String type) throws IOException {
        if(!types.contains(type)) {
            return wrap("error", "Type is incorrect! Choose one from " + types);
        }
        String fact = getFact(number, type);
        return wrap("fact", fact);
    }

    @GetMapping("mostpopular/")
    public Map<String, Map<Integer, BigInteger>> getMostPopular() {
        return new HashMap<>(){{put("mostpopular", numberRepository.getMostPopular());}};
    }

    @GetMapping("successrate/")
    public Map<String, String> getSuccessRate()  {
        return wrap("successrate", numberRepository.getSuccessRate());
    }

    @GetMapping("avglatency/")
    public Map<String, String> getAvgLatency()  {
        return wrap("avglatency", numberRepository.getAvgLatency());
    }

    /**
     * Method Wrapper for result
     * @return ready Map-answer
     */
    private Map<String, String> wrap(String key, Object value) {
        return new HashMap<>(){{put(key, String.valueOf(value));}};
    }

    /**
     * Method Send request to Number servise and get fact. Calculating sent request milliseconds
     * and persist to database
     * @return recived fact
     */

    private String getFact(int number, String type) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        StopWatch watch = new StopWatch();

        Request request = new Request.Builder()
                .url(numberServiceUrl + number + (type == null ? "" : "/" + type))
                .build();

        NumberRequest numberRequest = new NumberRequest();
        numberRequest.setNumber(number);

        String fact = null;

        try {
            watch.start();
            response = client.newCall(request).execute();
            fact = response.body().string();
            numberRequest.setFact(fact);
        } catch (IOException e) {
            numberRequest.setSuccess(false);
            return String.format("Error getting fact by number %d", number);
        } finally {
            watch.stop();
            numberRequest.setLatency(watch.getTotalTimeMillis());
            numberRepository.save(numberRequest);
        }

        return fact;
    }



}
