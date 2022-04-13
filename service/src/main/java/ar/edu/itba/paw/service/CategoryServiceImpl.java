package ar.edu.itba.paw.service;

import ar.edu.itba.paw.persistence.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<String> getCategories() {
        return categoryDao.getCategories();
    }

    @Override
    public List<String> getStaticCategories() {
        return new ArrayList<>(Arrays.asList("all", "collections", "art", "utility", "photography", "other", "search"));
    }
}
