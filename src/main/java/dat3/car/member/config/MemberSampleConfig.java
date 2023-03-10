package dat3.car.member.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dat3.car.member.dto.MemberRequest;
import dat3.car.member.entity.Member;

@Configuration
public class MemberSampleConfig {
    @Bean
    public List<Member> memberSamples() {
        var members = Arrays.asList(new Member[] {
            new Member("user1", "pass", "user1@email.com", "John", "Doe", "123 Main St", "New York", "10001"),
            new Member("user2", "pass", "user2@email.com", "Jane", "Smith", "456 Elm St", "Los Angeles", "90001"),
            new Member("user3", "pass", "user3@email.com", "Bob", "Johnson", "789 Oak St", "Chicago", "60601"),
            new Member("admin1", "pass", "user4@email.com", "Emily", "Brown", "246 Pine St", "Houston", "77001"),
            new Member("admin2", "pass", "user5@email.com", "Michael", "Davis", "369 Cedar St", "Phoenix", "85001")
        });

        String[] favoriteCarColors = {"green", "red", "blue"};

        Map<String,String> phones = new HashMap<>();
        phones.put("work", "555-555-5555");
        phones.put("home", "555-555-1234");
        phones.put("mobile", "555-555-4321");

        for (Member member : members) {
            member.setFavoriteCarColors(Arrays.asList(favoriteCarColors));
            member.setPhones(phones);
            member.setRoles(Arrays.asList(new String[] {"MEMBER"}));
            member.setAccountNonExpired(true);
            member.setAccountNonLocked(true);
            member.setEnabled(true);
            member.setCredentialsNonExpired(true);
        }

        // set the last to members as admins
        members.get(3).setRoles(Arrays.asList(new String[] {"ADMIN"}));
        members.get(4).setRoles(Arrays.asList(new String[] {"ADMIN"}));

        return members;
    }

    @Bean
    public List<MemberRequest> memberRequestSamples() {
        return memberSamples().stream().map(c -> new MemberRequest(c)).collect(Collectors.toList());
    }
}
