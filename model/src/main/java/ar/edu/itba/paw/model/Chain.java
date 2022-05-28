package ar.edu.itba.paw.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Chain {
    Ethereum,
    BSC,
    Polygon,
    Harmony,
    Solana,
    Ronin,
    Cardano,
    Tezos,
    Avalanche;

    private static final List<String> chains = Arrays.stream(Chain.values()).map(Enum::name).collect(Collectors.toList());

    public static List<String> getChains() {
        return chains;
    }
}
