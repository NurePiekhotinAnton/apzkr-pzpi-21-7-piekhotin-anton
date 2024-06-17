package org.safehouse.housemicroservice.service;

import lombok.RequiredArgsConstructor;
import org.safehouse.housemicroservice.model.dto.DeviceDto;
import org.safehouse.housemicroservice.model.dto.HouseInfoDto;
import org.safehouse.housemicroservice.model.entity.House;
import org.safehouse.housemicroservice.repository.HouseRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final RestTemplate restTemplate;

    public House getHouseById(Long id) {
        return houseRepository.findById(id).orElse(null);
    }

    public List<House> getHousesByUserId(Long userId) {
        if (userId == null || userId <= 0)
            return null;
        return houseRepository.findByUserId(userId);
    }

    public House createHouse(House house) {
        if (!existById(house.getId())) {
            return houseRepository.save(house);
        } else {
            return null;
        }
    }

    public boolean deleteHouseById(Long id) {
        if (houseRepository.existsById(id)) {
            houseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existById(Long id) {
        if (id == null || id <= 0)
            return false;
        return houseRepository.existsById(id);
    }

    public List<DeviceDto> getHouseDevices(Long houseId) {
        if (houseId == null || houseId <= 0)
            return null;

        ResponseEntity<List<DeviceDto>> response = restTemplate.exchange("http://localhost:8083/api/v1/device/my/" + houseId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public List<House> getAllHouses() {
        if (houseRepository.findAll().isEmpty()) {
            return null;
        }
        return houseRepository.findAll();
    }

    public boolean editHouse(Long id, HouseInfoDto houseInfoDto) {
        if (existById(id)) {
            House houseToEdit = houseRepository.findById(id).orElse(null);
			if (houseToEdit != null) {
				houseToEdit.setName(houseInfoDto.getName());
				houseToEdit.setAddress(houseInfoDto.getAddress());
				houseRepository.save(houseToEdit);
				return true;
			}
        }
		return false;
    }
}
