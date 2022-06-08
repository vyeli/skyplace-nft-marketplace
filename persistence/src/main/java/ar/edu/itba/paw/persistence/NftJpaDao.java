package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class NftJpaDao implements NftDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ImageDao imageDao;

    private static final String SELECT_ID_QUERY = "SELECT nfts.id " +
                                                                "FROM nfts " +
                                                                "LEFT OUTER JOIN sellorders ON nfts.id=sellorders.id_nft " +
                                                                "JOIN users ON id_owner=users.id " +
                                                                "LEFT OUTER JOIN favorited ON favorited.id_nft=nfts.id ";


    @Override
    public Nft create(int nftId, String contractAddr, String nftName, Chain chain, MultipartFile image, User owner, String collection, String description) {
        Image nftImage = imageDao.createImage(image);
        final Nft nft = new Nft(nftId, contractAddr, nftName, chain, nftImage.getIdImage(), collection, description, owner);
        em.persist(nft);
        return nft;
    }

    @Override
    public Optional<Nft> getNFTById(int id) {
        Nft nft = em.find(Nft.class, id);
        return nft == null || nft.isDeleted() ? Optional.empty() : Optional.of(nft);
    }

    protected Pair<Pair<StringBuilder,StringBuilder>, Pair<String,Object>> applyFilter(String columnNativeName, String paramName, String filter) {
        if(filter == null || filter.equals(""))
            return null;
        StringBuilder nativeQuery = new StringBuilder();
        StringBuilder hqlQuery = new StringBuilder();
        Pair<String, Object> arg = null;
        String[] filterArray = filter.split(",");
        nativeQuery.append(" AND ( ");
        for (int i = 0; i < filterArray.length; i++) {
            nativeQuery.append(String.format("%s LOWER(%s) LIKE LOWER(:%s) ", i == 0 ? "" : "OR", columnNativeName, paramName));
            arg = new Pair<>(paramName, filter);
        }
        nativeQuery.append(") ");
        return new Pair<>(new Pair<>(nativeQuery, hqlQuery), arg);
    }

    protected Pair<Pair<StringBuilder,StringBuilder>,List<Pair<String,Object>>> buildFilterQuery(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, String searchFor) {
        StringBuilder nativeQuery = new StringBuilder();
        StringBuilder hqlQuery = new StringBuilder();
        List<Pair<String,Object>> args = new ArrayList<>();
        nativeQuery.append(" WHERE nfts.is_deleted = false ");

        if(status != null && !status.equals("")) {
            String[] statusArray = status.split(",");
            StringBuilder statusNativeAux = new StringBuilder();
            final int[] bothStatus = {0};
            Arrays.stream(statusArray).forEach(s -> {
                if(s.equals("onSale") || s.equals("notSale")) {
                    statusNativeAux.append(String.format(" AND sellorders.id IS %s NULL ", s.equals("onSale") ? "NOT":""));
                    bothStatus[0]++;
                }
            });
            if(bothStatus[0] < 2) {
                nativeQuery.append(statusNativeAux);
            }
        }

        Pair<Pair<StringBuilder,StringBuilder>,Pair<String,Object>> applyCategoryFilter = applyFilter("sellorders.category", "category", category); // tirara null exception?
        if(applyCategoryFilter != null) {
            nativeQuery.append(applyCategoryFilter.getLeft().getLeft());
            args.add(applyCategoryFilter.getRight());
        }

        Pair<Pair<StringBuilder,StringBuilder>,Pair<String,Object>> applyChainFilter = applyFilter("nfts.chain", "chain", chain);
        if(applyChainFilter != null) {
            nativeQuery.append(applyChainFilter.getLeft().getLeft());
            args.add(applyChainFilter.getRight());
        }

        if(minPrice != null && minPrice.compareTo(BigDecimal.ZERO) > 0) {
            nativeQuery.append(" AND sellorders.price >= :minPrice ");
            args.add(new Pair<>("minPrice", minPrice));
        }
        if(maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0) {
            nativeQuery.append(" AND sellorders.price <=  :maxPrice ");
            args.add(new Pair<>("maxPrice", minPrice));
        }

        if(search != null && !search.equals("")) {
            if(searchFor != null && searchFor.equals("collection"))
                nativeQuery.append(" AND LOWER(nfts.collection) LIKE LOWER(:search) ");
            else
                nativeQuery.append(" AND LOWER(nfts.nft_name) LIKE '%'||LOWER(:search)||'%' ");
            args.add(new Pair<>("search", search));
        }

        switch (sort) { // ordenara bien esto?
            case "priceAsc":
                nativeQuery.append(" AND sellorders.id IS NOT NULL ");
                hqlQuery.append(" ORDER BY nft.sellorder.price ");
                nativeQuery.append(" ORDER BY sellorders.price ");
                break;
            case "priceDsc":
                nativeQuery.append(" AND sellorders.id IS NOT NULL ");
                hqlQuery.append(" ORDER BY nft.sellorder.price DESC ");
                nativeQuery.append(" ORDER BY sellorders.price DESC ");
                break;
            case "noSort":
                break;
            case "collection":
                hqlQuery.append(" ORDER BY nft.collection ");
                nativeQuery.append(" ORDER BY nfts.collection ");
                break;
            default:
                hqlQuery.append(" ORDER BY nft.nftName ");
                nativeQuery.append(" ORDER BY nfts.nft_name ");
                break;
        }

        return new Pair<>(new Pair<>(nativeQuery, hqlQuery), args);
    }

    @Override
    public List<Nft> getAllPublications(int page, int pageSize, String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, String searchFor) {
        Pair<Pair<StringBuilder,StringBuilder>,List<Pair<String,Object>>> filterQuery = buildFilterQuery(status, category, chain, minPrice, maxPrice, sort, search, searchFor);
        return executeQueries(filterQuery, pageSize, page);
    }

    private Pair<Pair<StringBuilder,StringBuilder>, List<Pair<String,Object>>> buildFilterQueryByUser(User user, boolean onlyFaved, boolean onlyOnSale, String sort) {
        StringBuilder hqlQuery = new StringBuilder();
        StringBuilder nativeQuery = new StringBuilder();
        List<Pair<String,Object>> args = new ArrayList<>();
        nativeQuery.append(" WHERE nfts.is_deleted = false ");
        if(!onlyFaved) {
            nativeQuery.append(" AND nfts.id_owner=:idOwner ");
            args.add(new Pair<>("idOwner",user.getId()));
        } else {
            nativeQuery.append(" AND favorited.user_id=:idUser ");
            args.add(new Pair<>("idUser",user.getId()));
        }
        if(onlyOnSale)
            nativeQuery.append(" AND sellorders.id IS NOT NULL ");

        switch (sort) {
            case "priceAsc":
                hqlQuery.append(" ORDER BY nft.sellorder.price ");
                nativeQuery.append(" ORDER BY sellorders.price ");
                break;
            case "priceDsc":
                hqlQuery.append(" ORDER BY nft.sellorder.price DESC ");
                nativeQuery.append(" ORDER BY sellorders.price DESC ");
                break;
            case "noSort":
                break;
            default:
                hqlQuery.append(" ORDER BY nft.nftName ");
                nativeQuery.append(" ORDER BY nfts.nft_name ");
                break;
        }

        return new Pair<>(new Pair<>(nativeQuery, hqlQuery), args);
    }

    @Override
    public List<Nft> getAllPublicationsByUser(int page, int pageSize, User user, boolean onlyFaved, boolean onlyOnSale, String sort) {
        Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery = buildFilterQueryByUser(user, onlyFaved, onlyOnSale, sort);
        return executeQueries(filterQuery, pageSize, page);
    }

    private List<Nft> executeQueries(Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery, int pageSize, int page) {
        String nativeQuery = filterQuery.getLeft().getLeft().toString();
        String hqlQuery = filterQuery.getLeft().getRight().toString();
        List<Pair<String,Object>> args = filterQuery.getRight();

        if(page <= 0)
            page = 1;
        final Query idQuery = em.createNativeQuery(SELECT_ID_QUERY.concat(nativeQuery).concat(" LIMIT :pageSize OFFSET :pageOffset"));
        idQuery.setParameter("pageSize", pageSize);
        idQuery.setParameter("pageOffset", pageSize*(page-1));
        for(Pair<String,Object> arg:args)
            idQuery.setParameter(arg.getLeft(), arg.getRight());

        @SuppressWarnings("unchecked")
        final List<Integer> ids = (List<Integer>) idQuery.getResultList().stream().collect(Collectors.toList());

        if(ids.size() == 0)
            return Collections.emptyList();

        final TypedQuery<Nft> query = em.createQuery("FROM Nft AS nft WHERE nft.id IN :ids ".concat(hqlQuery), Nft.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    private List<Integer> getIds(Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery) {
        String nativeQuery = filterQuery.getLeft().getLeft().toString();
        List<Pair<String,Object>> args = filterQuery.getRight();
        final Query countQuery = em.createNativeQuery(SELECT_ID_QUERY.concat(nativeQuery));
        for(Pair<String,Object> arg:args)
            countQuery.setParameter(arg.getLeft(), arg.getRight());

        @SuppressWarnings("unchecked")
        final List<Integer> ids = (List<Integer>) countQuery.getResultList().stream().collect(Collectors.toList());
        return ids;
    }

    @Override
    public int getAmountPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, String searchFor) {
        Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery = buildFilterQuery(status, category, chain, minPrice, maxPrice, sort, search, searchFor);
        return getIds(filterQuery).size();
    }

    @Override
    public int getAmountPublicationPagesByUser(int pageSize, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale) {
        Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery = buildFilterQueryByUser(user, onlyFaved, onlyOnSale, "noSort");
        return (getIds(filterQuery).size()-1)/pageSize+1;
    }

    @Override
    public Optional<Nft> getNftByPk(int nftContractId, String contractAddr, String chain) {
        final TypedQuery<Nft> query = em.createQuery("FROM Nft nft WHERE nft.nftId = :nftId AND lower(nft.contractAddr) = lower(:contractAddr) AND nft.chain = :chain", Nft.class);
        query.setParameter("nftId", nftContractId);
        query.setParameter("contractAddr", contractAddr);
        query.setParameter("chain", Chain.valueOf(chain));
        return query.getResultList().stream().findFirst();
    }

    @Override
    public boolean isNftCreated(int nftId, String contractAddr, String chain) {
        if(!Chain.getChains().contains(chain))
            return false;
        Optional<Nft> maybeNft = getNftByPk(nftId, contractAddr, chain);
        return maybeNft.isPresent() && !maybeNft.get().isDeleted();
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
