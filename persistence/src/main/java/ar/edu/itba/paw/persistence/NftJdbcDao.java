package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class NftJdbcDao implements NftDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertNft;
    private final ImageDao imageDao;
    private final static Double JARO_WINKLER_UMBRAL = 0.8;
    private final String NFT_SQL_VARIABLES = " nfts.id AS id , nft_id AS nftId, contract_addr AS contractAddr, nft_name AS nftName, chain, id_image AS idImage, id_owner AS idOwner, collection, description, properties, sellorders.id AS sellOrderId ";
    private final String SELECT_NFT_QUERY =
            "SELECT " +
                    NFT_SQL_VARIABLES +
            " FROM nfts LEFT OUTER JOIN sellorders ON sellorders.id_nft=nfts.id ";

    private final String SELECT_PUBLICATION_QUERY =
                    "SELECT " +
                    NFT_SQL_VARIABLES +
                    "            ,price," +
                    "            category," +
                    "            username," +
                    "            email," +
                    "            CASE WHEN favorited.user_id IS NULL THEN false ELSE true END AS isFaved" +
                    "        FROM" +
                    "            nfts" +
                    "            LEFT OUTER JOIN sellorders" +
                    "                ON nfts.id=sellorders.id_nft" +
                    "            JOIN users" +
                    "                ON id_owner=users.id" +
                    "            LEFT OUTER JOIN favorited" +
                    "                ON (favorited.user_id=? AND favorited.id_nft=nfts.id) ";


    private final RowMapper<Publication> SELECT_PUBLICATION_MAPPER = (rs, i) -> {
        Nft nft = createNftFromResultSet(rs);
        SellOrder sellOrder = null;
        if(nft.getSellOrder() != null) {
            BigDecimal price = rs.getBigDecimal("price");
            String category = rs.getString("category");
            sellOrder = new SellOrder(nft.getSellOrder(), price, nft.getNftId(), category);
        }
        User user = new User(nft.getIdOwner(), rs.getString("email"));
        boolean isFaved = rs.getBoolean("isFaved");
        return new Publication(nft, sellOrder, user, isFaved);
    };
    private final RowMapper<Nft> SELECT_NFT_MAPPER = (rs, i) -> createNftFromResultSet(rs);
    @Autowired
    public NftJdbcDao(final DataSource ds, final ImageDao imageDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertNft = new SimpleJdbcInsert(ds)
                .withTableName("nfts")
                .usingGeneratedKeyColumns("id");
        this.imageDao = imageDao;
    }

    private Nft createNftFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        long idNft = rs.getLong("nftId");
        String contractAddr = rs.getString("contractAddr");
        String name = rs.getString("nftName");
        String chain = rs.getString("chain");
        long idImage = rs.getLong("idImage");
        long idOwner = rs.getLong("idOwner");
        String collection = rs.getString("collection");
        String description = rs.getString("description");
        Array propertiesArray = rs.getArray("properties");
        String[] properties = null;
        if(propertiesArray != null)
            properties = (String[])propertiesArray.getArray();
        Long idSellOrder = rs.getLong("sellOrderId");
        if(idSellOrder == 0)
            idSellOrder = null;
        return new Nft(id, idNft, contractAddr, name ,chain, idImage, idOwner, collection, description, properties, idSellOrder);
    }

    @Override
    public Optional<Nft> create(long nftId, String contractAddr, String nftName, String chain, MultipartFile image, long idOwner, String collection, String description, String[] properties) {
        List<String> chains = jdbcTemplate.query("SELECT chain FROM chains", (rs, i) -> rs.getString("chain"));
        if(!chains.contains(chain))
            return Optional.empty();

        final Map<String, Object> nftData = new HashMap<>();
        nftData.put("nft_id", nftId);
        nftData.put("contract_addr",contractAddr);
        nftData.put("nft_name", nftName);
        nftData.put("chain", chain);
        nftData.put("id_owner", idOwner);
        nftData.put("collection", collection);
        nftData.put("description", description);

        Optional<Long> idImage = imageDao.createImage(image);
        if(!idImage.isPresent())
            return Optional.empty();

        nftData.put("id_image", idImage.get());
        long id = jdbcInsertNft.executeAndReturnKey(nftData).longValue();

        return Optional.of(new Nft(id, nftId, contractAddr, nftName, chain, idImage.get(), idOwner, collection, description, properties, null));
    }

    @Override
    public Optional<Nft> getNFTById(String nftId) {
        try {
            long nftIdLong = Long.parseLong(nftId);
            List<Nft> result = jdbcTemplate.query(SELECT_NFT_QUERY+" WHERE nfts.id=?", new Object[]{nftIdLong}, SELECT_NFT_MAPPER);
            return Optional.ofNullable(result.get(0));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Publication> getAllPublications(int page, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, User currentUser) {
        StringBuilder sb = new StringBuilder(SELECT_PUBLICATION_QUERY);
        List<Object> args = new ArrayList<>();
        if (currentUser != null)
            args.add(currentUser.getId());
        else
            args.add(0);
        sb.append(" WHERE true ");
        String[] categories = category.split(",");
        StringBuilder categoryFilter = new StringBuilder();
        List<Object> categoryArgs = new ArrayList<>();
        boolean allNfts = false;
        categoryFilter.append(" AND ( ");
        for(int i = 0; i < categories.length; i++) {
            if(categories[i].equals("all")) {
                allNfts = true;
                break;
            }
            String categoryAux = categories[i].substring(0,1).toUpperCase()+categories[i].substring(1);
            if(i == 0)
                categoryFilter.append(" category LIKE ? ");
            else
                categoryFilter.append(" OR category LIKE ? ");
            categoryArgs.add(categoryAux);
        }
        categoryFilter.append(") ");
        if(!allNfts) {
            sb.append(categoryFilter);
            args.addAll(categoryArgs);
        }

        String[] chains = chain.split(",");
        StringBuilder chainFilter = new StringBuilder();
        List<Object> chainArgs = new ArrayList<>();
        boolean allChains = false;

        chainFilter.append(" AND ( ");
        for(int i = 0; i < chains.length; i++) {
            if(chains[i].equals("all")) {
                allChains = true;
                break;
            }
            String chainAux = chains[i].substring(0,1).toUpperCase()+chains[i].substring(1);
            if(i == 0)
                chainFilter.append(" chain LIKE ? ");
            else
                chainFilter.append(" OR chain LIKE ? ");
            chainArgs.add(chainAux);
        }
        chainFilter.append(") ");

        if(!allChains) {
            sb.append(chainFilter);
            args.addAll(chainArgs);
        }

        if(minPrice.compareTo(new BigDecimal(0)) > 0) {
            sb.append(" AND price >= ? ");
            args.add(minPrice);
        }
        if(maxPrice.compareTo(new BigDecimal(0)) > 0) {
            sb.append(" AND price <=  ? ");
            args.add(maxPrice);
        }
        switch (sort) {
            case "name":
                sb.append(" ORDER BY nftName ");
                break;
            case "priceAsc":
                sb.append(" ORDER BY price");
                break;
            case "priceDsc":
                sb.append(" ORDER BY price DESC");
                break;
        }

        List<Publication> result = jdbcTemplate.query(sb.toString(), args.toArray(), SELECT_PUBLICATION_MAPPER);
        JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();

        if(search != null){
            result = result.stream().filter(publication -> (jaroWinkler.apply(publication.getNft().getNftName().toLowerCase(), search.toLowerCase()) >= JARO_WINKLER_UMBRAL || calculateDistance(publication.getNft().getNftName().toLowerCase(), search.toLowerCase()) < 4)).collect(Collectors.toList());
        }

        return result;
    }

    @Override
    public List<Publication> getAllPublicationsByUser(int page, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale) {
        StringBuilder sb = new StringBuilder(SELECT_PUBLICATION_QUERY);
        List<Object> args = new ArrayList<>();
        args.add(currentUser != null ? currentUser.getId():0);
        sb.append(" WHERE TRUE ");
        if(!onlyFaved) {
            sb.append(" AND id_owner=? ");
            args.add(user.getId());
        } else
            sb.append(" AND favorited.user_id IS NOT NULL ");
        if(onlyOnSale)
            sb.append(" AND sellorders.id IS NOT NULL ");

        System.out.println(sb);
        return jdbcTemplate.query(sb.toString(), args.toArray(), SELECT_PUBLICATION_MAPPER);
    }

    @Override
    public void updateOwner(long nftId, long idBuyer) {
        jdbcTemplate.update("UPDATE nfts SET id_owner = ? WHERE id = ?", idBuyer, nftId);
    }

    @Override
    public void delete(String productId) {
        try {
            long productIdLong = Long.parseLong(productId);
            jdbcTemplate.update("DELETE FROM nfts WHERE id=?", productIdLong);
        } catch(Exception ignored){}
        }

    // Code extracted from https://github.com/crwohlfeil/damerau-levenshtein/blob/master/src/main/java/com/codeweasel/DamerauLevenshtein.java
    private int calculateDistance(CharSequence source, CharSequence target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Parameter must not be null");
        }
        int sourceLength = source.length();
        int targetLength = target.length();
        if (sourceLength == 0) return targetLength;
        if (targetLength == 0) return sourceLength;
        int[][] dist = new int[sourceLength + 1][targetLength + 1];
        for (int i = 0; i < sourceLength + 1; i++) {
            dist[i][0] = i;
        }
        for (int j = 0; j < targetLength + 1; j++) {
            dist[0][j] = j;
        }
        for (int i = 1; i < sourceLength + 1; i++) {
            for (int j = 1; j < targetLength + 1; j++) {
                int cost = source.charAt(i - 1) == target.charAt(j - 1) ? 0 : 1;
                dist[i][j] = Math.min(Math.min(dist[i - 1][j] + 1, dist[i][j - 1] + 1), dist[i - 1][j - 1] + cost);
                if (i > 1 &&
                        j > 1 &&
                        source.charAt(i - 1) == target.charAt(j - 2) &&
                        source.charAt(i - 2) == target.charAt(j - 1)) {
                    dist[i][j] = Math.min(dist[i][j], dist[i - 2][j - 2] + cost);
                }
            }
        }
        return dist[sourceLength][targetLength];
    }
}
