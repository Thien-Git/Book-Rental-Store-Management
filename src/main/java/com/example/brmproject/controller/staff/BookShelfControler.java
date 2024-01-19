package com.example.brmproject.controller.staff;

import com.example.brmproject.domain.dto.BookshelfCaseDTO;
import com.example.brmproject.service.BookShelfService;
import com.example.brmproject.ultilities.StaticFunction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("staff")
public class BookShelfControler {

    BookShelfService bookShelfService;
    @Autowired
    public BookShelfControler(BookShelfService bookShelfService) {
        this.bookShelfService = bookShelfService;
    }
    @PostMapping("/bookshelf/search-bookId")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String searchByBookId(Model model,@RequestParam(name = "bookId") Integer bookId,RedirectAttributes redirectAttributes)
    {
        try {
            BookshelfCaseDTO caseDTO = bookShelfService.searchByBookId(bookId);
            if(caseDTO==null)
            {
                redirectAttributes.addFlashAttribute("alertError","Can't find book shelf for this book!");
                return "redirect:/staff/bookshelf/show-all";
            }
            model.addAttribute("case", caseDTO);
            return "adminTemplate/bookShelf/bookshelf-detail";
        }catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("alertError",e.getMessage());
            return "redirect:/staff/bookshelf/show-all";
        }
    }

    @GetMapping("/bookshelf/detail/{bookshelfId}")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String detail(Model model,@PathVariable Integer bookshelfId,RedirectAttributes redirectAttributes)
    {
        try {
            BookshelfCaseDTO detail=bookShelfService.findById(bookshelfId);
            model.addAttribute("case",detail);
            return "adminTemplate/bookShelf/bookshelf-detail";
        }catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("alertError",e.getMessage());
            return "redirect:/staff/bookshelf/show-all";
        }
    }

    @PostMapping("/bookshelf/search-blank-case")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String showBlank(Model model,@RequestParam Integer num)
    {
        List<BookshelfCaseDTO> list=bookShelfService.findBlankCase(num);
        model.addAttribute("bookShelfList",list);
        return "adminTemplate/bookShelf/showAllCase";

    }
    @GetMapping("/bookshelf/create")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String create (Model model){
        BookshelfCaseDTO bookshelfCase=new BookshelfCaseDTO();
        model.addAttribute("bookshelf",bookshelfCase);
        return "adminTemplate/bookShelf/create";
    }
    @PostMapping("/bookshelf/create")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String create (Model model, @ModelAttribute("bookshelf") @Valid BookshelfCaseDTO bookshelfCase, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookshelf", bookshelfCase);
            return "adminTemplate/bookShelf/create";
        }

        if(bookShelfService.create(bookshelfCase)) {
            redirectAttributes.addFlashAttribute("alertMessage","Create BookShelf success!");
        } else {
            redirectAttributes.addFlashAttribute("alertError","Opps ! Something wrong!");
        }
        return "redirect:/staff/bookshelf/show-all";
    }

    @GetMapping("/bookshelf/show-all")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String showAll (Model model,@ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError)
    {
        StaticFunction.showAlert(model,alertMessage,alertError);

        List<BookshelfCaseDTO> list=bookShelfService.showAll();
        model.addAttribute("bookShelfList",list);
        return "adminTemplate/bookShelf/showAllCase";
    }

//home
    @GetMapping("/dashboard")
    public String home()
    {
        return "adminTemplate/home";
    }
}
