package dev.suspicious.backend.auth;

import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.crypto.Signature;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AlgorandSignatureService {

    public boolean verify(String address, String nonce, String signatureBase64) {
        try {
            Address addr = new Address(address);
            byte[] msg = nonce.getBytes();
            byte[] sigBytes = Base64.getDecoder().decode(signatureBase64);

            Signature sig = new Signature(sigBytes);

            return addr.verifyBytes(msg, sig);

        } catch (Exception e) {
            return false;
        }
    }
}