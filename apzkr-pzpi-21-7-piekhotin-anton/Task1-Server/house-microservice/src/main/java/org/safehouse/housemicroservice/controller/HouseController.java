package org.safehouse.housemicroservice.controller;

import lombok.RequiredArgsConstructor;
import org.safehouse.housemicroservice.model.dto.HouseInfoDto;
import org.safehouse.housemicroservice.model.entity.House;
import org.safehouse.housemicroservice.service.HouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/house")
public class HouseController {

    private final HouseService houseService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getHouseById(@PathVariable Long id) {
        if (id == null || id <= 0)
            return ResponseEntity.badRequest().body("Invalid id");
        if (!houseService.existById(id))
            return ResponseEntity.badRequest().body("House house with this id not found");

        return ResponseEntity.ok(houseService.getHouseById(id));
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createHouse(@PathVariable Long userId, @RequestBody HouseInfoDto houseDto) {
        if (userId == null || userId <= 0)
            return ResponseEntity.badRequest().body("Invalid user id");

        if (houseDto.getName() == null || houseDto.getName().isEmpty()
                || houseDto.getAddress() == null || houseDto.getAddress().isEmpty())
            return ResponseEntity.badRequest().body("Invalid data");

        House houseToCreate = House.builder()
                .name(houseDto.getName())
                .address(houseDto.getAddress())
                .userId(userId)
                .build();
        houseService.createHouse(houseToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(houseToCreate);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteHouseById(@PathVariable Long id) {
        if (id == null || id <= 0)
            return ResponseEntity.badRequest().body("Invalid id");
        if (!houseService.existById(id))
            return ResponseEntity.badRequest().body("House house with this id not found");

        return ResponseEntity.ok(houseService.deleteHouseById(id));
    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<?> getHousesByUserId(@PathVariable Long userId) {
        if (userId == null || userId <= 0)
            return ResponseEntity.badRequest().body("Invalid id");

        return ResponseEntity.ok(houseService.getHousesByUserId(userId));
    }

    @GetMapping("/devices/{houseId}")
    public ResponseEntity<?> getDevicesByHouseId(@PathVariable Long houseId) {
        if (houseId == null || houseId <= 0)
            return ResponseEntity.badRequest().body("Invalid id");

        return ResponseEntity.ok(houseService.getHouseDevices(houseId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllHouses() {
        List<House> housesToReturn = houseService.getAllHouses();
        if (housesToReturn == null || housesToReturn.isEmpty())
            return ResponseEntity.badRequest().body("No houses found");

        return ResponseEntity.ok(houseService.getAllHouses());
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editHouse(@RequestBody HouseInfoDto houseInfoDto, @PathVariable Long id) {
        if (id == null || id <= 0)
            return ResponseEntity.badRequest().body("Invalid id");

        if (houseInfoDto == null)
            return ResponseEntity.badRequest().body("Invalid data");

        if (houseService.getHouseById(id) == null)
            return ResponseEntity.badRequest().body("House with this id not found");

        if (houseService.editHouse(id, houseInfoDto)) {
            return ResponseEntity.ok().body("House edited successfully");
        } else {
            return ResponseEntity.badRequest().body("Error while editing house");
        }
    }
}
