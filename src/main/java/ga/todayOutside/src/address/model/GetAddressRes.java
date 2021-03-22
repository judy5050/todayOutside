package ga.todayOutside.src.address.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GetAddressRes {

//    private  String firstAddressName;
    private Long addressIdx;
    private String secondAddressName;
    private String thirdAddressName;
    private Integer addressOrder;

    public GetAddressRes(Long addressIdx,String thirdAddressName,String secondAddressName,Integer addressOrder){
        this.addressIdx=addressIdx;
        int index=0;
        if(secondAddressName.matches(".*시.*")){
            index=secondAddressName.indexOf("시");
            this.secondAddressName=secondAddressName.substring(index+1);
        }else{
            this.secondAddressName=secondAddressName;
        }
        if(thirdAddressName!=null){


            this.thirdAddressName=thirdAddressName;
        }else{
            this.thirdAddressName="없음";
        }


        this.addressOrder=addressOrder;
    }

}
