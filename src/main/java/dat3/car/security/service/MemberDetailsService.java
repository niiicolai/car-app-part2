package dat3.car.security.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dat3.car.security.entity.UserWithRoles;
import dat3.car.security.repository.UserWithRolesRepository;

@Service
public class MemberDetailsService implements UserDetailsService {
    UserWithRolesRepository userWithRolesRepository;

    public MemberDetailsService(UserWithRolesRepository userWithRolesRepository) {
        this.userWithRolesRepository = userWithRolesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    
        Optional<UserWithRoles> userOpt = userWithRolesRepository.findById(username);
        if (userOpt.isEmpty())
            throw new UsernameNotFoundException("User doesn't exist!");
        return userOpt.get();
    }
}
