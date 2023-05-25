package stellarburgers.user;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserRegistrationPojo {
    private String email;
    private String password;
    private String name;

}
