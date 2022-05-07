package co.com.mimeli.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class BlackListRequest {
	@NotBlank
	private String ip;
}
