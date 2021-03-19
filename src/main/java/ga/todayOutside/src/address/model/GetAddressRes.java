package ga.todayOutside.src.address.model;

import lombok.Data;

@Data
public class GetAddressRes {

//    private  String firstAddressName;
    private Long addressIdx;
    private String secondAddressName;
    private Integer addressOrder;

    public GetAddressRes(Long addressIdx,String secondAddressName,Integer addressOrder){
        this.addressIdx=addressIdx;
        this.secondAddressName=secondAddressName;
        this.addressOrder=addressOrder;
    }

}
