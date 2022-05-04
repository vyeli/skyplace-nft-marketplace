package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.User;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class NftJdbcDao implements NftDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertNft;
    private final ImageDao imageDao;
    private final static Double JARO_WINKLER_UMBRAL = 0.8;
    private final String SELECT_NFT_QUERY =
            "SELECT nfts.id AS id ,nft_id, contract_addr, nft_name, chain, id_image, id_owner, collection, description, properties, sellorders.id AS id_sellorder " +
            "FROM nfts LEFT OUTER JOIN sellorders ON sellorders.id_nft=nfts.id ";

    private final RowMapper<Nft> SELECT_MAPPER = (rs, i) -> {
        long id = rs.getLong("id");
        long idNft = rs.getLong("nft_id");
        String contractAddr = rs.getString("contract_addr");
        String name = rs.getString("nft_name");
        String chain = rs.getString("chain");
        long idImage = rs.getLong("id_image");
        long idOwner = rs.getLong("id_owner");
        String collection = rs.getString("collection");
        String description = rs.getString("description");
        Array propertiesArray = rs.getArray("properties");
        String[] properties = null;
        if(propertiesArray != null)
            properties = (String[])propertiesArray.getArray();
        Long idSellOrder = rs.getLong("id_sellorder");
        if(idSellOrder == 0)
            idSellOrder = null;
        return new Nft(id, idNft, contractAddr, name ,chain, idImage, idOwner, collection, description, properties, idSellOrder);
    };

    @Autowired
    public NftJdbcDao(final DataSource ds, final ImageDao imageDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertNft = new SimpleJdbcInsert(ds)
                .withTableName("nfts")
                .usingGeneratedKeyColumns("id");
        this.imageDao = imageDao;
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
            List<Nft> result = jdbcTemplate.query(SELECT_NFT_QUERY+" WHERE nfts.id=?", new Object[]{nftIdLong}, SELECT_MAPPER);
            return Optional.ofNullable(result.get(0));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Nft>> getAllNFTs(int page, String chain, String search) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList<>();

        sb.append(SELECT_NFT_QUERY).append(" WHERE true ");

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

        List<Nft> result = jdbcTemplate.query(sb.toString(), args.toArray(), SELECT_MAPPER);
        JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();

        if(search != null){
            result = result.stream().filter(nft -> (jaroWinkler.apply(nft.getNftName().toLowerCase(), search.toLowerCase()) >= JARO_WINKLER_UMBRAL || calculateDistance(nft.getNftName().toLowerCase(), search.toLowerCase()) < 4)).collect(Collectors.toList());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<Nft>> getAllNFTsByUser(int page, User user) {
        List<Nft> result = jdbcTemplate.query(SELECT_NFT_QUERY+" WHERE id_owner=?", new Object[]{user.getId()}, SELECT_MAPPER);
        return Optional.ofNullable(result);
    }

    @Override
    public void updateOwner(long nftId, long idBuyer) {
        jdbcTemplate.update("UPDATE nfts SET id_owner = ? WHERE id = ?", idBuyer, nftId);
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
