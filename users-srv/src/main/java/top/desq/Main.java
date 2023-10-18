package top.desq;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import top.desq.dto.CreateUserRequest;
import top.desq.dto.UserResponse;
import top.desq.entity.User;

public class Main {

    public static void main(String[] args) {

        var dto = new CreateUserRequest("alex", "email@email.com", "password");
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User map = modelMapper.map(dto, User.class);
        UserResponse userResponse = modelMapper.map(map, UserResponse.class);
        System.out.println(map);
        System.out.println(userResponse);
    }
}
