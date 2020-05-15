package intland.assignment.winaball.repository;

import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.Territory;
import org.springframework.data.repository.CrudRepository;

public interface TerritoryRepository extends CrudRepository<Territory, Integer> {
  Territory findByCountry(Country country);
}
