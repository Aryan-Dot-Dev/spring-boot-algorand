package dev.suspicious.backend.auth;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NonceService {

    private final Map<String, String> nonces = new ConcurrentHashMap<>();
    private final Map<String, Instant> expiry = new ConcurrentHashMap<>();

    public String generateNonce(String address) {
        String nonce = UUID.randomUUID().toString();
        System.out.println("NONCE request for: " + address);
        nonces.put(address, nonce);
        expiry.put(address, Instant.now().plusSeconds(300)); // 5 min

        return nonce;
    }

    public String getNonce(String address) {
        if (!nonces.containsKey(address)) return null;

        if (Instant.now().isAfter(expiry.get(address))) {
            nonces.remove(address);
            expiry.remove(address);
            return null;
        }

        return nonces.get(address);
    }

    public void clearNonce(String address) {
        nonces.remove(address);
        expiry.remove(address);
    }
}
