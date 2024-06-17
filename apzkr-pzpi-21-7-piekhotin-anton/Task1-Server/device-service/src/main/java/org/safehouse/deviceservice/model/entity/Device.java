package org.safehouse.deviceservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "device")
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
public class Device {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "house_id", nullable = false)
	Long houseId;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	DeviceType type;
}
