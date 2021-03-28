package ga.todayOutside.src.disaster.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table
@Data
@DynamicInsert
public class DisasterAlarm {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long userIdx;

    @Column()
    private String disaster_1;
    @Column()
    private String disaster_2;
    @Column()
    private String disaster_3;
    @Column()
    private String disaster_4;
    @Column()
    private String disaster_5;
    @Column()
    private String disaster_6;
    @Column()
    private String disaster_7;
    @Column()
    private String disaster_8;
    @Column()
    private String disaster_9;
    @Column()
    private String disaster_10;
    @Column()
    private String disaster_11;
    @Column()
    private String disaster_12;
    @Column()
    private String disaster_13;
    @Column()
    private String disaster_14;
    @Column()
    private String disaster_15;
    @Column()
    private String disaster_16;
    @Column()
    private String disaster_17;
    @Column()
    private String disaster_18;
    @Column()
    private String disaster_19;
    @Column()
    private String disaster_20;
    @Column()
    private String disaster_21;
    @Column()
    private String disaster_22;
    @Column()
    private String disaster_23;

}
