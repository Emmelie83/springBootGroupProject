package se.iths.springbootgroupproject;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.iths.springbootgroupproject.entities.Message;
import se.iths.springbootgroupproject.entities.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageFormData {

    @NotNull
    @Size(min = 1, max = 100)
    private String messageTitle;

    @NotNull
    @Size(min = 1, max = 2000)
    private String messageBody;

    private boolean makePublic;

    public Message toEntity(User user) {
        var message = new Message();
        message.setLastModifiedDate(null);
        message.setMessageTitle(messageTitle);
        message.setMessageBody(messageBody);
        message.setPublic(makePublic);
        message.setUser(user);
        return message;
    }
}
