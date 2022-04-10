package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.NftCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ExploreJdbcDao implements ExploreDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExploreJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    private List<NftCard> executeSelectQuery(String query, Object[] args) {
        return jdbcTemplate.query(query, args, (rs, i) -> {
            String name = rs.getString("nft_name");
            String contract_addr = rs.getString("contract_addr");
            String chain = rs.getString("chain");
            long id_product = rs.getLong("id_product");
            byte[] img = rs.getBytes("img");
            float price = rs.getFloat("price");
            int score = 0;
            long id_nft = rs.getLong("id_nft");
            String seller_email = rs.getString("seller_email");
            String descr = rs.getString("descr");
            return new NftCard(img, name, chain, price, score, seller_email, descr, contract_addr, id_nft, id_product);
        });
    }

    @Override
    public NftCard getNFTById(long id) {
        String selectNFTByIdQuery = "SELECT sellorders.id AS id_product, nfts.id AS id_nft, contract_addr, nft_name, id_category, img, chain, price, descr, seller_email FROM nfts INNER JOIN chains ON nfts.id_chain = chains.id INNER JOIN sellorders ON (id_nft = nfts.id AND nft_addr = contract_addr) WHERE sellorders.id=?";
        List<NftCard> r = executeSelectQuery(selectNFTByIdQuery, new Object[]{id});
        return r.size() > 0 ? r.get(0):null;
    }

    @Override
    public List<NftCard> getNFTs(int page) {
        String selectAllNFTsQuery = "SELECT sellorders.id AS id_product, nfts.id AS id_nft, contract_addr, nft_name, id_category, img, chain, price, descr, seller_email FROM nfts INNER JOIN chains ON nfts.id_chain = chains.id INNER JOIN sellorders ON (id_nft = nfts.id AND nft_addr = contract_addr)";
        return executeSelectQuery(selectAllNFTsQuery, null);
    }
}
