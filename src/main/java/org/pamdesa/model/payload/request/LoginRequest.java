package org.pamdesa.model.payload.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

	@NotBlank(message = "NotBlank")
	private String username;

	@NotBlank(message = "NotBlank")
	private String password;

}
