package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.service.auction.AuctionOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/auction/operation")
@RequiredArgsConstructor
@Validated
public class AuctionOperationController {

    private final AuctionOperationService auctionOperationService;
}
