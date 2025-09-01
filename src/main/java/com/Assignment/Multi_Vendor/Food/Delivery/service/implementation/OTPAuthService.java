package com.Assignment.Multi_Vendor.Food.Delivery.service.implementation;

import com.Assignment.Multi_Vendor.Food.Delivery.dto.EmailDetailsDto;
import com.Assignment.Multi_Vendor.Food.Delivery.model.Orders;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OTPAuthService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(EmailDetailsDto detailsDto){
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(from);
        mail.setTo(detailsDto.getTo());
        mail.setText("""
                This mail is for 2-step authentication, pls note down the one time password
                
                123456
                """);
        mail.setSubject("2-Step Authentication. ");

        mailSender.send(mail);
    }

    public void mailSender(EmailDetailsDto detailsDto, Orders orders){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(detailsDto.getTo());
        message.setText("Your order, " + orders.getDishName() + ", from restaurant," + orders.getRestaurantName()
                + " has been delivered to you by delivery agent" + orders.getAgent().getFirstName()
        );

        mailSender.send(message);
    }

}
