package ga.todayOutside.src.FCM;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FCMController {
    @Autowired
    FirebaseCloudMessageService firebaseCloudMessageService;

    @GetMapping("/FCM")
    public void test(@RequestParam String title, @RequestParam String body) throws IOException {
        String token = "dk2Byh05Qcq9URB9RWyVrh:APA91bGWivS8vx01z4X1WEX5w_AHHaZhtgA-76aTxrB3GDfU1WRglS9SDp54byUBBXKzNGxwKJ5jLlaie4DCbMFDhZuu_-KoNCBi6Hojbq9_RFq7F2gNBEtIIUhVE89XlQKYaDiI1RiH";
        firebaseCloudMessageService.sendMessageTo(token, title, body);
    }
}
