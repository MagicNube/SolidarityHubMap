package org.pinguweb.backend.service;


import org.pinguweb.backend.model.Preference;
import org.springframework.stereotype.Service;
import org.pinguweb.backend.repository.PreferenceRepository;

@Service
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;
    public PreferenceService(PreferenceRepository preferenceRepository) {this.preferenceRepository = preferenceRepository;}
    public Preference savePreference(Preference preference) {return preferenceRepository.save(preference);}
}
