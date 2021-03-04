package ga.todayOutside.src.address.model;

import lombok.Data;

@Data
public class GetAddressRes {

    private  String firstAddressName;
    private String secondAddressName;
    private String thirdAddressName;
    private Long addressIdx;

    public GetAddressRes(Long addressIdx,String firstAddressName,String secondAddressName, String thirdAddressName){
        this.addressIdx=addressIdx;
        this.firstAddressName=firstAddressName;
        this.secondAddressName=secondAddressName;
        this.thirdAddressName=thirdAddressName;
    }

}
