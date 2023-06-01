package stellarburgers.user;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginPojo {
    private String email;
    private String password;
}
