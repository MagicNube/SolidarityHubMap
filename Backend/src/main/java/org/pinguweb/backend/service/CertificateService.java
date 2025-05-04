package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Certificate;
import org.pinguweb.backend.repository.CertificateRepository;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {this.certificateRepository = certificateRepository;}
    public Certificate saveCertificate(Certificate certificate) {return certificateRepository.save(certificate);}
}
