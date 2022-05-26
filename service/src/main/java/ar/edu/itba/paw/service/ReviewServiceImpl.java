package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.DummyReview;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService{

    private UserService userService;

    @Autowired
    public ReviewServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void addReview(int idReviewer, int idReviewee, int score, String title, String comments) {

    }

    @Override
    public List<DummyReview> getUserReviews(int idUser) {
        Optional<User> user = userService.getUserById(idUser);
        List<DummyReview> userReviews = new ArrayList<>();
        userReviews.add(new DummyReview(userService.getUserById(2).get(),user.get(),5,"One of the best vendors", "Always having such an extensive nft catalog and with not so high prices. 10/10"));
        userReviews.add(new DummyReview(userService.getUserById(2).get(),user.get(),1,"Go buy the actual nft", "This guy just screenshots every NFT he sells. Just look up on the searchbar for one of his NFTs and you'll find another publicaction for EVERY SINGLE ONE of his articles. Disgraceful"));
        userReviews.add(new DummyReview(userService.getUserById(2).get(),user.get(),3,"Could be better", "His nfts aren't that good, but with such an extensive catalog you can buy almost anything. Also try making multiple offers on some NFTs to get a 'discount'. It worked for me, so try yourself"));
        return userReviews;
    }

    @Override
    public void deleteReview(int idReview) {

    }
}
