package cn.granitech.web.controller;

import cn.granitech.business.service.TransactionTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/8/21 11:17
 * @Version: 1.0
 */

@RestController
public class TransactionTestController {

    @Autowired
    TransactionTestService ttService;

    @RequestMapping("/tt1")
    public ResponseEntity<String> tt1() {
        ttService.test1();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("/tt2")
    public ResponseEntity<String> tt2() {
        ttService.test2();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("/tt3")
    public ResponseEntity<String> tt3() {
        ttService.test3();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("/tt4")
    public ResponseEntity<String> tt4() {
        ttService.test4();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
