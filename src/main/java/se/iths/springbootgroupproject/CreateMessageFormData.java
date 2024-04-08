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


//    public CreateMessageFormData() {
//    }
//
//
//    //this is for updating existing messages
//
//    public CreateMessageFormData(String messageTitle, String messageBody) {
//        this.messageTitle = messageTitle;
//        this.messageBody = messageBody;
//
//    }

    public Message toEntity(User user) {
        var message = new Message();
        message.setMessageTitle(messageTitle);
        message.setMessageBody(messageBody);
        message.setUser(user);
        return message;
    }
}
