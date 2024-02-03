package com.onlineauctions.onlineauctions.pojo.request;

import com.onlineauctions.onlineauctions.pojo.user.balance.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletInfo {

    private boolean success;
    private String message;
    private Wallet wallet;
}
