package com.onlineauctions.onlineauctions.service;

import com.onlineauctions.onlineauctions.mapper.AuctionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionMapper auctionMapper;

    private final CargoService cargoService;

    private final UserService userService;
}
