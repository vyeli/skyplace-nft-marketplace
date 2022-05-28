package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.InvalidChainException;
import ar.edu.itba.paw.exceptions.UserIsNotNftOwnerException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.NftDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Service
public class NftServiceImpl implements NftService {
    private final NftDao nftDao;
    private final int pageSize = 12;
    private final UserService userService;

    @Autowired
    public NftServiceImpl(NftDao nftDao, UserService userService) {
        this.nftDao = nftDao;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Nft create(int nftId, String contractAddr, String nftName, String chain, MultipartFile image, int idOwner, String collection, String description) {
        if(Arrays.stream(Chain.values()).noneMatch(e -> e.name().equals(chain)))
            throw new InvalidChainException();

        User owner = userService.getUserById(idOwner).orElseThrow(UserNotFoundException::new); // FIXME: replace with checked exception
        return nftDao.create(nftId, contractAddr, nftName, Chain.valueOf(chain), image, owner, collection, description);
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Optional<Nft> getNFTById(int nftId) {
        return nftDao.getNFTById(nftId);
    }

    @Override
    public List<Publication> getAllPublications(int page, String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, String searchFor) {
        List<Nft> nfts = nftDao.getAllPublications(page, pageSize, status, category, chain, minPrice, maxPrice, sort, search, searchFor);
        List<Publication> publications = new ArrayList<>();
        User currentUser = userService.getCurrentUser().orElse(null);
        return createPublicationsWithNfts(nfts, currentUser);
    }

    private List<Publication> createPublicationsWithNfts(List<Nft> nfts, User user) {
        List<Publication> publications = new ArrayList<>();
        for(Nft nft:nfts) {
            if(user == null)
                publications.add(new Publication(nft, null));
            else {
                boolean currentUserFavedNft = false;
                for(Favorited favorite:nft.getFavoritedsById())
                    if(favorite.getUser().getId() == user.getId()) {
                        publications.add(new Publication(nft, favorite));
                        currentUserFavedNft = true;
                        break;
                    }
                if(!currentUserFavedNft)
                    publications.add(new Publication(nft, null));
            }
        }
        return publications;
    }

    @Override
    public List<Publication> getAllPublicationsByUser(int page, User user, String publicationType, String sort) {
        User currentUser = userService.getCurrentUser().orElse(null);

        if (publicationType.equals("favorited") && (currentUser == null || currentUser.getId() != user.getId()))
            return Collections.emptyList();

        switch (publicationType) {
            case "favorited":
                List<Nft> favedNfts = nftDao.getAllPublicationsByUser(page, pageSize, user, true, false, sort);
                return createPublicationsWithNfts(favedNfts, currentUser);
            case "selling":
                List<Nft> sellingNfts = nftDao.getAllPublicationsByUser(page, pageSize, user, false, true, sort);
                return createPublicationsWithNfts(sellingNfts, currentUser);
            case "inventory":
                List<Nft> nfts = nftDao.getAllPublicationsByUser(page, pageSize, user, false, false, sort);
                return createPublicationsWithNfts(nfts, currentUser);
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public int getAmountPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, String searchFor) {
        return nftDao.getAmountPublications(status, category, chain, minPrice, maxPrice, sort, search, searchFor);
    }

    @Override
    public int getAmountPublicationPagesByUser(User user, User currentUser, String publicationType) {
        switch (publicationType) {
            case "favorited":
                return nftDao.getAmountPublicationPagesByUser(pageSize, user, currentUser, true, false);
            case "selling":
                return nftDao.getAmountPublicationPagesByUser(pageSize, user, currentUser, false, true);
            case "inventory":
                return nftDao.getAmountPublicationPagesByUser(pageSize, user, currentUser, false, false);
            default:
                return 1;
        }
    }

    @Transactional
    @Override
    public void delete(int productId) {
        if (!userService.currentUserOwnsNft(productId) && !userService.isAdmin())
            throw new UserIsNotNftOwnerException();

        nftDao.delete(productId);
    }

}