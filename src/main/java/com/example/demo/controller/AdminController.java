package com.example.demo.controller;


import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    private CategoryService categoryService;
    private ProductService productService;

    public AdminController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages ";

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String productCategory(Model model){
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }
    @GetMapping("/admin/categories/add")
    public String addProductCategory(Model model){
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }
    @PostMapping("/admin/categories/add")
    public String postProductCategory(@ModelAttribute("category") Category category){
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteProductCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/update/{id}")
    public String updateProductCategory(@PathVariable long id, Model model){
        Optional<Category> category = categoryService.getCategoryById(id);
        if(category.isPresent()){
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        }else{
            return "404";
        }
    }
    @GetMapping("/admin/products")
    public String products(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }
    @GetMapping("/admin/products/add")
    public String productsAdd(Model model){
        model.addAttribute("productDTO", new ProductDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String productsAddPost(@ModelAttribute("productDTO") ProductDto productDto,
                              @RequestParam("imageName")MultipartFile file,
                              @RequestParam("imgName")String imgName) throws IOException {

        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCategory(categoryService.getCategoryById(productDto.getCategoryId()).get());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setWeight(productDto.getWeight());
        String imageUUID;
        if(!file.isEmpty()){
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        }else{
            imageUUID = imgName;
        }
        product.setImageName(imageUUID);

        productService.addProduct(product);
        return "redirect:/admin/products";

    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id, Model model){
        Product product = productService.getProductById(id).get();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setWeight(product.getWeight());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setImageName(product.getImageName());

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("productDTO", productDto);
        return "productsAdd";
    }
}
