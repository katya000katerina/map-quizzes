package com.mapquizzes.services.interfaces;

import java.io.IOException;
import java.security.Principal;

public interface CertificateService {
    byte[] getCertificate(Principal principal) throws IOException;
}
