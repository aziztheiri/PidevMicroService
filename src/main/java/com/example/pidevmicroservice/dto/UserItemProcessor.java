package com.example.pidevmicroservice.dto;

import com.example.pidevmicroservice.entities.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class UserItemProcessor implements ItemProcessor<User, UserReportDTO> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public UserReportDTO process(User user) throws Exception {
        String creationDate = user.getCreationDate() != null ? user.getCreationDate().format(formatter) : "N/A";
        return new UserReportDTO(
                user.getCin(),
                user.getEmail(),
                user.getName(),
                user.getUserRole().toString(),
                user.isVerified(),
                creationDate,
                user.getAge(),
                user.getGender(),
                user.getMonthsSinceLastClaim(),
                user.getTotalClaimAmount(),
                user.getMonthlyPremiumAuto(),
                user.getCustomerLifetimeValue(),
                user.getVehicleClassLuxuryCar(),
                user.getEmploymentStatusEmployed(),
                user.getLocationCodeSuburban()
        );
    }
}