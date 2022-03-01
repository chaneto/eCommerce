package com.example.ecommerce.web;

import com.example.ecommerce.model.bindings.ProductAddBindingModel;
import com.example.ecommerce.model.bindings.ProductBuyBindingModel;
import com.example.ecommerce.model.entities.ProductEntity;
import com.example.ecommerce.model.serviceModels.ProductServiceModel;
import com.example.ecommerce.model.views.ProductRestViewModel;
import com.example.ecommerce.model.views.ProductViewModel;
import com.example.ecommerce.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper mapper;
    private int[] index = new int[2];

    public ProductController(ProductService productService, ModelMapper mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }

    @GetMapping("add")
    public String addProduct(Model model){
        if (!model.containsAttribute("productAddBindingModel")) {
            model.addAttribute("productAddBindingModel", new ProductAddBindingModel());
            model.addAttribute("productIsExists", false);
        }

        return "add-product";
    }

    @PostMapping("add")
    public String addProductConfirm(@Valid ProductAddBindingModel productAddBindingModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);
            return "redirect:add";
        }
        ProductServiceModel productServiceModel = this.mapper.map(productAddBindingModel, ProductServiceModel.class);

        if (this.productService.productIsExists(productServiceModel.getName())) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("productIsExists", true);
            return "redirect:add";
        }

        redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
        redirectAttributes.addFlashAttribute("successfulProductAdded", true);
        this.productService.seedProduct(productServiceModel);

        return "redirect:add";
    }


//      @PostMapping("add")
//    @ResponseBody
//    public ResponseEntity<ProductEntity> create(ProductAddBindingModel productAddBindingModel) throws ServletException {
//        ProductServiceModel productServiceModel = this.mapper.map(productAddBindingModel, ProductServiceModel.class);
//        ProductEntity product = this.productService.seedProduct(productServiceModel);
//        if (product == null) {
//            throw new ServletException();
//        } else {
//            return new ResponseEntity<>(product, HttpStatus.CREATED);
//        }
//    }


//    @GetMapping("/allProducts/{id}/{pageNo}")
//    @ResponseBody
//    public ResponseEntity<?>  allProductsOrderByQuantities(@PathVariable("id") Long id, @PathVariable("pageNo") Integer pageNo, Model model){
//        List<ProductViewModel> products = new ArrayList<>();
//        if(id > 6){
//            id = Long.valueOf(6);
//        }
//        Sort sortBy = null;
//        if(id == 1){
//            sortBy = Sort.by("name");
//        }else if(id == 2){
//            sortBy = Sort.by("name").descending();
//        }else if(id == 3){
//            sortBy = Sort.by("category");
//        }else if(id == 4){
//            sortBy = Sort.by("category").descending();
//        }else if(id == 5){
//            sortBy = Sort.by("createdDate");
//        }else if(id == 6){
//            sortBy = Sort.by("createdDate").descending();
//        }
//        double pageCount = Math.ceil((double) this.productService.getAllProductCount() / 3);
//        if(pageNo > pageCount){
//            pageNo = (int)pageCount -1;
//        }
//        products = this.productService.getAllProducts(pageNo, 3, sortBy);
//        ProductRestViewModel productsVew = new ProductRestViewModel(products, this.productService.getAllProductCount());
//        model.addAttribute("allProducts", productsVew);
//        model.addAttribute("sortIndex", id);
//        model.addAttribute("pageCount", pageCount);
//        model.addAttribute("pageNo", pageNo);
//        return new ResponseEntity<>(productsVew, HttpStatus.OK) ;
//    }

    @GetMapping("/allProducts/{id}/{pageNo}")
    public String getAllEmployees(@PathVariable("id") Long id, @PathVariable("pageNo") Integer pageNo, Model model) {
        List<ProductViewModel> products = new ArrayList<>();
        if(id > 6){
            id = Long.valueOf(6);
        }
        Sort sortBy = null;
        if(id == 1){
            sortBy = Sort.by("name");
        }else if(id == 2){
            sortBy = Sort.by("name").descending();
        }else if(id == 3){
            sortBy = Sort.by("category");
        }else if(id == 4){
            sortBy = Sort.by("category").descending();
        }else if(id == 5){
            sortBy = Sort.by("createdDate");
        }else if(id == 6){
            sortBy = Sort.by("createdDate").descending();
        }
        double pageCount = Math.ceil((double) this.productService.getAllProductCount() / 3);
        if(pageNo > pageCount){
            pageNo = (int)pageCount -1;
        }
        products = this.productService.getAllProducts(pageNo, 3, sortBy);
        ProductRestViewModel productsVew = new ProductRestViewModel(products, this.productService.getAllProductCount());
        model.addAttribute("allProducts", productsVew);
        model.addAttribute("sortIndex", id);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("pageNo", pageNo);
        return "all-products";
    }


    @GetMapping(value = "/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        this.productService.deleteProductById(id);
        model.addAttribute("pageCount", this.productService.getAllProductCount() / 3);
        return "redirect:" + referer;
    }

    @GetMapping ("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id, Model model, ProductAddBindingModel productAddBindingModel, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        productAddBindingModel = this.mapper.map(this.productService.getById(id), ProductAddBindingModel.class);
        model.addAttribute("productAddBindingModel", productAddBindingModel);
            index[0] = Integer.parseInt(referer.substring(referer.length() - 3, referer.length() - 2));
            index[1] = Integer.parseInt(referer.substring(referer.length() - 1));
        model.addAttribute("page", index);
        return  "update-product";
    }

    @GetMapping ("/update")
    public String update(Model model) {
        model.addAttribute("page", index);
        return  "update-product";
    }

    @PostMapping("/update/{id}/{page}")
    public String updateProductConfirm(@Valid ProductAddBindingModel productAddBindingModel,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       @PathVariable("id") Long id, @PathVariable("page") int[] page){

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);
            return "redirect:/products/update";
        }
        ProductViewModel productViewModel = this.productService.getById(id);
        ProductServiceModel productServiceModel = this.mapper.map(productAddBindingModel, ProductServiceModel.class);
        boolean namesMatches = productViewModel.getName().equals(productServiceModel.getName());

        if (this.productService.productIsExists(productServiceModel.getName()) && !namesMatches) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("productIsExists", true);
            return "redirect:/products/update";
        }
            this.productService.updateProduct(productServiceModel, id);

            return "redirect:/products/allProducts/" + page[0] + "/" + page[1];
        }

        @PostMapping("/buy/{id}")
        public String buyProduct(@Valid ProductBuyBindingModel productBuyBindingModel,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @PathVariable("id") Long id, HttpServletRequest request){
            String referer = request.getHeader("Referer");
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("productName", this.productService.getById(id).getName());
                if(productBuyBindingModel.getQuantityBuy() == null){
                    redirectAttributes.addFlashAttribute("quantityIsNull", true);
                    return "redirect:" + referer;
                }else if(productBuyBindingModel.getQuantityBuy().compareTo(BigDecimal.ZERO) <= 0){
                    redirectAttributes.addFlashAttribute("quantityIsNegative", true);
                    return "redirect:" + referer;
                }
            }
            BigDecimal quantityBuy = productBuyBindingModel.getQuantityBuy();
            if(this.productService.quantityIsEnough(id, quantityBuy)){
                this.productService.setQuantity(quantityBuy, id);
                return "redirect:" + referer;
            }else {
                redirectAttributes.addFlashAttribute("quantityIsNotEnough", true);
                redirectAttributes.addFlashAttribute("productName", this.productService.getById(id).getName());
                return "redirect:" + referer;
            }
        }

    @ModelAttribute("productBuyBindingModel")
    public ProductBuyBindingModel productBuyBindingModel() {
        return new ProductBuyBindingModel();
    }
}
