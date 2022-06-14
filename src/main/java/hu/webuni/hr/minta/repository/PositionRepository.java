package hu.webuni.hr.minta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.minta.model.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {

	public Optional<Position> findByName(String name);
}
