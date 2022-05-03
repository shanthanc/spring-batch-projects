package com.shanthan.customerdetailsbatch.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class Customer {

    @NotNull(message = "RecordId cannot be null")
    @Pattern(regexp = "\\d", message = "Invalid RecordId!")
    private Integer recordId;

    @NotNull(message = "CustomerId cannot be null")
    @Pattern(regexp = "\\d{9}", message = "Invalid customerId!")
    private String customerId;

    @NotNull(message = "First name cannot be null or empty")
    @Pattern(regexp = "\\p{Alpha}+", message = "Invalid first name!")
    private String firstName;

    @Pattern(regexp = "\\p{Alpha}+", message = "Invalid middle name!")
    private String middleName;

    @NotNull(message = "Last name cannot be null or empty")
    @Pattern(regexp = "\\p{Alpha}+", message = "Invalid last name!")
    private String lastName;

    @NotNull(message = "Address cannot be null or empty")
    private Address address;

    @Email(message = "Invalid email")
    private String emailAddress;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Invalid home phone!")
    private String homePhone;

    @NotNull(message = "Cell phone can't be null!")
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Invalid cell phone!")
    private String cellPhone;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Invalid work phone!")
    private String workPhone;

    @NotNull(message = "Notification preference cannot be null")
    @Pattern(regexp = "\\d{1}", message = "Invalid notification preference")
    private Integer notificationPreference;
}

