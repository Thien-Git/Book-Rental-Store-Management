//package com.example.brmproject.ultilities;
//
//import com.example.brmproject.domain.dto.BookDetailDTO;
//import com.example.brmproject.domain.dto.MySession;
//import com.example.brmproject.service.BookDetailService;
//import com.example.brmproject.ultilities.SD.BookDetailStatus;
//import jakarta.servlet.http.HttpSession;
//import jakarta.servlet.http.HttpSessionEvent;
//import jakarta.servlet.http.HttpSessionListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SessionDestroyListener implements HttpSessionListener {
//    private  BookDetailService bookDetailService;
//
//    @Autowired
//    public SessionDestroyListener(BookDetailService bookDetailService) {
//        this.bookDetailService = bookDetailService;
//        System.out.println("SessionDestroyListener initialized");
//    }
//
//    @Override
//    public void sessionDestroyed(HttpSessionEvent event) {
//        HttpSession session = event.getSession();
//        session.getAttribute("session");
//        MySession mySession=(MySession) session.getAttribute("session");
//        if(mySession.getBookDetailList()!=null) {
//            for (BookDetailDTO bd : mySession.getBookDetailList()) {
//                bookDetailService.updateStatus(bd, BookDetailStatus.AVAILABLE.toString());
//            }
//        }
//        System.out.println("Session is about to be destroyed: " + session.getId());
//        System.out.println("Session is about to be destroyed: " + session.getAttribute("session"));
//
//        // Uncomment and correct this section when you have a clear understanding
//        // of how you are storing BookDetailDTO objects in the session.
//        // List<BookDetailDTO> bookedBookIds = (List<BookDetailDTO>) session.getAttribute("session");
//        // for (BookDetailDTO bd : bookedBookIds) {
//        //     bookDetailService.updateStatusToAvailable(bd.getId()); // Assuming BookDetailDTO has a method getId()
//        // }
//    }
//
//}
