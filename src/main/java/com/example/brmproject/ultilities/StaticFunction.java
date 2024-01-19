package com.example.brmproject.ultilities;

import com.example.brmproject.ultilities.SD.OrderStatus;
import org.springframework.ui.Model;

public  class StaticFunction {

    //getstatus
    public static void setStatus(Model model)
    {
        model.addAttribute("booking", OrderStatus.BOOKING.toString());
        model.addAttribute("rent",OrderStatus.RENT.toString());
        model.addAttribute("overdue",OrderStatus.OVERDUE.toString());
    }

    public static void showAlert(Model model,String alertMessage, String alertError)
    {

        //show alert
        if (!alertMessage.isEmpty()) {
            model.addAttribute("alertMessage", alertMessage);
        }
        else {
            model.addAttribute("alertMessage", null);
        }
        if (!alertError.isEmpty()) {
            model.addAttribute("alertError", alertError);
        }
        else {
            model.addAttribute("alertError", null);
        }
    }
}
