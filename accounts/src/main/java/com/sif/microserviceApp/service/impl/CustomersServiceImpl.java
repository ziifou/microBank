package com.sif.microserviceApp.service.impl;


import com.sif.microserviceApp.dto.AccountsDto;
import com.sif.microserviceApp.dto.CardsDto;
import com.sif.microserviceApp.dto.CustomerDetailsDto;
import com.sif.microserviceApp.dto.LoansDto;
import com.sif.microserviceApp.entity.Accounts;
import com.sif.microserviceApp.entity.Customer;
import com.sif.microserviceApp.exception.ResourceNotFoundException;
import com.sif.microserviceApp.mapper.AccountsMapper;
import com.sif.microserviceApp.mapper.CustomerMapper;
import com.sif.microserviceApp.repository.AccountsRepository;
import com.sif.microserviceApp.repository.CustomerRepository;
import com.sif.microserviceApp.service.ICustomersService;
import com.sif.microserviceApp.service.clients.CardsFeignClient;
import com.sif.microserviceApp.service.clients.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        if(loansDtoResponseEntity != null){
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        if(cardsDtoResponseEntity != null){
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }

        return customerDetailsDto;
    }
}