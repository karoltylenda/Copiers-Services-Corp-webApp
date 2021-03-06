package com.tytanisukcesu.copiers.service;

import com.tytanisukcesu.copiers.entity.Counter;
import com.tytanisukcesu.copiers.entity.Device;
import com.tytanisukcesu.copiers.repository.CounterRepository;
import com.tytanisukcesu.copiers.repository.DeviceRepository;
import com.tytanisukcesu.copiers.service.exception.CounterIncorrectValuesException;
import com.tytanisukcesu.copiers.service.exception.DeviceNotFoundException;
import com.tytanisukcesu.copiers.service.exception.ModelNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final CounterRepository counterRepository;
    private final DeviceRepository deviceRepository;
    private static final Logger LOGGER = Logger.getLogger(CounterService.class.getName());

    private boolean isCounterPresent(Counter counter) {
        Optional<Counter> optionalCounter = counterRepository.findCounterByCounterDateAndDeviceSerialNumber(counter.getCounterDate(), counter.getDevice().getSerialNumber());
        if (optionalCounter.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Counter save(Counter counter) {
        Optional<Device> deviceOptional = deviceRepository.findById(counter.getDevice().getId());
        if (deviceOptional.isPresent()){
            counter.setDevice(deviceOptional.get());
            if(isCounterPossible(counter)){
                Counter counterSaved = counterRepository.save(counter);
                return counterSaved;
            } else throw new CounterIncorrectValuesException();
        } else throw new DeviceNotFoundException(counter.getDevice().getId());
    }

    private boolean isCounterPossible(Counter counter) {
        Optional<Counter> counterBeforeOptional = counterBefore(counter);
        Optional<Counter> counterAfterOptional = counterAfter(counter);
        if (isCounterPresent(counter)) {
            return false;
        } else if (counterAfterOptional.isEmpty() && counterBeforeOptional.isEmpty()) {
            return true;
        } else if (counterBeforeOptional.isEmpty() && counterAfterOptional.isPresent()) {
            if (counter.getMonoCounter() <= counterAfterOptional.get().getMonoCounter() && counter.getColourCounter() <= counterAfterOptional.get().getColourCounter()) {
                return true;
            } else {
                return false;
            }
        } else if (counterBeforeOptional.isPresent() && counterAfterOptional.isPresent()) {
            if (counter.getMonoCounter() >= counterBeforeOptional.get().getMonoCounter() && counter.getMonoCounter() <= counterAfterOptional.get().getMonoCounter()
                    && counter.getColourCounter() >= counterBeforeOptional.get().getColourCounter() && counter.getColourCounter() <= counterAfterOptional.get().getColourCounter()) {
                return true;
            } else {
                return false;
            }
        } else if (counterBeforeOptional.isPresent() && counterAfterOptional.isEmpty()) {
            if (counter.getMonoCounter() >= counterBeforeOptional.get().getMonoCounter() && counter.getColourCounter() >= counterBeforeOptional.get().getColourCounter()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private Optional<Counter> counterBefore(Counter counter) {
        Optional<Counter> counterOptional = counterRepository.getTopByCounterDateBeforeAndDevice_SerialNumberOrderByCounterDateDesc(counter.getCounterDate(), counter.getDevice().getSerialNumber());
        return counterOptional;
    }

    private Optional<Counter> counterAfter(Counter counter) {
        Optional<Counter> counterOptional = counterRepository.getFirstByCounterDateAfterAndDevice_SerialNumberOrderByCounterDateAsc(counter.getCounterDate(), counter.getDevice().getSerialNumber());
        return counterOptional;
    }

    public List<Counter> findAllByDeviceSerialNumber(String serialNumber) {
        return counterRepository.findAllByDeviceSerialNumberOrderByCounterDateDesc(serialNumber);
    }

    public Counter findById(Long id) {
        return counterRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(id,"counter"));
    }

    public boolean delete(Long id) {
        Optional<Counter> counter = counterRepository.findById(id);
        if (counter.isPresent()) {
            counterRepository.delete(counter.get());
            return true;
        } else {
            return false;
        }
    }

    public List<Counter> findAll() {
        return counterRepository.findAll();
    }

}
