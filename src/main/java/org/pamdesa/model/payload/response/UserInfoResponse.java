package org.pamdesa.model.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
}
