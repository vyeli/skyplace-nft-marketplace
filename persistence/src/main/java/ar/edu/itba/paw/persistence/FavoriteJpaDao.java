package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.NftNotFoundException;
import ar.edu.itba.paw.model.Favorited;
import ar.edu.itba.paw.model.FavoritedId;
import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;

@Repository
public class FavoriteJpaDao implements FavoriteDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteJpaDao.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private NftDao nftDao;

    @Override
    public void addNftFavorite(int productId, User user) {
        Nft nft = nftDao.getNFTById(productId).orElseThrow(NftNotFoundException::new);
        Favorited newFavorited = new Favorited(user, nft);
        em.persist(newFavorited);
    }

    @Override
    public void removeNftFavorite(int productId, User user) {
        nftDao.getNFTById(productId).orElseThrow(NftNotFoundException::new);
        Favorited favoriteToRemove = em.find(Favorited.class, new FavoritedId(user.getId(), productId));
        em.remove(favoriteToRemove);
    }

    @Override
    public boolean userFavedNft(int userId, int idNft) {
        final Query query = em.createNativeQuery("SELECT * FROM favorited WHERE user_id = :user_id AND id_nft = :id_nft ");
        query.setParameter("user_id", userId);
        query.setParameter("id_nft", idNft);
        return query.getResultList().size() > 0;
    }

    @Override
    public int getNftFavorites(int productId) {
        final Query query = em.createNativeQuery("SELECT count(*) FROM Favorited WHERE id_nft = :id_nft");
        query.setParameter("id_nft", productId);
        return ((BigInteger) query.getSingleResult()).intValue();
    }
}
