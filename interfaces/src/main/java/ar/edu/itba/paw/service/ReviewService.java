package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.DummyReview;

import java.util.List;

public interface ReviewService {

    void addReview(int idReviewer, int idReviewee, int score, String title, String comments);

    List<DummyReview> getUserReviews(int idUser);

    void deleteReview(int idReview);

}
