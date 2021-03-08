package ga.todayOutside.src.disaster;

import ga.todayOutside.src.user.UserInfoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/disaster")
public class DisasterController {
    private final DisasterService disasterService;

    @Autowired
    public DisasterController(DisasterService disasterService) {
        this.disasterService = disasterService;
    }

    /**
     * 재난 정보 조회 api
     */
    @ResponseBody
    @GetMapping("/info")
    public Map<String, Object> getInfomation() {
        Map<String, Object> result = new HashMap<String, Object>();

        result = disasterService.getImfomation();
        return result;
    }
}
