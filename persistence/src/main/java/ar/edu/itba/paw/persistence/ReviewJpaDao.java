package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.NftNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.Favorited;
import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReviewJpaDao implements ReviewDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewJpaDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Review addReview(User reviewer, User reviewee, int score, String title, String comments) {
        final Review newReview = new Review(reviewer, reviewee, score, title, comments);
        em.persist(newReview);
        return newReview;
    }

    @Override
    public List<Review> getUserReviews(int page, int userId, int pageSize) {
        final Query idQuery = em.createNativeQuery("SELECT id FROM reviews WHERE id_reviewee = :id_reviewee LIMIT :pageSize OFFSET :pageOffset");
        idQuery.setParameter("id_reviewee", userId);
        idQuery.setParameter("pageSize", pageSize);
        idQuery.setParameter("pageOffset", (page-1)*pageSize);
        @SuppressWarnings("unchecked")
        final List<Integer> reviewsIds = (List<Integer>) idQuery.getResultList().stream().collect(Collectors.toList());

        if(reviewsIds.size() == 0)
            return Collections.emptyList();

        final TypedQuery<Review> query = em.createQuery("FROM Review AS r WHERE r.id IN :ids", Review.class);
        query.setParameter("ids", reviewsIds);
        return query.getResultList();
    }

    @Override
    public List<Review> getAllUserReviews(int userId) {
        final TypedQuery<Review> query = em.createQuery("FROM Review AS r WHERE r.usersByIdReviewee.id = :userId", Review.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public boolean hasReviewByUser(int reviewerId, int revieweeId) {
        final Query query = em.createNativeQuery("SELECT count(*) FROM reviews WHERE id_reviewer = :id_reviewer AND id_reviewee = :id_reviewee");
        query.setParameter("id_reviewer", reviewerId);
        query.setParameter("id_reviewee", revieweeId);
        return ((BigInteger)query.getSingleResult()).longValue() > 0;
    }

    @Override
    public long getUserReviewsAmount(int userId){
        final Query query = em.createNativeQuery("SELECT count(*) FROM reviews WHERE id_reviewee = :id_reviewee");
        query.setParameter("id_reviewee", userId);
        return ((BigInteger)query.getSingleResult()).longValue();
    }

    @Override
    public void deleteReview(int reviewId) {
        Review review = em.find(Review.class, reviewId);
        em.remove(review);
    }
}
