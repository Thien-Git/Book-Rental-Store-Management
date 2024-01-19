package com.example.brmproject.controller.staff;

import com.example.brmproject.domain.dto.CategoryBookDTO;
import com.example.brmproject.domain.dto.CategoryDTO;
import com.example.brmproject.repositories.CategoryEntityRepository;
import com.example.brmproject.service.CategoryBookService;
import com.example.brmproject.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("staff")

public class CategoryController {

    @Autowired
    private CategoryEntityRepository categoryEntityRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryBookService categoryBookService;

    @GetMapping("/categories/new")
    public String getNewCategoryPage(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "adminTemplate/category/addNewCategory";
    }

    @PostMapping("/categories/new")
    public String addNewCategory(@ModelAttribute("category") @Valid CategoryDTO categoryDTO,
                                 BindingResult bindingResult,
                                 Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("category", categoryDTO);
            return "adminTemplate/category/addNewCategory";
        }
        CategoryDTO newCategory = categoryService.addNewCategory(categoryDTO);
        return "redirect:/staff/categories";
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "adminTemplate/category/showAllCategories";
    }


    @GetMapping("/categories/update/{categoryID}")
    public String getUpdateCategoryPage(@PathVariable("categoryID") Integer categoryId, Model model) {
        CategoryDTO categoryDTO = categoryService.findById(categoryId);
        model.addAttribute("category", categoryDTO);
        return "adminTemplate/category/updateCategory";
    }

    @PostMapping("/categories/update/{categoryID}")
    public String updateCategory(Model model, @ModelAttribute("newCategory") CategoryDTO categoryDTO,
                                 @PathVariable Integer categoryID
    ) {
        CategoryDTO findCategoryDTO = categoryService.findById(categoryID);
        if(!categoryDTO.getName().trim().isEmpty()) {
            findCategoryDTO.setName(categoryDTO.getName());
        }
        if (!categoryDTO.getDescription().trim().isEmpty()) {
            findCategoryDTO.setDescription(categoryDTO.getDescription());
        }
        categoryService.updateCategory(findCategoryDTO);
        List<CategoryBookDTO> categoryBookList = categoryBookService.findByCategoryId(categoryID);
        for (CategoryBookDTO categoryBook : categoryBookList) {
            Integer categoryBookId = categoryBook.getId();
            categoryBookService.updateCategoryBook(categoryBookId, categoryID);
        }
        return "redirect:/staff/categories";
    }
}
