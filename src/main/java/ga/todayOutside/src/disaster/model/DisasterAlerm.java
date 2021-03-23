package ga.todayOutside.src.disaster.model;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table
public class DisasterAlerm {

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

    @Builder
    public DisasterAlerm(Long id, Long userIdx, String disaster_1, String disaster_2, String disaster_3, String disaster_4, String disaster_5, String disaster_6, String disaster_7, String disaster_8, String disaster_9, String disaster_10, String disaster_11, String disaster_12, String disaster_13, String disaster_14, String disaster_15, String disaster_16, String disaster_17, String disaster_18, String disaster_19, String disaster_20, String disaster_21) {
        this.id = id;
        this.userIdx = userIdx;
        this.disaster_1 = disaster_1;
        this.disaster_2 = disaster_2;
        this.disaster_3 = disaster_3;
        this.disaster_4 = disaster_4;
        this.disaster_5 = disaster_5;
        this.disaster_6 = disaster_6;
        this.disaster_7 = disaster_7;
        this.disaster_8 = disaster_8;
        this.disaster_9 = disaster_9;
        this.disaster_10 = disaster_10;
        this.disaster_11 = disaster_11;
        this.disaster_12 = disaster_12;
        this.disaster_13 = disaster_13;
        this.disaster_14 = disaster_14;
        this.disaster_15 = disaster_15;
        this.disaster_16 = disaster_16;
        this.disaster_17 = disaster_17;
        this.disaster_18 = disaster_18;
        this.disaster_19 = disaster_19;
        this.disaster_20 = disaster_20;
        this.disaster_21 = disaster_21;
    }
}
