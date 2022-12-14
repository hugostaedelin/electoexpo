package fr.hstaedelin.electoexpo.services;

import fr.hstaedelin.electoexpo.configuration.exceptions.NotFoundException;
import fr.hstaedelin.electoexpo.models.dto.MuseumDTO;
import fr.hstaedelin.electoexpo.models.job.Museum;
import fr.hstaedelin.electoexpo.repositories.MuseumRepository;
import fr.hstaedelin.electoexpo.services.mappers.MuseumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MuseumService {
    private final MuseumRepository museumRepository;
    private final MuseumMapper museumMapper;

    @Autowired
    public MuseumService(MuseumRepository museumRepository, MuseumMapper museumMapper) {
        this.museumRepository = museumRepository;
        this.museumMapper = museumMapper;
    }

    public Iterable<MuseumDTO> getMuseums() {
        return this.museumMapper.museumList(this.museumRepository.findAll());
    }

    public Optional<MuseumDTO> getMuseumByID(Integer id) {
        return Optional.of(this.museumMapper.map(this.museumRepository.findById(id).orElseThrow(NotFoundException::new)));
    }

    public Museum addMuseum(MuseumDTO museumDTO) {
        Museum object = this.museumMapper.map(museumDTO);
        this.museumRepository.save(object);
        return object;
    }

    public Museum update(Integer id, MuseumDTO objectDTO) {
        Optional<MuseumDTO> object = this.getMuseumByID(id);
        if (object.isPresent()) {
            this.museumRepository.save(this.update(objectDTO, this.museumMapper.map(object.get())));
            return this.museumMapper.map(object.get());
        } else return null;
    }

    private Museum update(MuseumDTO museumDTO, Museum museum) {
        museum.setLabel(museumDTO.getLabel());
        return museum;
    }

    public void removeAll() {
        museumRepository.deleteAll();
    }

    public void removeById(Integer id) {
        Optional<MuseumDTO> type = this.getMuseumByID(id);
        type.ifPresent(objectDTO -> this.museumRepository.delete(this.museumMapper.map(objectDTO)));
    }
}
