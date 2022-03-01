package com.example.ecommerce.web;

import com.example.ecommerce.model.views.CategoryViewModel;
import com.example.ecommerce.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper mapper;

    public CategoryController(CategoryService categoryService, ModelMapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

//    @GetMapping("/getAllCategory")
//    @ResponseBody
//    public ResponseEntity<List<CategoryViewModel>> getAllCategory(){
//        return new ResponseEntity<>(this.categoryService.getAllCategory(), HttpStatus.OK);
//    }


    @GetMapping("/getAllCategory")
    public String getAllCategory(Model model){
        model.addAttribute("allCategories", this.categoryService.getAllCategory());
        model.addAttribute("allCategoriesCount", this.categoryService.getAllCategory().size());
        return "all-categories";
    }
}
