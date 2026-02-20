package dev.suspicious.backend.auth;

import dev.suspicious.backend.auth.dto.*;
import dev.suspicious.backend.model.Role;
import dev.suspicious.backend.model.User;
import dev.suspicious.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final NonceService nonceService;
    private final AlgorandSignatureService signatureService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(
            NonceService nonceService,
            AlgorandSignatureService signatureService,
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.nonceService = nonceService;
        this.signatureService = signatureService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // Step 1: Request nonce
    @PostMapping("/nonce")
    public NonceResponse getNonce(@RequestBody NonceRequest req) {
        String nonce = nonceService.generateNonce(req.address());
        return new NonceResponse(nonce);
    }

    // Step 2: Verify signature and return JWT
    @PostMapping("/verify")
    public VerifyResponse verify(@RequestBody VerifyRequest req) {

        String nonce = nonceService.getNonce(req.address());
        Role role = req.role();

        if (nonce == null) {
            throw new RuntimeException("Nonce expired or missing");
        }

        boolean ok = signatureService.verify(
                req.address(),
                nonce,
                req.signature()
        );

        if (!ok) {
            throw new RuntimeException("Invalid signature");
        }

        User user = userRepository.findById(req.address())
                .orElseGet(() -> {
                    User newUser = new User(req.address(), req.role());
                    return userRepository.save(newUser);
                });

        nonceService.clearNonce(req.address());

        String token = jwtService.generateToken(req.address());

        return new VerifyResponse(token);
    }
}
