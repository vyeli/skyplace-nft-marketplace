package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;

public interface ImageDao {

    Image getImage(long id);
}
