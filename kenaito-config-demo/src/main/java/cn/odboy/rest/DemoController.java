package cn.odboy.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo")
public class DemoController {

    @PostMapping("/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.ok("success");
    }
}
