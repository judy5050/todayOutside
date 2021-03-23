package ga.todayOutside.src.disaster;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/disaster-alram")
public class DisasterAlaramController {

    @GetMapping("/{userIdx}")
    public Map<String, Object> getAlramInfo() {


        return new HashMap<>();
    }

}
