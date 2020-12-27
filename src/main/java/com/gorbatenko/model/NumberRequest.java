package com.gorbatenko.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Class-entity for requests
 * @autor Vladimir Gorbatenko
 * @version 1.0
 */

@Entity(name="number_requests")
@Data
@NoArgsConstructor
public class NumberRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="fact")
    private String fact;

    @Column(name="number")
    private int number;

    @Column(name="latency")
    private Long latency;

    @Column(name="success")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean success = true;
}
