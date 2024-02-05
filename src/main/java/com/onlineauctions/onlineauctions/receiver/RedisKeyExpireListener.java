package com.onlineauctions.onlineauctions.receiver;

import com.onlineauctions.onlineauctions.service.auction.AuctionOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {

    private static AuctionOperationService auctionOperationService;

    @Autowired
    public void setObject (AuctionOperationService auctionOperationService){
        RedisKeyExpireListener.auctionOperationService = auctionOperationService;
    }

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.toString();
        if (msg.startsWith("auction:")){
            String auctionId = msg.substring(6);
            auctionOperationService.cancelAuction(auctionId);
            log.info("结束拍卖会：{}",auctionId);
        }
    }
}
