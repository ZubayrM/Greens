package object.repositories;

import object.model.GlobalSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {



}