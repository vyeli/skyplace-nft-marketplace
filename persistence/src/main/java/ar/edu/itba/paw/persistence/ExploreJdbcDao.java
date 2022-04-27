package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.NftCard;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExploreJdbcDao implements ExploreDao{

    private final JdbcTemplate jdbcTemplate;
    private final static Double JARO_WINKLER_UMBRAL = 0.8;

    @Autowired
    public ExploreJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    private List<NftCard> executeSelectNFTQuery(String query, Object[] args) {
        return jdbcTemplate.query(query, args, (rs, i) -> {
            String name = rs.getString("nft_name");
            String contract_addr = rs.getString("contract_addr");
            String chain = rs.getString("chain");
            long id_product = rs.getLong("id_product");
            long id_image = rs.getLong("id_image");
            float price = rs.getFloat("price");
            int score = 0;
            long id_nft = rs.getLong("id_nft");
            String seller_email = rs.getString("seller_email");
            String descr = rs.getString("descr");
            return new NftCard(id_image, name, chain, price, score, seller_email, descr, contract_addr, id_nft, id_product);
        });
    }

    @Override
    public NftCard getNFTById(String id) {
        try {
            long prodId = Long.parseLong(id);
            String selectNFTByIdQuery = "SELECT sellorders.id AS id_product, category, nfts.id AS id_nft, contract_addr, nft_name, id_image, chain, price, descr, seller_email FROM nfts NATURAL JOIN chains INNER JOIN sellorders ON (id_nft = nfts.id AND nft_addr = contract_addr) WHERE sellorders.id=?";
            List<NftCard> r = executeSelectNFTQuery(selectNFTByIdQuery, new Object[]{prodId});
            return r.size() > 0 ? r.get(0):null;
        } catch(NumberFormatException e) {
            return null;
        }
    }

    @Override
    public List<NftCard> getNFTs(int page, String categoryName, String chain, double minPrice, double maxPrice, String sort, String search) {

        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList<>();

        sb.append("SELECT sellorders.id AS id_product, category, nfts.id AS id_nft, contract_addr, nft_name, id_image, chain, price, descr, seller_email FROM nfts NATURAL JOIN chains INNER JOIN sellorders ON (id_nft = nfts.id AND nft_addr = contract_addr) WHERE true ");
        String[] categories = categoryName.split(",");
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

        if(minPrice > 0) {
            sb.append(" AND price >= ? ");
            args.add(minPrice);
        }
        if(maxPrice > 0) {
            sb.append(" AND price <=  ? ");
            args.add(maxPrice);
        }
        switch (sort) {
            case "name":
                sb.append(" ORDER BY nft_name ");
                break;
            case "price_asc":
                sb.append(" ORDER BY price");
                break;
            case "price_dsc":
                sb.append(" ORDER BY price DESC");
                break;
        }

        List<NftCard> result = executeSelectNFTQuery(sb.toString(), args.toArray());
        JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();

        if(search != null){
            result = result.stream().filter(nftCard -> (jaroWinkler.apply(nftCard.getName(), search) >= JARO_WINKLER_UMBRAL || calculateDistance(nftCard.getName(), search) < 4)).collect(Collectors.toList());
        }

        return result;
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
