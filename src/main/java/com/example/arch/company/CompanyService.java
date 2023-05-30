package com.example.arch.company;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyDtoMapper companyDtoMapper;
    private final CompanyJobOfferDtoMapper companyJobOfferDtoMapper;

    public CompanyService(CompanyRepository companyRepository,
                          CompanyDtoMapper companyDtoMapper,
                          CompanyJobOfferDtoMapper companyJobOfferDtoMapper) {
        this.companyRepository = companyRepository;
        this.companyDtoMapper = companyDtoMapper;
        this.companyJobOfferDtoMapper = companyJobOfferDtoMapper;
    }

    Optional<CompanyDto> getCompanyById(Long id){
        return companyRepository.findById(id)
                .map(company -> companyDtoMapper.map(company));
    }

    List<CompanyJobOfferDto> getJobOffersByCompanyId(Long companyId){
        return companyRepository.findById(companyId)
                .map(Company::getJobOffers)
                .orElse(Collections.emptyList())
                .stream()
                .map(jobOffer -> companyJobOfferDtoMapper.map(jobOffer))
                .toList();
    }

    CompanyDto saveCompany(CompanyDto companyDto){
        Company company = companyDtoMapper.map(companyDto);
        Company savedCompany = companyRepository.save(company);
        return companyDtoMapper.map(savedCompany);
    }

    Optional<CompanyDto> replaceCompany(Long companyId, CompanyDto companyDto){
        if (!companyRepository.existsById(companyId)){
            return Optional.empty();
        }
        companyDto.setId(companyId);
        Company companyToUpdate = companyDtoMapper.map(companyDto);
        Company updatedEntity = companyRepository.save(companyToUpdate);
        return Optional.of(companyDtoMapper.map(updatedEntity));
    }

    void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

}
