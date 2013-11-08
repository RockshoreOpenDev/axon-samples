package net.rockshore.axon.chat.query;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ChatRoomView {
	@Id
	private String chatRoomName;
	
}
