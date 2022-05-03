package com.shanthan.customerdetailsbatch.mapper;

import com.shanthan.customerdetailsbatch.domain.Address;
import com.shanthan.customerdetailsbatch.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import static com.shanthan.customerdetailsbatch.util.CustomerConstants.*;

@Data
@Builder
@AllArgsConstructor
public class CustomerFieldMapper implements FieldSetMapper<Customer> {

    @Override
    public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
        Address address = Address.builder()
                .address1(fieldSet.readString(ADDRESS_1))
                .address2(fieldSet.readString(ADDRESS_2))
                .city(fieldSet.readString(CITY))
                .state(fieldSet.readString(STATE))
                .zipCode(fieldSet.readString(ZIPCODE))
                .build();

      return Customer.builder()
                .recordId(fieldSet.readInt(RECORD_ID))
                .customerId(fieldSet.readString(CUSTOMER_ID))
                .firstName(fieldSet.readString(FIRST_NAME))
                .middleName(fieldSet.readString(MIDDLE_NAME))
                .lastName(fieldSet.readString(LAST_NAME))
                .address(address)
                .emailAddress(fieldSet.readString(EMAIL_ADDRESS))
                .homePhone(fieldSet.readString(HOME_PHONE))
                .cellPhone(fieldSet.readString(CELL_PHONE))
                .workPhone(fieldSet.readString(WORK_PHONE))
                .notificationPreference(fieldSet.readInt(NOTIFICATION_PREFERENCE))
                .build();
    }
}
