package ga.todayOutside.src.disaster.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table
@Data
public class DisasterAlarm {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long userIdx;

    @Column(nullable = false)
    private String disaster_1;
    @Column(nullable = false)
    private String disaster_2;
    @Column(nullable = false)
    private String disaster_3;
    @Column(nullable = false)
    private String disaster_4;
    @Column(nullable = false)
    private String disaster_5;
    @Column(nullable = false)
    private String disaster_6;
    @Column(nullable = false)
    private String disaster_7;
    @Column(nullable = false)
    private String disaster_8;
    @Column(nullable = false)
    private String disaster_9;
    @Column(nullable = false)
    private String disaster_10;
    @Column(nullable = false)
    private String disaster_11;
    @Column(nullable = false)
    private String disaster_12;
    @Column(nullable = false)
    private String disaster_13;
    @Column(nullable = false)
    private String disaster_14;
    @Column(nullable = false)
    private String disaster_15;
    @Column(nullable = false)
    private String disaster_16;
    @Column(nullable = false)
    private String disaster_17;
    @Column(nullable = false)
    private String disaster_18;
    @Column(nullable = false)
    private String disaster_19;
    @Column(nullable = false)
    private String disaster_20;
    @Column(nullable = false)
    private String disaster_21;
    @Column(nullable = false)
    private String disaster_22;
    @Column(nullable = false)
    private String disaster_23;

}
