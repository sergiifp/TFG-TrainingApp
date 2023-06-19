package com.example.TFGtraining.SessionResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/trainingsessions/{id}/sessionresult")
public class SessionResultController {

    @Autowired
    private SessionResultService sessionResultService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSessionResult(@PathVariable Long id, @RequestBody Map<String,String> body) throws ParseException {
        LocalTime finalTime = null;
        if (body.get("finalTime") != null) {
            finalTime = LocalTime.parse(body.get("finalTime"));
        }
        Integer km = null;
        if (body.get("finalKm") != null){
            km = Integer.parseInt(body.get("finalKm"));
        }
        sessionResultService.createSessionResult(id,km,finalTime);
    }

}
