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
import org.springframework.transaction.annotation.Transactional;
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
    private final int PAGE_SIZE = 12;
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

    protected Nft createNftFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int idNft = rs.getInt("nftId");
        String contractAddr = rs.getString("contractAddr");
        String name = rs.getString("nftName");
        String chain = rs.getString("chain");
        int idImage = rs.getInt("idImage");
        int idOwner = rs.getInt("idOwner");
        String collection = rs.getString("collection");
        String description = rs.getString("description");
        Array propertiesArray = rs.getArray("properties");
        String[] properties = null;
        if(propertiesArray != null)
            properties = (String[])propertiesArray.getArray();
        Integer idSellOrder = rs.getInt("sellOrderId");
        if(idSellOrder == 0)
            idSellOrder = null;
        return new Nft(id, idNft, contractAddr, name ,chain, idImage, idOwner, collection, description, properties, idSellOrder);
    }

    @Override
    public Optional<Nft> create(int nftId, String contractAddr, String nftName, String chain, MultipartFile image, int idOwner, String collection, String description, String[] properties) {
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

        Optional<Integer> idImage = imageDao.createImage(image);
        if(!idImage.isPresent())
            return Optional.empty();

        nftData.put("id_image", idImage.get());
        int id = jdbcInsertNft.executeAndReturnKey(nftData).intValue();

        return Optional.of(new Nft(id, nftId, contractAddr, nftName, chain, idImage.get(), idOwner, collection, description, properties, null));
    }

    @Override
    public Optional<Nft> getNFTById(int nftId) {
        return jdbcTemplate.query(SELECT_NFT_QUERY+" WHERE nfts.id=?", new Object[]{nftId}, SELECT_NFT_MAPPER).stream().findFirst();
    }

    protected Pair<StringBuilder, List<Object>> applyFilter(String columnName, String filter) {
        if(filter == null || filter.equals(""))
            return null;
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList<>();
        String[] filterArray = filter.split(",");
        sb.append(" AND ( ");
        for (int i = 0; i < filterArray.length; i++) {
            String aux = filterArray[i].substring(0, 1).toUpperCase() + filterArray[i].substring(1);
            if (i == 0)
                sb.append(String.format(" %s LIKE ? ", columnName));
            else
                sb.append(String.format(" OR %s LIKE ? ", columnName));
            args.add(aux);
        }
        sb.append(") ");
        return new Pair<>(sb, args);
    }

    protected Pair<StringBuilder, List<Object>> buildFilterQuery(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList<>();
        sb.append(" WHERE true ");

        if(status != null && !status.equals("")) {
            String[] statusArray = status.split(",");
            StringBuilder statusAux = new StringBuilder();
            final int[] bothStatus = {0};
            Arrays.stream(statusArray).forEach(s -> {
                if(s.equals("onSale") || s.equals("notSale")) {
                    statusAux.append(String.format(" AND sellorders.id IS %s NULL ", s.equals("onSale") ? "NOT":""));
                    bothStatus[0]++;
                }
            });
            if(bothStatus[0] < 2)
                sb.append(statusAux);
        }

        Pair<StringBuilder, List<Object>> applyCategoryFilter = applyFilter("category", category);
        if(applyCategoryFilter != null) {
            sb.append(applyCategoryFilter.getLeft());
            args.addAll(applyCategoryFilter.getRight());
        }

        Pair<StringBuilder, List<Object>> applyChainFilter = applyFilter("chain", chain);
        if(applyChainFilter != null) {
            sb.append(applyChainFilter.getLeft());
            args.addAll(applyChainFilter.getRight());
        }

        if(minPrice != null && minPrice.compareTo(new BigDecimal(0)) > 0) {
            sb.append(" AND price >= ? ");
            args.add(minPrice);
        }
        if(maxPrice != null && maxPrice.compareTo(new BigDecimal(0)) > 0) {
            sb.append(" AND price <=  ? ");
            args.add(maxPrice);
        }
        switch (sort) {
            case "priceAsc":
                sb.append(" ORDER BY price");
                break;
            case "priceDsc":
                sb.append(" ORDER BY price DESC");
                break;
            case "noSort":
                break;
            default:
                sb.append(" ORDER BY nftName ");
                break;
        }

        return new Pair<>(sb, args);
    }

    protected Pair<StringBuilder, List<Object>> buildQueryPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, User currentUser) {
        StringBuilder sb = new StringBuilder(SELECT_PUBLICATION_QUERY);
        List<Object> args = new ArrayList<>();
        if (currentUser != null)
            args.add(currentUser.getId());
        else
            args.add(0);

        Pair<StringBuilder, List<Object>> filterResults = buildFilterQuery(status, category, chain, minPrice, maxPrice, sort);
        sb.append(filterResults.getLeft());
        args.addAll(filterResults.getRight());
        return new Pair<>(sb, args);
    }

    @Override
    public List<Publication> getAllPublications(int page, String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, User currentUser) {
        Pair<StringBuilder,List<Object>> queryBuilder = buildQueryPublications(status, category, chain, minPrice, maxPrice, sort, currentUser);
        StringBuilder sb = queryBuilder.getLeft();
        List<Object> args = queryBuilder.getRight();

        sb.append(" LIMIT ? OFFSET ? ");
        args.add(PAGE_SIZE);
        args.add((page-1)*PAGE_SIZE);

        List<Publication> result = jdbcTemplate.query(sb.toString(), args.toArray(), SELECT_PUBLICATION_MAPPER);
        result = applySearchAlgorithms(result, search);

        return result;
    }

    protected List<Publication> applySearchAlgorithms(List<Publication> publications, String search) {
        JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();

        if(search != null && !search.equals(""))
            publications = publications.stream().filter(publication -> (jaroWinkler.apply(publication.getNft().getNftName().toLowerCase(), search.toLowerCase()) >= JARO_WINKLER_UMBRAL || calculateDistance(publication.getNft().getNftName().toLowerCase(), search.toLowerCase()) < 4)).collect(Collectors.toList());
        return publications;
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

        return jdbcTemplate.query(sb.toString(), args.toArray(), SELECT_PUBLICATION_MAPPER);
    }

    @Override
    public int getAmountPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String search) {
        Pair<StringBuilder,List<Object>> queryBuilder = buildQueryPublications(status, category, chain, minPrice, maxPrice, "noSort", null);
        StringBuilder sb = queryBuilder.getLeft();
        List<Object> args = queryBuilder.getRight();

        List<Publication> result = jdbcTemplate.query(sb.toString(), args.toArray(), SELECT_PUBLICATION_MAPPER);
        result = applySearchAlgorithms(result, search);
        return result.size();
    }

    @Override
    public void updateOwner(int nftId, int idBuyer) {
        jdbcTemplate.update("UPDATE nfts SET id_owner = ? WHERE id = ?", idBuyer, nftId);
    }

    @Override
    public void delete(int productId) {
        jdbcTemplate.update("DELETE FROM nfts WHERE id=?", productId);
    }

    // Code extracted from https://github.com/crwohlfeil/damerau-levenshtein/blob/master/src/main/java/com/codeweasel/DamerauLevenshtein.java
    protected int calculateDistance(CharSequence source, CharSequence target) {
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

    protected static class Pair<T, U> {
        private final T left;
        private final U right;

        Pair(T left, U right) {
            this.left = left;
            this.right = right;
        }

        public T getLeft() {
            return left;
        }

        public U getRight() {
            return right;
        }
    }
}
