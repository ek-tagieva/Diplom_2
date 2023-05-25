package stellarburgers.order;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPojo {
    private List<String> ingredients;

}
