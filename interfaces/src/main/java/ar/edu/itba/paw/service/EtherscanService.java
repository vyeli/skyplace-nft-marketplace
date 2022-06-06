package ar.edu.itba.paw.service;

import java.math.BigDecimal;

public interface EtherscanService {
    boolean isTransactionValid(String txHash, String walletBuyer, String walletSeller, BigDecimal price);
}
