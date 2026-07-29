package za.co.lz.service.team.transfer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.transfer.TransferWindow;
import za.co.lz.repository.team.transfer.TransferWindowRepository;
import za.co.lz.service.team.transfer.ITransferWindowService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferWindowServiceImpl implements ITransferWindowService {

    @Autowired
    private TransferWindowRepository transferWindowRepository;

    @Override
    public TransferWindow create(TransferWindow window) {
        return transferWindowRepository.save(window);
    }

    @Override
    public List<TransferWindow> findAll() {
        return transferWindowRepository.findAll();
    }

    @Override
    public Optional<TransferWindow> findById(UUID uuid) {
        return transferWindowRepository.findById(uuid);
    }

    @Override
    public TransferWindow update(TransferWindow window, UUID uuid) {
        return transferWindowRepository.save(window);
    }

    @Override
    public void delete(UUID uuid) {
        transferWindowRepository.deleteById(uuid);
    }

    @Override
    public List<TransferWindow> findOpenWindows(LocalDate date) {
        return transferWindowRepository.findAll().stream()
                .filter(window -> window.isOpen(date))
                .toList();
    }
}
