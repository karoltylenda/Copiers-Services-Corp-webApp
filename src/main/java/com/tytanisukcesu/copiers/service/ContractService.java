package com.tytanisukcesu.copiers.service;

import com.tytanisukcesu.copiers.entity.Contract;
import com.tytanisukcesu.copiers.entity.Device;
import com.tytanisukcesu.copiers.repository.ContractRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRespository contractRespository;

    public List<Contract> findAll(){
        List<Contract> contracts = contractRespository.findAll();
        return contracts;
    }

    public Contract findById(Long id){
        Optional<Contract> contractOptional = contractRespository.findById(id);
        return contractOptional.orElse(new Contract());
    }

    public Contract save(Contract contract){
        Optional<Contract> contractOptional = contractRespository.findContractByDevice(contract.getDevice());
        if (contractOptional.isPresent()){
            return contractOptional.get();
        } else {
            Contract contractSaved = contractRespository.save(contract);
            return contractSaved;
        }
    }

    public boolean delete(Long id) {
        Optional<Contract> contractOptional = contractRespository.findById(id);
        if (contractOptional.isPresent()) {
            contractRespository.delete(contractOptional.get());
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Contract update(Long id, Contract contract){
        Optional<Contract> contractOptional = contractRespository.findById(id);
        if(contractOptional.isPresent()){
            Contract contractUpdated = contractOptional.get();
            contractUpdated.setContractNumber(contract.getContractNumber());
            contractUpdated.setColorPagePrice(contract.getColorPagePrice());
            contractUpdated.setMonoPagePrice(contract.getMonoPagePrice());
            contractUpdated.setLeasePrice(contract.getLeasePrice());
            contractUpdated.setStartDate(contract.getStartDate());
            contractUpdated.setEndDate(contract.getEndDate());
            return contractUpdated;
        } else {
            return new Contract();
        }
    }
}
