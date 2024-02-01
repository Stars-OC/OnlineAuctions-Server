package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/auction")
@RequiredArgsConstructor
@Validated
public class AuctionController {

    private final AuctionService auctionService;
}
