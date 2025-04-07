package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Donation;
import org.pinguweb.backend.repository.DonationRepository;
import org.springframework.stereotype.Service;

@Service
public class DonationService {
    private final DonationRepository donationRepository;
    public DonationService(DonationRepository donationRepository) {this.donationRepository = donationRepository;}
    public Donation saveDonation(Donation donation) {return donationRepository.save(donation);}
}
