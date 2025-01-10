package com.bankAccount.bankAccount.services.auth;
import com.bankAccount.bankAccount.config.UserDetail;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));


        return UserDetail.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .accountLocked(false)
                .disabled(false)
                .build();
    }
}
