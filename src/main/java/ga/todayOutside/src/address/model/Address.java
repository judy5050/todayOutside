package ga.todayOutside.src.address.model;

import ga.todayOutside.config.BaseEntity;
import ga.todayOutside.src.user.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="address")
public class Address  extends BaseEntity {

    /**
     * 주소 인덱스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="addressIdx")
    private Long id;

    /**
     *도  ex 경기도 ..
     */
    @Column(name = "firstAddressName")
    private String firstAddressName;

    /**
     * 시
     */
    @Column(name = "secondAddressName")
    private String secondAddressName;

    /**
     * 구
     */
    @Column(name="thirdAddressName")
    private String thirdAddressName;




    /**
     * 회원엔티티
     */

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn("userIdx")
//    private UserInfo userInfo;



}
