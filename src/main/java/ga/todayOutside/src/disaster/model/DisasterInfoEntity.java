package ga.todayOutside.src.disaster.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "Disaster")
public class DisasterInfoEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "city",nullable = false)
    private String city;

    @Column(name = "msg",nullable = false)
    private String msg;

    @Column(name = "createDate",nullable = false)
    private String createDate;

    @Column(name = "msgIdx",nullable = false)
    private Long msgIdx;

    @Column(name = "kind", nullable = false)
    private String kind;

    @Builder
    public DisasterInfoEntity(Long id, String state, String city,
                              String msg, String createDate,
                              Long msgIdx, String kind) {

        this.id = id;
        this.state = state;
        this.city = city;
        this.msg = msg;
        this.createDate = createDate;
        this.msgIdx = msgIdx;
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "DisasterInfoEntity{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", msg='" + msg + '\'' +
                ", createDate='" + createDate + '\'' +
                ", msgIdx=" + msgIdx +
                '}';
    }
}
