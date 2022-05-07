package co.com.mimeli.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlackListResponse {
	private String ip;
	private String message;
}
