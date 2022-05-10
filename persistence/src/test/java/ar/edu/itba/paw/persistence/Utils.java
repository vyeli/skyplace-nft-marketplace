package ar.edu.itba.paw.persistence;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final String BUYORDER_TABLE = "buyorders";
    public static final String SELLORDER_TABLE = "sellorders";
    public static final String NFT_TABLE = "nfts";
    public static final String USER_TABLE = "users";
    public static final String IMAGE_TABLE = "images";
    public static final String FAVORITE_TABLE = "favorited";
    public static final String PURCHASE_TABLE = "purchases";

    public static Map<String, Object> createUserData(int userId) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", userId);
        userData.put("username", "User");
        userData.put("wallet", "0xUser");
        userData.put("email", String.format("%d@USER",userId));
        userData.put("password", "USER");
        userData.put("wallet_chain", "Ethereum");
        userData.put("role", "User");

        return userData;
    }

    public static Map<String, Object> createSellOrderData(int sellOrderId, int nftId) {
        Map<String, Object> sellOrderData = new HashMap<>();

        sellOrderData.put("id", sellOrderId);
        sellOrderData.put("price", new BigDecimal(3));
        sellOrderData.put("id_nft", nftId);
        sellOrderData.put("category", "Other");

        return sellOrderData;
    }

    public static Map<String, Object> createNftData(int nftId, int imageId, int ownerId) {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("id", nftId);
        nftData.put("nft_id", nftId);
        nftData.put("contract_addr", String.format("0x%d",nftId));
        nftData.put("chain", "Ethereum");
        nftData.put("id_image", imageId);
        nftData.put("id_owner", ownerId);
        nftData.put("collection", "collection");
        nftData.put("description", "description");
        nftData.put("properties", null);

        return nftData;
    }

    public static Map<String, Object> createImageData(int imageId) {
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("id_image", imageId);
        imageData.put("image", new byte[1]);

        return imageData;
    }

    public static Map<String, Object> createFavoritedData(int userId, int nftId) {
        Map<String, Object> favoritedData = new HashMap<>();
        favoritedData.put("user_id", userId);
        favoritedData.put("id_nft", nftId);

        return favoritedData;
    }

    public static Map<String, Object> createPurchaseData(int nftId, int buyerId, int sellerId) {
        Map<String, Object> purchasesData = new HashMap<>();
        purchasesData.put("id_nft", nftId);
        purchasesData.put("id_buyer", buyerId);
        purchasesData.put("price", new BigDecimal(5));
        purchasesData.put("id_seller", sellerId);
        purchasesData.put("buy_date", new Timestamp(LocalTime.now().getNano()));

        return purchasesData;
    }
}
