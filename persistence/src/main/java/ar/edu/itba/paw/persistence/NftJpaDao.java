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
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class NftJpaDao implements NftDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ImageDao imageDao;

    private static final String COUNT_ID_QUERY = "SELECT COUNT(nfts.id) " +
            "FROM nfts " +
            "LEFT OUTER JOIN sellorders ON nfts.id=sellorders.id_nft " +
            "JOIN users ON id_owner=users.id ";
    private static final String SELECT_ID_QUERY = "SELECT nfts.id " +
                                                                "FROM nfts " +
                                                                "LEFT OUTER JOIN sellorders ON nfts.id=sellorders.id_nft " +
                                                                "JOIN users ON id_owner=users.id ";

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
        List<String> filters = new ArrayList<>();
        nativeQuery.append(" AND ( ");
        for (int i = 0; i < filterArray.length; i++) {
            nativeQuery.append(String.format(" %s LOWER(%s) LIKE LOWER(:%s) ", i == 0 ? "" : "OR", columnNativeName, paramName.concat(String.valueOf(i))));
            filters.add(filterArray[i]);
            if (i==filterArray.length-1)
                arg = new Pair<>(paramName, filters);
        }

        nativeQuery.append(") ");
        return new Pair<>(new Pair<>(nativeQuery, hqlQuery), arg);
    }

    protected Pair<Pair<StringBuilder,StringBuilder>,List<Pair<String,Object>>> buildFilterQuery(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String search, String searchFor) {
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

        return new Pair<>(new Pair<>(nativeQuery, hqlQuery), args);
    }

    @Override
    public List<Nft> getAllPublications(int page, int pageSize, String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, String searchFor) {
        Pair<Pair<StringBuilder,StringBuilder>,List<Pair<String,Object>>> filterQuery = buildFilterQuery(status, category, chain, minPrice, maxPrice, search, searchFor);
        return executeQueries(filterQuery, pageSize, page, sort);
    }

    private Pair<Pair<StringBuilder,StringBuilder>, List<Pair<String,Object>>> buildFilterQueryByUser(User user, boolean onlyFaved, boolean onlyOnSale) {
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

        return new Pair<>(new Pair<>(nativeQuery, hqlQuery), args);
    }

    private Pair<String, String> applySort(String sort) {
        switch (sort) {
            case "priceAsc":
                return new Pair<>(" ORDER BY sellorders.price ", " ORDER BY nft.sellorder.price ");
            case "priceDsc":
                return new Pair<>(" ORDER BY sellorders.price DESC ", " ORDER BY nft.sellorder.price DESC ");
            case "noSort":
                return new Pair<>(" "," ");
            case "collection":
                return new Pair<>(" ORDER BY nfts.collection "," ORDER BY nft.collection ");
        }
        return new Pair<>(" ORDER BY nfts.nft_name ", " ORDER BY nft.nftName ");
    }

    @Override
    public List<Nft> getAllPublicationsByUser(int page, int pageSize, User user, boolean onlyFaved, boolean onlyOnSale, String sort) {
        Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery = buildFilterQueryByUser(user, onlyFaved, onlyOnSale);
        return executeQueries(filterQuery, pageSize, page, sort);
    }
    private List<Nft> executeQueries(Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery, int pageSize, int page, String sort) {
        String nativeQuery = filterQuery.getLeft().getLeft().toString();
        String hqlQuery = filterQuery.getLeft().getRight().toString();
        Pair<String,String> sorts = applySort(sort);
        List<Pair<String,Object>> args = filterQuery.getRight();

        if(page <= 0)
            page = 1;
        StringBuilder queryString = new StringBuilder(SELECT_ID_QUERY);
        queryString.append(nativeQuery).append(sorts.getLeft()).append(" LIMIT :pageSize OFFSET :pageOffset");
        final Query idQuery = em.createNativeQuery(queryString.toString());
        idQuery.setParameter("pageSize", pageSize);
        idQuery.setParameter("pageOffset", pageSize*(page-1));
        for(Pair<String,Object> arg:args) {
            if(arg.getLeft().equals("category") || arg.getLeft().equals("chain")) {
                @SuppressWarnings("unchecked")
                List<String> els = (List<String>) arg.getRight();
                for(int i = 0; i < els.size(); i++)
                    idQuery.setParameter(arg.getLeft().concat(String.valueOf(i)), els.get(i));
            } else
                idQuery.setParameter(arg.getLeft(), arg.getRight());
        }

        @SuppressWarnings("unchecked")
        final List<Integer> ids = (List<Integer>) idQuery.getResultList().stream().collect(Collectors.toList());

        if(ids.size() == 0)
            return Collections.emptyList();

        final TypedQuery<Nft> query = em.createQuery("FROM Nft AS nft WHERE nft.id IN :ids ".concat(hqlQuery).concat(sorts.getRight()), Nft.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    private Integer getIds(Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery, String sort) {
        String nativeQuery = filterQuery.getLeft().getLeft().toString();
        Pair<String, String> sorts = applySort(sort);
        List<Pair<String,Object>> args = filterQuery.getRight();

        StringBuilder queryString = new StringBuilder(COUNT_ID_QUERY).append(nativeQuery).append(sorts.getLeft());
        final Query countQuery = em.createNativeQuery(queryString.toString());
        for(Pair<String,Object> arg:args) {
            if(arg.getLeft().equals("category") || arg.getLeft().equals("chain")) {
                @SuppressWarnings("unchecked")
                List<String> els = (List<String>) arg.getRight();
                for(int i = 0; i < els.size(); i++)
                    countQuery.setParameter(arg.getLeft().concat(String.valueOf(i)), els.get(i));
            } else
                countQuery.setParameter(arg.getLeft(), arg.getRight());
        }

        @SuppressWarnings("unchecked")
        Optional<BigInteger> res = (Optional<BigInteger>) countQuery.getResultList().stream().findFirst();
        return res.orElse(BigInteger.ZERO).intValue();
    }

    @Override
    public int getAmountPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, String searchFor) {
        Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery = buildFilterQuery(status, category, chain, minPrice, maxPrice, search, searchFor);
        return getIds(filterQuery, "noSort");
    }

    @Override
    public int getAmountPublicationPagesByUser(int pageSize, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale) {
        Pair<Pair<StringBuilder, StringBuilder>, List<Pair<String,Object>>> filterQuery = buildFilterQueryByUser(user, onlyFaved, onlyOnSale);
        return (getIds(filterQuery, "noSort")-1)/pageSize+1;
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
