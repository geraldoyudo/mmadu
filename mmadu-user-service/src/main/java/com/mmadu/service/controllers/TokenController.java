package com.mmadu.service.controllers;

import com.mmadu.service.entities.AppToken;
import com.mmadu.service.exceptions.TokenNotFoundException;
import com.mmadu.service.service.AppTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private AppTokenService appTokenService;

    @GetMapping("/generate")
    public AppToken generateToken(){
        return appTokenService.generateToken();
    }

    @GetMapping("/retrieve/{tokenId}")
    public AppToken getToken(@PathVariable("tokenId") String tokenId){
        return appTokenService.getToken(tokenId);
    }

    @GetMapping("/reset/{tokenId}")
    public AppToken resetToken(@PathVariable("tokenId") String tokenId){
        return appTokenService.resetToken(tokenId);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleTokenNotFoundException(){

    }
}
