package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Donation;
import org.springframework.stereotype.Service;
import org.pinguweb.backend.repository.DonationRepository;

@Service
public class DonationService {
    private final DonationRepository donationRepository;
    public DonationService(DonationRepository donationRepository) {this.donationRepository = donationRepository;}
    public Donation saveDonation(Donation donation) {return donationRepository.save(donation);}
}
