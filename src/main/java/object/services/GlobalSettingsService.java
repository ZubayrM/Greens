package object.services;

import lombok.AllArgsConstructor;
import object.model.GlobalSettings;
import object.repositories.GlobalSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class GlobalSettingsService {

    private GlobalSettingsRepository globalSettingsRepository;

    public Map<String, String> getSetting(){
        Iterable<GlobalSettings> globalSettings = globalSettingsRepository.findAll();
        Map<String, String> settings = new HashMap<>();
        globalSettings.forEach(gS -> settings.put(gS.getName(), gS.getValue()));
        return settings;
    }

}
