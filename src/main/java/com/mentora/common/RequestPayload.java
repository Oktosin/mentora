package com.mentora.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayload {
	
	private int pageNumber;
	private int pageSize;

}
