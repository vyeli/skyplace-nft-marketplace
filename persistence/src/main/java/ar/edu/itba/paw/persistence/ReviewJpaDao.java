package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ReviewJpaDao implements ReviewDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewJpaDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addReview(int idReviewer, int idReviewee, int score, String comments) {
        final Review newReview = new Review(idReviewer, idReviewee, score, comments);
        em.persist(newReview);
    }
}
